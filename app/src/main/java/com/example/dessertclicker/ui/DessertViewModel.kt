package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {
    private val desserts: List<Dessert> = Datasource.dessertList
    private val _uiState = MutableStateFlow(
        DessertUiState(
            revenue = 0,
            dessertsSold = 0,
            currentDessertPrice = desserts.first().price,
            currentDessertImageId = desserts.first().imageId
        )
    )
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    fun onDessertClicked() {
        _uiState.update { currentState ->
            val newDessertsSold = currentState.dessertsSold + 1
            val newRevenue =
                currentState.revenue + currentState.currentDessertPrice
            val dessertToShow = determineDessertToShow(
                desserts = desserts,
                dessertsSold = newDessertsSold
            )

            currentState.copy(
                revenue = newRevenue,
                dessertsSold = newDessertsSold,
                currentDessertPrice = dessertToShow.price,
                currentDessertImageId = dessertToShow.imageId
            )
        }
    }

    private fun determineDessertToShow(
        desserts: List<Dessert>,
        dessertsSold: Int
    ): Dessert {
        var dessertToShow = desserts.first()

        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                break
            }
        }

        return dessertToShow
    }
}