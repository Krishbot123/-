package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.FormatsCatalog
import com.example.data.PortalHomeCatalog
import com.example.model.FormatCategory
import com.example.model.FormatItem
import com.example.ui.FormatFilter
import com.example.ui.components.FormatCard
import com.example.ui.components.RajTeachersHeroBanner

@Composable
fun FormatsScreen(
    formats: List<FormatItem>,
    searchQuery: String,
    selectedFilter: FormatFilter,
    onSearchQueryChange: (String) -> Unit,
    onSelectFilter: (FormatFilter) -> Unit,
    onOpenCategory: (FormatCategory) -> Unit,
    onOpenBuilder: (FormatItem) -> Unit,
    onQuickDownload: (FormatItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val isFiltering = searchQuery.isNotBlank() || selectedFilter != FormatFilter.ALL

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "hero_banner") {
                RajTeachersHeroBanner(
                    totalCount = FormatsCatalog.allFormats.size,
                    filteredCount = formats.size
                )
            }

            item(key = "search_bar") {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("formats_search_input"),
                    placeholder = {
                        Text("खोजें: DA 60-63, Bonus, Pension, GPF, GPA, SDMC, Leave, RSR...")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search formats"
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }

            item(key = "filter_chips") {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    items(FormatFilter.entries) { filter ->
                        val selected = selectedFilter == filter
                        FilterChip(
                            selected = selected,
                            onClick = { onSelectFilter(filter) },
                            modifier = Modifier.testTag("filter_chip_${filter.name.lowercase()}"),
                            label = {
                                Text(
                                    text = filter.label,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

            if (isFiltering) {
                if (formats.isEmpty()) {
                    item(key = "empty_state") {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "कोई प्रपत्र नहीं मिला",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "कृपया खोज शब्द बदलें या All फ़िल्टर चुनें।",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    item(key = "results_count") {
                        Text(
                            text = "खोज परिणाम (${formats.size} प्रपत्र)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    items(
                        items = formats,
                        key = { it.id }
                    ) { item ->
                        FormatCard(
                            item = item,
                            onFillAndGenerate = { onOpenBuilder(item) },
                            onQuickDownload = { onQuickDownload(item) }
                        )
                    }
                }
            } else {
                // Default Portal Home View matching all uploaded RajTeachers screenshots
                item(key = "online_services_strip") {
                    OnlineServicesStrip(
                        services = PortalHomeCatalog.onlineServiceCards,
                        onOpenService = onOpenBuilder
                    )
                }

                item(key = "downloads_categories_header") {
                    Text(
                        text = "Downloads Categories (सभी 20 श्रेणियां)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                items(
                    items = FormatsCatalog.downloadCategories,
                    key = { "cat_${it.name}" }
                ) { category ->
                    CategoryGridCard(
                        category = category,
                        onClick = { onOpenCategory(category) }
                    )
                }

                item(key = "state_circulars_table") {
                    StateCircularsTableCard(
                        circulars = PortalHomeCatalog.stateCircularTable,
                        onOpenCircular = onOpenBuilder
                    )
                }

                item(key = "latest_updates_header") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Latest Updates (नवीनतम अपडेट्स)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        TextButton(onClick = { onOpenCategory(FormatCategory.LATEST_UPDATES) }) {
                            Text("View All (10)")
                        }
                    }
                }

                items(
                    items = PortalHomeCatalog.latestUpdates.take(4),
                    key = { "home_${it.id}" }
                ) { item ->
                    FormatCard(
                        item = item,
                        onFillAndGenerate = { onOpenBuilder(item) },
                        onQuickDownload = { onQuickDownload(item) }
                    )
                }

                item(key = "latest_formats_header") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Latest Formats (नवीनतम प्रपत्र)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        TextButton(onClick = { onOpenCategory(FormatCategory.LATEST_FORMATS) }) {
                            Text("View All (10)")
                        }
                    }
                }

                items(
                    items = PortalHomeCatalog.latestFormats.take(4),
                    key = { "home_lf_${it.id}" }
                ) { item ->
                    FormatCard(
                        item = item,
                        onFillAndGenerate = { onOpenBuilder(item) },
                        onQuickDownload = { onQuickDownload(item) }
                    )
                }
            }

            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
