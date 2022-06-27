package net.jquesada.gol

import androidx.compose.runtime.*

data class GameOfLifeViewModel(val cellGrid: GameOfLifeCellGrid) {
    val currentGeneration = mutableStateOf(cellGrid.generations)
    val rowCount = mutableStateOf(cellGrid.rowCount)
    val colCount = mutableStateOf(cellGrid.colCount)
    val isSimulating = mutableStateOf(cellGrid.isSimulating)

    private var _cells : MutableList<Boolean> = mutableStateListOf()
    var cells : MutableList<Boolean> by mutableStateOf(_cells).also {
        _cells.addAll(cellGrid.cells)
    }

    fun refresh() {
        _cells.clear()
        _cells.addAll(cellGrid.cells)
    }
}