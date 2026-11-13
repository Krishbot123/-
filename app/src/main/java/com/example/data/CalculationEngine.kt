package com.example.data

import com.example.model.ArrearMonthRow
import com.example.model.ArrearSummary
import com.example.model.BonusResult
import com.example.model.BudgetResult
import com.example.model.FormatFormData
import com.example.model.MdmCalculationResult
import com.example.model.PensionResult
import com.example.model.TaBillResult
import com.example.model.TaxComparisonResult
import kotlin.math.max
import kotlin.math.roundToLong

object CalculationEngine {

    private val hindiMonths = listOf(
        "जनवरी", "फरवरी", "मार्च", "अप्रैल", "मई", "जून",
        "जुलाई", "अगस्त", "सितम्बर", "अक्टूबर", "नवम्बर", "दिसम्बर"
    )

    fun calculateArrear(data: FormatFormData): ArrearSummary {
        val oldBasic = data.oldBasicPay.toLongOrNull()?.coerceAtLeast(0L) ?: 39300L
        val newBasic = data.newBasicPay.toLongOrNull()?.coerceAtLeast(0L) ?: 44300L
        val oldDaPct = data.oldDaPercent.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 50.0
        val newDaPct = data.newDaPercent.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 55.0
        val hraPct = data.hraPercent.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 9.0
        val monthsCount = (data.arrearMonths.toIntOrNull() ?: 6).coerceIn(1, 36)

        val startIndex = hindiMonths.indexOfFirst {
            data.arrearStartMonth.contains(it, ignoreCase = true)
        }.let { if (it >= 0) it else 6 } // Default July

        val baseYear = Regex("\\d{4}").find(data.arrearStartMonth)?.value?.toIntOrNull() ?: 2025

        val rows = (0 until monthsCount).map { offset ->
            val monthIndex = (startIndex + offset) % 12
            val year = baseYear + ((startIndex + offset) / 12)
            val label = "${hindiMonths[monthIndex]} $year"

            val drawnDa = (oldBasic * oldDaPct / 100.0).roundToLong()
            val drawnHra = (oldBasic * hraPct / 100.0).roundToLong()
            val drawnGross = oldBasic + drawnDa + drawnHra

            val dueDa = (newBasic * newDaPct / 100.0).roundToLong()
            val dueHra = (newBasic * hraPct / 100.0).roundToLong()
            val dueGross = newBasic + dueDa + dueHra

            val diff = dueGross - drawnGross
            val nps = if (data.includeNpsDeduction && diff > 0) {
                (( (newBasic + dueDa) - (oldBasic + drawnDa) ) * 0.10).roundToLong().coerceAtLeast(0L)
            } else {
                0L
            }
            val net = diff - nps

            ArrearMonthRow(
                serial = offset + 1,
                monthLabel = label,
                drawnBasic = oldBasic,
                drawnDa = drawnDa,
                drawnHra = drawnHra,
                drawnGross = drawnGross,
                dueBasic = newBasic,
                dueDa = dueDa,
                dueHra = dueHra,
                dueGross = dueGross,
                grossDifference = diff,
                npsDeduction = nps,
                netPayable = net
            )
        }

        return ArrearSummary(
            rows = rows,
            totalDrawn = rows.sumOf { it.drawnGross },
            totalDue = rows.sumOf { it.dueGross },
            totalGrossDifference = rows.sumOf { it.grossDifference },
            totalNpsDeduction = rows.sumOf { it.npsDeduction },
            totalNetPayable = rows.sumOf { it.netPayable }
        )
    }

    fun calculateIncomeTax(data: FormatFormData): TaxComparisonResult {
        val gross = data.grossAnnualSalary.toLongOrNull()?.coerceAtLeast(0L) ?: 864000L
        val hraEx = data.hraExemption.toLongOrNull()?.coerceAtLeast(0L) ?: 0L
        val ded80c = (data.deduction80C.toLongOrNull() ?: 0L).coerceIn(0L, 150000L)
        val nps80ccd = (data.nps80CCD1B.toLongOrNull() ?: 0L).coerceIn(0L, 50000L)
        val med80d = (data.medical80D.toLongOrNull() ?: 0L).coerceIn(0L, 100000L)
        val homeLoan = (data.homeLoanInterest.toLongOrNull() ?: 0L).coerceIn(0L, 200000L)

        // Old Regime (Std Ded ₹50,000 + exemptions)
        val oldStdDed = 50000L
        val oldTotalDed = oldStdDed + hraEx + ded80c + nps80ccd + med80d + homeLoan
        val oldTaxable = max(0L, gross - oldTotalDed)
        val oldBase = computeOldRegimeSlabTax(oldTaxable)
        val oldAfter87A = if (oldTaxable <= 500000L) 0L else oldBase
        val oldCess = (oldAfter87A * 0.04).roundToLong()
        val oldTotal = oldAfter87A + oldCess

        // New Regime FY 2025-26 / 2026-27 (Std Ded ₹75,000; 87A rebate up to ₹12,00,000 taxable)
        val newStdDed = 75000L
        val newTaxable = max(0L, gross - newStdDed)
        val newBase = computeNewRegimeSlabTax(newTaxable)
        val newAfter87A = if (newTaxable <= 1200000L) 0L else newBase
        val newCess = (newAfter87A * 0.04).roundToLong()
        val newTotal = newAfter87A + newCess

        val recommended = if (newTotal <= oldTotal) "New Regime (नई कर व्यवस्था)" else "Old Regime (पुरानी कर व्यवस्था)"
        val savings = kotlin.math.abs(oldTotal - newTotal)

        return TaxComparisonResult(
            financialYear = data.taxFinancialYear,
            grossIncome = gross,
            oldTotalDeductions = oldTotalDed,
            oldTaxableIncome = oldTaxable,
            oldBaseTax = oldAfter87A,
            oldCess = oldCess,
            oldTotalTax = oldTotal,
            newStandardDeduction = newStdDed,
            newTaxableIncome = newTaxable,
            newBaseTax = newAfter87A,
            newCess = newCess,
            newTotalTax = newTotal,
            recommendedRegime = recommended,
            taxSavings = savings
        )
    }

