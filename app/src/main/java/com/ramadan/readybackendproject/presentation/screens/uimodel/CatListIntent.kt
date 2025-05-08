package com.ramadan.readybackendproject.presentation.screens.uimodel

/**
 * Defines user intents (actions) for the Cat List screen
 */
sealed class CatListIntent {
    /**
     * Intent to load cat images initially
     */
    data object LoadCatImages : CatListIntent()

    /**
     * Intent to refresh the cat images
     */
    data object RefreshCatImages : CatListIntent()
}