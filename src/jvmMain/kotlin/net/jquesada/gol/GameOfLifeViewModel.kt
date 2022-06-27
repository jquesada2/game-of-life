package net.jquesada.gol

import androidx.compose.runtime.*
import kotlinx.coroutines.*

data class GameOfLifeViewModel(val cellGrid: GameOfLifeCellGrid) {
    val rowCount = mutableStateOf(cellGrid.rowCount)
    val colCount = mutableStateOf(cellGrid.colCount)
    val simulationRunning = mutableStateOf(false)
    var simulationTask: Job? = null

    private var _cells : MutableList<Boolean> = mutableStateListOf()
    var cells : MutableList<Boolean> by mutableStateOf(_cells).also {
        _cells.addAll(cellGrid.cells)
    }

    fun refresh() {
        _cells.clear()
        _cells.addAll(cellGrid.cells)
    }

    fun startSimulation() {
        if (simulationRunning.value && simulationTask != null)
            return

        simulationRunning.value = true
        simulationTask = CoroutineScope(Dispatchers.Default).launch {
            while (simulationRunning.value) {
                cellGrid.tick()
                refresh()

                delay(500L)
            }
        }

        simulationTask?.start()
    }

    fun stopSimulation() {
        simulationRunning.value = false
        simulationTask?.cancel()
    }

}