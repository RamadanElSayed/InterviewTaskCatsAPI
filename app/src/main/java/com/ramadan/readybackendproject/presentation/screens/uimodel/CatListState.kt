package com.ramadan.readybackendproject.presentation.screens.uimodel

import com.ramadan.readybackendproject.presentation.model.CatImageUI

data class CatListState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val catImages: List<CatImageUI> = emptyList(),
    val error: ErrorState? = null
)