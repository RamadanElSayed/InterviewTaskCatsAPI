package com.ramadan.readybackendproject.presentation.screens.uimodel

import com.ramadan.readybackendproject.presentation.model.CatImageUI


/**
 * Represents the UI state for the cat list screen
 */
data class CatListState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val catImages: List<CatImageUI> = emptyList(),
    val error: ErrorState? = null
)