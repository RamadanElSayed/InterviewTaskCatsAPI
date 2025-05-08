package com.ramadan.readybackendproject.presentation.screens.uimodel

sealed class CatListIntent {
    data object LoadCatImages : CatListIntent()
    data object RefreshCatImages : CatListIntent()
}