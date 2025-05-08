package com.ramadan.readybackendproject.presentation.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramadan.readybackendproject.R
import com.ramadan.readybackendproject.presentation.screens.uimodel.ErrorState

@Composable
fun ErrorView(
    errorState: ErrorState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val iconRes = when (errorState) {
            is ErrorState.NetworkError -> R.drawable.ic_launcher_background
            is ErrorState.NotFound -> R.drawable.ic_launcher_background
            is ErrorState.AccessDenied -> R.drawable.ic_launcher_background
            is ErrorState.ServiceUnavailable -> R.drawable.ic_launcher_background
            is ErrorState.EmptyResponse -> R.drawable.ic_launcher_background
            is ErrorState.ApiError -> R.drawable.ic_launcher_background
            is ErrorState.UnknownError -> R.drawable.ic_launcher_background
        }

        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = getErrorTitle(errorState),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = getErrorMessage(errorState),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (shouldShowRetry(errorState)) {
            Button(
                onClick = onRetry,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp)
            ) {
                Text("Try Again")
            }
        }
    }
}


private fun getErrorTitle(errorState: ErrorState): String {
    return when (errorState) {
        is ErrorState.NetworkError -> "No Internet Connection"
        is ErrorState.NotFound -> "Not Found"
        is ErrorState.AccessDenied -> "Access Denied"
        is ErrorState.ServiceUnavailable -> "Service Unavailable"
        is ErrorState.EmptyResponse -> "No Images Found"
        is ErrorState.ApiError -> "Server Error"
        is ErrorState.UnknownError -> "Something Went Wrong"
    }
}

private fun getErrorMessage(errorState: ErrorState): String {
    return when (errorState) {
        is ErrorState.NetworkError ->
            "Please check your network connection and try again."

        is ErrorState.NotFound ->
            "We couldn't find what you were looking for. It might have been moved or deleted."

        is ErrorState.AccessDenied ->
            "You don't have permission to access this resource. Please check your credentials."

        is ErrorState.ServiceUnavailable ->
            "The service is currently unavailable. Please try again later."

        is ErrorState.EmptyResponse ->
            "We couldn't find any cat images at the moment. Please try again later."

        is ErrorState.ApiError ->
            "We encountered an error (${errorState.code}) when connecting to the server: ${errorState.message}"

        is ErrorState.UnknownError ->
            errorState.message.ifEmpty { "An unexpected error occurred. Please try again later." }
    }
}


private fun shouldShowRetry(errorState: ErrorState): Boolean {
    // Show retry for all errors except EmptyResponse and AccessDenied
    return when (errorState) {
        is ErrorState.EmptyResponse,
        is ErrorState.AccessDenied -> false

        else -> true
    }
}
