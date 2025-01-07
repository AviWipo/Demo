package com.example.demo.screen.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.demo.R
import com.example.demo.network.model.DogResponse
import com.example.demo.screen.main.viewModel.MainViewModel
import com.example.demo.shared.utils.UiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainView() {

    val viewModel = koinViewModel<MainViewModel>()
    val state = viewModel.uiStateDog.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchRandomDogImage()
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state.value) {
            is UiState.Error -> {
                Text(
                    modifier = Modifier.testTag(stringResource(id = R.string.test_tag_main_view_error_text)),
                    text = (state.value as UiState.Error<*>).message
                )
            }

            UiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.testTag(
                    stringResource(
                        id = R.string.test_tag_main_view_progress
                    )
                )
            )

            is UiState.Success -> {
                (state.value as UiState.Success<DogResponse?>).data?.let {
                    Image(
                        modifier = Modifier.testTag(stringResource(id = R.string.test_tag_main_view_image)),
                        painter = rememberAsyncImagePainter(it.image),
                        contentDescription = "Random image of Dog",
                    )
                }
            }

            UiState.Idle -> {}
        }
        Button(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_8))
                .testTag(stringResource(id = R.string.test_tag_main_view_button)),
            shape = RectangleShape,
            onClick = { viewModel.fetchRandomDogImage() }) {
            Text(text = stringResource(id = R.string.main_view_button_title))
        }
    }
}