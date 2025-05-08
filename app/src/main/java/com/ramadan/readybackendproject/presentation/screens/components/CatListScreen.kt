package com.ramadan.readybackendproject.presentation.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramadan.readybackendproject.presentation.screens.uimodel.CatListIntent
import com.ramadan.readybackendproject.presentation.screens.viewmodel.CatListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatListScreen(
    modifier: Modifier = Modifier,
    viewModel: CatListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        viewModel.processIntent(CatListIntent.LoadCatImages)
    }

    LaunchedEffect(state.isRefreshing) {
        if (state.isRefreshing) {
            pullToRefreshState.animateToThreshold()
        } else {
            pullToRefreshState.animateToHidden()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Random Cat Images",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading && state.catImages.isEmpty() -> {
                    LoadingView()
                }

                state.errorMessage != null && state.catImages.isEmpty() -> {
                    state.errorMessage?.let {
                        ErrorView(
                            errorMessage = state.errorMessage,
                            errorCode = state.errorCode,
                            onRetry = {
                                viewModel.processIntent(CatListIntent.RefreshCatImages)
                            }
                        )
                    }
                }

                else -> {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        PullToRefreshBox(
                            isRefreshing = state.isRefreshing,
                            onRefresh = { viewModel.processIntent(CatListIntent.RefreshCatImages) },
                            state = pullToRefreshState,
                            modifier = Modifier.fillMaxSize(),
                            indicator = {
                                PullToRefreshDefaults.Indicator(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    isRefreshing = state.isRefreshing,
                                    state = pullToRefreshState,
                                    containerColor = MaterialTheme.colorScheme.surface,
                                )
                            }
                        ) {
                            if (state.catImages.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        items = state.catImages,
                                        key = { it.id }
                                    ) { catImage ->
                                        CatImageItem(
                                            catImage = catImage
                                        )
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No cat images found.\nPull down to refresh!",
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }

                            if (state.isRefreshing && state.catImages.isNotEmpty()) {
                                LoadingView(isOverlay = true)
                            }
                        }
                    }
                }
            }
        }
    }
}