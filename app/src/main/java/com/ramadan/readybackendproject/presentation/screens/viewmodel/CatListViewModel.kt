package com.ramadan.readybackendproject.presentation.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramadan.readybackendproject.domain.model.BaseResult
import com.ramadan.readybackendproject.domain.usecase.GetCatImagesUseCase
import com.ramadan.readybackendproject.domain.usecase.RefreshCatImagesUseCase
import com.ramadan.readybackendproject.presentation.mapper.CatImageDomainMapper
import com.ramadan.readybackendproject.presentation.screens.uimodel.CatListIntent
import com.ramadan.readybackendproject.presentation.screens.uimodel.CatListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatListViewModel @Inject constructor(
    private val getCatImagesUseCase: GetCatImagesUseCase,
    private val refreshCatImagesUseCase: RefreshCatImagesUseCase,
    private val mapper: CatImageDomainMapper
) : ViewModel() {

    private val _state = MutableStateFlow(CatListState())
    val state: StateFlow<CatListState> = _state

    init {
        processIntent(CatListIntent.LoadCatImages)
    }

    fun processIntent(intent: CatListIntent) {
        when (intent) {
            is CatListIntent.LoadCatImages -> loadCatImages()
            is CatListIntent.RefreshCatImages -> refreshCatImages()
        }
    }

    private fun loadCatImages() {
        viewModelScope.launch {
            // Show loading state
            _state.update {
                it.copy(
                    isLoading = true, errorMessage = null,
                    errorCode = null
                )
            }

            getCatImagesUseCase().collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        val uiModels = mapper.mapToPresentationList(result.data)
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                catImages = uiModels,
                                errorMessage = null,
                                errorCode = null
                            )
                        }
                    }

                    is BaseResult.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                errorMessage = null,
                                errorCode = null
                            )
                        }
                    }

                    is BaseResult.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = true, errorMessage = null,
                                errorCode = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun refreshCatImages() {
        viewModelScope.launch {
            // Show refreshing state
            _state.update {
                it.copy(
                    isRefreshing = true, errorMessage = null,
                    errorCode = null
                )
            }

            refreshCatImagesUseCase().collect { result ->
                when (result) {
                    is BaseResult.Success -> {
                        val newImages = mapper.mapToPresentationList(result.data)

                        _state.update { currentState ->
                            // Combine existing images with new ones
                            // val combinedImages = currentState.catImages + newImages

                            currentState.copy(
                                isLoading = false,
                                isRefreshing = false,
                                catImages = newImages,
                                errorMessage = null,
                                errorCode = null
                            )
                        }
                    }

                    is BaseResult.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                errorMessage = it.errorMessage,
                                errorCode = it.errorCode
                            )
                        }
                    }

                    is BaseResult.Loading -> {
                        _state.update {
                            it.copy(
                                isRefreshing = true,
                                errorMessage = null,
                                errorCode = null
                            )
                        }
                    }
                }
            }
        }
    }

}