    private fun computeOldRegimeSlabTax(taxable: Long): Long {
        if (taxable <= 250000L) return 0L
        var tax = 0.0
        if (taxable > 250000L) {
            tax += (minOf(taxable, 500000L) - 250000L) * 0.05
        }
        if (taxable > 500000L) {
            tax += (minOf(taxable, 1000000L) - 500000L) * 0.20
        }
        if (taxable > 1000000L) {
            tax += (taxable - 1000000L) * 0.30
        }
        return tax.roundToLong()
    }

    private fun computeNewRegimeSlabTax(taxable: Long): Long {
        if (taxable <= 400000L) return 0L
        var tax = 0.0
        if (taxable > 400000L) {
            tax += (minOf(taxable, 800000L) - 400000L) * 0.05
        }
        if (taxable > 800000L) {
            tax += (minOf(taxable, 1200000L) - 800000L) * 0.10
        }
        if (taxable > 1200000L) {
            tax += (minOf(taxable, 1600000L) - 1200000L) * 0.15
        }
        if (taxable > 1600000L) {
            tax += (minOf(taxable, 2000000L) - 1600000L) * 0.20
        }
        if (taxable > 2000000L) {
            tax += (minOf(taxable, 2400000L) - 2000000L) * 0.25
        }
        if (taxable > 2400000L) {
            tax += (taxable - 2400000L) * 0.30
        }
        return tax.roundToLong()
    }

    fun calculateMdm(data: FormatFormData): MdmCalculationResult {
        val primary = data.mdmPrimaryCount.toLongOrNull()?.coerceAtLeast(0L) ?: 0L
        val upperPrimary = data.mdmUpperPrimaryCount.toLongOrNull()?.coerceAtLeast(0L) ?: 0L
        val days = data.mdmWorkingDays.toLongOrNull()?.coerceAtLeast(0L) ?: 0L

        val primaryMeals = primary * days
        val upperMeals = upperPrimary * days

        // Standard PM POSHAN norms: 100g Primary, 150g Upper Primary; Conversion cost ₹6.19 & ₹9.29
        val primaryGrainKg = primaryMeals * 0.100
        val upperGrainKg = upperMeals * 0.150
        val primaryCost = primaryMeals * 6.19
        val upperCost = upperMeals * 9.29

        return MdmCalculationResult(
            month = data.mdmMonth,
            primaryStudentMeals = primaryMeals,
            upperPrimaryStudentMeals = upperMeals,
            primaryGrainKg = primaryGrainKg,
            upperPrimaryGrainKg = upperGrainKg,
            totalGrainKg = primaryGrainKg + upperGrainKg,
            primaryCookingCost = primaryCost,
            upperPrimaryCookingCost = upperCost,
            totalCookingCost = primaryCost + upperCost
        )
    }

    fun calculateAcpFixationPay(currentLevel: String, currentBasic: Long, nextLevel: String): Pair<Long, Long> {
        val currentCells = FormatsCatalog.payMatrixLevels[currentLevel] ?: return currentBasic to currentBasic
        val idx = currentCells.indexOfFirst { it >= currentBasic }.let { if (it >= 0) it else 0 }
        val nextCellInSameLevel = currentCells.getOrElse(idx + 1) { currentCells.last() }
        val targetCells = FormatsCatalog.payMatrixLevels[nextLevel] ?: return nextCellInSameLevel to nextCellInSameLevel
        val placedInNextLevel = targetCells.firstOrNull { it >= nextCellInSameLevel } ?: targetCells.last()
        return nextCellInSameLevel to placedInNextLevel
    }

