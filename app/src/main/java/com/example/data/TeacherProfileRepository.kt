package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.TeacherProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.teacherProfileDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "teacher_profile_prefs"
)

class TeacherProfileRepository(private val context: Context) {

    private object Keys {
        val EMPLOYEE_NAME = stringPreferencesKey("employee_name")
        val FATHER_NAME = stringPreferencesKey("father_name")
        val EMPLOYEE_ID = stringPreferencesKey("employee_id")
        val DESIGNATION = stringPreferencesKey("designation")
        val SUBJECT = stringPreferencesKey("subject")
        val SCHOOL_NAME = stringPreferencesKey("school_name")
        val UDISE_CODE = stringPreferencesKey("udise_code")
        val BLOCK = stringPreferencesKey("block")
        val DISTRICT = stringPreferencesKey("district")
        val PAY_LEVEL = stringPreferencesKey("pay_level")
        val BASIC_PAY = stringPreferencesKey("basic_pay")
        val JOINING_DATE = stringPreferencesKey("joining_date")
        val DOB = stringPreferencesKey("dob")
        val MOBILE = stringPreferencesKey("mobile")
        val PAN_NUMBER = stringPreferencesKey("pan_number")
        val DDO_NAME = stringPreferencesKey("ddo_name")
    }

    private val defaultProfile = TeacherProfile()

    val profileFlow: Flow<TeacherProfile> = context.teacherProfileDataStore.data.map { prefs ->
        TeacherProfile(
            employeeName = prefs[Keys.EMPLOYEE_NAME] ?: defaultProfile.employeeName,
            fatherOrHusbandName = prefs[Keys.FATHER_NAME] ?: defaultProfile.fatherOrHusbandName,
            employeeId = prefs[Keys.EMPLOYEE_ID] ?: defaultProfile.employeeId,
            designation = prefs[Keys.DESIGNATION] ?: defaultProfile.designation,
            subject = prefs[Keys.SUBJECT] ?: defaultProfile.subject,
            schoolName = prefs[Keys.SCHOOL_NAME] ?: defaultProfile.schoolName,
            udiseCode = prefs[Keys.UDISE_CODE] ?: defaultProfile.udiseCode,
            block = prefs[Keys.BLOCK] ?: defaultProfile.block,
            district = prefs[Keys.DISTRICT] ?: defaultProfile.district,
            payLevel = prefs[Keys.PAY_LEVEL] ?: defaultProfile.payLevel,
            basicPay = prefs[Keys.BASIC_PAY] ?: defaultProfile.basicPay,
            joiningDate = prefs[Keys.JOINING_DATE] ?: defaultProfile.joiningDate,
            dob = prefs[Keys.DOB] ?: defaultProfile.dob,
            mobile = prefs[Keys.MOBILE] ?: defaultProfile.mobile,
            panNumber = prefs[Keys.PAN_NUMBER] ?: defaultProfile.panNumber,
            ddoName = prefs[Keys.DDO_NAME] ?: defaultProfile.ddoName
        )
    }

    suspend fun saveProfile(profile: TeacherProfile) {
        context.teacherProfileDataStore.edit { prefs ->
            prefs[Keys.EMPLOYEE_NAME] = profile.employeeName
            prefs[Keys.FATHER_NAME] = profile.fatherOrHusbandName
            prefs[Keys.EMPLOYEE_ID] = profile.employeeId
            prefs[Keys.DESIGNATION] = profile.designation
            prefs[Keys.SUBJECT] = profile.subject
            prefs[Keys.SCHOOL_NAME] = profile.schoolName
            prefs[Keys.UDISE_CODE] = profile.udiseCode
            prefs[Keys.BLOCK] = profile.block
            prefs[Keys.DISTRICT] = profile.district
            prefs[Keys.PAY_LEVEL] = profile.payLevel
            prefs[Keys.BASIC_PAY] = profile.basicPay
            prefs[Keys.JOINING_DATE] = profile.joiningDate
            prefs[Keys.DOB] = profile.dob
            prefs[Keys.MOBILE] = profile.mobile
            prefs[Keys.PAN_NUMBER] = profile.panNumber
            prefs[Keys.DDO_NAME] = profile.ddoName
        }
    }
}
