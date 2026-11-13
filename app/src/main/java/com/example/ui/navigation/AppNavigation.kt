package com.example.ui.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FolderShared
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.data.FormatsCatalog
import com.example.model.FormatCategory
import com.example.model.GeneratedFileInfo
import com.example.ui.MainViewModel
import com.example.ui.components.ExportSuccessDialog
import com.example.ui.screens.CalculatorsScreen
import com.example.ui.screens.CategoryDetailScreen
import com.example.ui.screens.FilesAndPortalsScreen
import com.example.ui.screens.FormatBuilderScreen
import com.example.ui.screens.FormatsScreen
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.WarmAmber
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object FormatsRoute

@Serializable
data object CalculatorsRoute

@Serializable
data object FilesPortalsRoute

@Serializable
data class CategoryDetailRoute(val categoryName: String)

@Serializable
data class FormatBuilderRoute(val formatId: String)

private data class TopLevelNavItem<T : Any>(
    val route: T,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RajTeachersApp(
    viewModel: MainViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val formats by viewModel.filteredFormats.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val teacherProfile by viewModel.teacherProfile.collectAsStateWithLifecycle()
    val savedFiles by viewModel.savedFiles.collectAsStateWithLifecycle()
    val currentFormat by viewModel.currentFormat.collectAsStateWithLifecycle()
    val formData by viewModel.formData.collectAsStateWithLifecycle()
    val previewDoc by viewModel.livePreviewDocument.collectAsStateWithLifecycle()
    val isExporting by viewModel.isExporting.collectAsStateWithLifecycle()
    val lastExportedFile by viewModel.lastExportedFile.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    val topLevelItems = listOf(
        TopLevelNavItem(
            route = FormatsRoute,
            label = "Portal",
            selectedIcon = Icons.Filled.Description,
            unselectedIcon = Icons.Outlined.Description,
            testTag = "nav_formats"
        ),
        TopLevelNavItem(
            route = CalculatorsRoute,
            label = "Calculators",
            selectedIcon = Icons.Filled.Calculate,
            unselectedIcon = Icons.Outlined.Calculate,
            testTag = "nav_calculators"
        ),
        TopLevelNavItem(
            route = FilesPortalsRoute,
            label = "Files & Tools",
            selectedIcon = Icons.Filled.FolderShared,
            unselectedIcon = Icons.Outlined.FolderShared,
            testTag = "nav_files_profile"
        )
    )

    val isDetailOrBuilder = currentDestination?.hasRoute<FormatBuilderRoute>() == true ||
        currentDestination?.hasRoute<CategoryDetailRoute>() == true

    fun openFileSafely(fileInfo: GeneratedFileInfo) {
        val opened = runCatching {
            context.startActivity(viewModel.exportManager.openFileIntent(fileInfo))
        }.isSuccess
        if (!opened) {
            runCatching {
                context.startActivity(viewModel.exportManager.shareFileIntent(fileInfo))
            }.onFailure {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        "फाइल सेव हो चुकी है (${fileInfo.name})। खोलने के लिए PDF/Excel/Word व्यूअर ऐप इंस्टॉल करें।"
                    )
                }
            }
        }
    }

    fun shareFileSafely(fileInfo: GeneratedFileInfo) {
        runCatching {
            context.startActivity(viewModel.exportManager.shareFileIntent(fileInfo))
        }.onFailure {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("शेयर करने के लिए कोई ऐप उपलब्ध नहीं है")
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavRail = maxWidth >= 600.dp

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                if (!isDetailOrBuilder) {
                    TopAppBar(
                        title = {
                            Text(
                                text = "RajTeachers",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = WarmAmber
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = DeepTeal,
                            titleContentColor = Color.White
                        )
                    )
                }
            },
            bottomBar = {
                if (!useNavRail && !isDetailOrBuilder) {
                    NavigationBar {
                        topLevelItems.forEach { item ->
                            val selected = currentDestination?.hasRoute(item.route::class) == true
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                modifier = Modifier.testTag(item.testTag),
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useNavRail && !isDetailOrBuilder) {
                    NavigationRail {
                        topLevelItems.forEach { item ->
                            val selected = currentDestination?.hasRoute(item.route::class) == true
                            NavigationRailItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                modifier = Modifier.testTag(item.testTag),
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = FormatsRoute,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable<FormatsRoute> {
                        FormatsScreen(
                            formats = formats,
                            searchQuery = searchQuery,
                            selectedFilter = selectedFilter,
                            onSearchQueryChange = viewModel::updateSearchQuery,
                            onSelectFilter = viewModel::selectFilter,
                            onOpenCategory = { category ->
                                navController.navigate(CategoryDetailRoute(category.name))
                            },
                            onOpenBuilder = { item ->
                                viewModel.openFormatBuilder(item.id)
                                navController.navigate(FormatBuilderRoute(item.id))
                            },
                            onQuickDownload = viewModel::quickDownloadFormat
                        )
                    }

                    composable<CategoryDetailRoute> { backStackEntry ->
                        val args = backStackEntry.toRoute<CategoryDetailRoute>()
                        val category = FormatCategory.entries.firstOrNull { it.name == args.categoryName }
                            ?: FormatCategory.TEACHERS_RELATED
                        CategoryDetailScreen(
                            category = category,
                            onOpenBuilder = { item ->
                                viewModel.openFormatBuilder(item.id)
                                navController.navigate(FormatBuilderRoute(item.id))
                            },
                            onQuickDownload = viewModel::quickDownloadFormat,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable<CalculatorsRoute> {
                        CalculatorsScreen(
                            onOpenFormatBuilder = { item ->
                                viewModel.openFormatBuilder(item.id)
                                navController.navigate(FormatBuilderRoute(item.id))
                            }
                        )
                    }

                    composable<FilesPortalsRoute> {
                        FilesAndPortalsScreen(
                            teacherProfile = teacherProfile,
                            savedFiles = savedFiles,
                            onSaveProfile = viewModel::saveProfile,
                            onOpenFormatBuilder = { item ->
                                viewModel.openFormatBuilder(item.id)
                                navController.navigate(FormatBuilderRoute(item.id))
                            },
                            onOpenFile = ::openFileSafely,
                            onShareFile = ::shareFileSafely,
                            onSaveFileToDownloads = viewModel::saveFileToDownloads,
                            onDeleteFile = viewModel::deleteSavedFile
                        )
                    }

                    composable<FormatBuilderRoute> { backStackEntry ->
                        val args = backStackEntry.toRoute<FormatBuilderRoute>()
                        LaunchedEffect(args.formatId) {
                            if (currentFormat?.id != args.formatId) {
                                viewModel.openFormatBuilder(args.formatId)
                            }
                        }
                        val activeFormat = currentFormat
                            ?: FormatsCatalog.findById(args.formatId)
                            ?: FormatsCatalog.allFormats.first()

                        FormatBuilderScreen(
                            format = activeFormat,
                            formData = formData,
                            previewDoc = previewDoc,
                            isExporting = isExporting,
                            onUpdateForm = viewModel::updateFormData,
                            onAutoFillProfile = viewModel::applySavedProfileToForm,
                            onExport = viewModel::exportCurrentFormat,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }

    lastExportedFile?.let { fileInfo ->
        ExportSuccessDialog(
            file = fileInfo,
            onOpen = {
                viewModel.dismissExportDialog()
                openFileSafely(fileInfo)
            },
            onShare = {
                viewModel.dismissExportDialog()
                shareFileSafely(fileInfo)
            },
            onSaveToDownloads = {
                viewModel.saveFileToDownloads(fileInfo)
            },
            onDismiss = viewModel::dismissExportDialog
        )
    }
}