    fun calculatePlSurrenderAmount(basicPay: Long, daPercent: Double, days: Int = 15): Long {
        val totalMonthly = basicPay + (basicPay * daPercent / 100.0)
        return ((totalMonthly / 30.0) * days).roundToLong()
    }

    fun calculatePension(data: FormatFormData): PensionResult {
        val lastBasic = data.newBasicPay.toLongOrNull()
            ?: data.oldBasicPay.toLongOrNull()
            ?: 56100L
        val daPct = data.newDaPercent.toDoubleOrNull() ?: 60.0
        val daOnBasic = (lastBasic * daPct / 100.0).roundToLong()
        val totalEmoluments = lastBasic + daOnBasic
        val years = (Regex("\\d+").find(data.totalServiceYears)?.value?.toIntOrNull() ?: 30)
            .coerceIn(10, 33)
        // Rajasthan Pension Rule: 50% of last basic for >= 28 years qualifying service; pro-rata below 28
        val fullPension = if (years >= 28) {
            (lastBasic * 0.50).roundToLong()
        } else {
            ((lastBasic * 0.50) * (years / 28.0)).roundToLong()
        }
        val commPct = (data.commutationPercent.toIntOrNull() ?: 40).coerceIn(0, 40)
        val commutedMonthly = (fullPension * commPct / 100.0).roundToLong()
        // Standard Rajasthan Commutation factor at age 60 = 8.194
        val age = data.retirementAge.toIntOrNull() ?: 60
        val factor = when {
            age <= 55 -> 8.859
            age <= 58 -> 8.532
            else -> 8.194
        }
        val commutationLumpSum = (commutedMonthly * 12.0 * factor).roundToLong()
        val residualPension = (fullPension - commutedMonthly).coerceAtLeast(0L)
        // DCRG Gratuity: (Basic + DA) / 2 * completed six-monthly periods / 2 -> max ₹20,00,000 / ₹25,00,000
        val sixMonthlyPeriods = (years * 2).coerceAtMost(66)
        val dcrgRaw = ((totalEmoluments / 4.0) * sixMonthlyPeriods).roundToLong()
        val dcrg = dcrgRaw.coerceAtMost(2500000L)

        return PensionResult(
            lastBasicPay = lastBasic,
            daPercent = daPct,
            daOnBasic = daOnBasic,
            totalEmoluments = totalEmoluments,
            qualifyingYears = years,
            fullBasicPension = fullPension,
            commutationPercent = commPct,
            commutedMonthlyPension = commutedMonthly,
            commutationLumpSum = commutationLumpSum,
            residualMonthlyPension = residualPension,
            dcrgGratuity = dcrg
        )
    }

    fun calculateBonus2025_26(data: FormatFormData): BonusResult {
        val level = data.oldPayLevel.uppercase().trim()
        val levelNum = Regex("\\d+").find(level)?.value?.toIntOrNull() ?: 10
        val isEligible = levelNum <= 12 // Non-gazetted employees up to Level-12
        val months = (data.bonusMonthsWorked.toIntOrNull() ?: 12).coerceIn(1, 12)
        val maxCeiling = 7000L
        val grossBonus = if (!isEligible || months < 6) {
            0L
        } else {
            ((maxCeiling * 30.0 / 31.0) * (months / 12.0)).roundToLong()
        }
        val cash75 = (grossBonus * 0.75).roundToLong()
        val gpf25 = grossBonus - cash75
        return BonusResult(
            payLevel = level,
            monthsWorked = months,
            maxCeiling = maxCeiling,
            grossBonus = grossBonus,
            cashAmount75 = cash75,
            gpfAmount25 = gpf25,
            isEligible = isEligible && months >= 6
        )
    }

    fun calculateTaBill(data: FormatFormData): TaBillResult {
        val fare = data.taFareAmount.toLongOrNull()?.coerceAtLeast(0L) ?: 1450L
        val days = (data.taDaDays.toIntOrNull() ?: 3).coerceIn(0, 60)
        val rate = data.taDailyRate.toLongOrNull()?.coerceAtLeast(0L) ?: 450L
        val daTotal = days * rate
        return TaBillResult(
            fareAmount = fare,
            daDays = days,
            dailyRate = rate,
            totalDaAmount = daTotal,
            totalClaimAmount = fare + daTotal
        )
    }

    fun calculateBudgetProposal(data: FormatFormData): BudgetResult {
        val salary = data.budgetSalaryDemand.toLongOrNull()?.coerceAtLeast(0L) ?: 4800000L
        val office = data.budgetOfficeExpense.toLongOrNull()?.coerceAtLeast(0L) ?: 120000L
        val repair = data.budgetBuildingRepair.toLongOrNull()?.coerceAtLeast(0L) ?: 250000L
        return BudgetResult(
            salaryHead = salary,
            officeExpenseHead = office,
            repairMaintenanceHead = repair,
            totalDemand = salary + office + repair
        )
    }
}
