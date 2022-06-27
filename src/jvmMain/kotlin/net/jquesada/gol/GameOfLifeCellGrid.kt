package net.jquesada.gol

import kotlinx.coroutines.*
import kotlin.random.Random

class GameOfLifeCellGrid(rows: Int = 10, cols: Int = 10) {

    var rowCount: Int = rows
        private set
    var colCount: Int = cols
        private set
    var isSimulating = false
        private set
    var generations: Int = 0
        private set

    lateinit var cells: MutableList<Boolean>
    private lateinit var neighboringCellOffsets: Sequence<Int>

    var simulationTask: Job? = null
    var simulationSpeed: Long = 200L

    init {
        resize(rows, cols)
    }

    fun resize(newRowCount: Int, newColCount: Int) {
        rowCount = newRowCount
        colCount = newColCount
        val cellCount = rowCount * colCount
        cells = MutableList(cellCount) { Random.nextBoolean() }
        neighboringCellOffsets = sequenceOf(1, 1 + rowCount, rowCount, -1 + rowCount, -1, -1 - rowCount, -rowCount, 1 - rowCount)
    }

    fun countLiveNeighbors(idx: Int): Int {
        return neighboringCellOffsets
            .map { idx + it }
            .filter { it >= 0 && it < cells.size }
            .filter { cells[it] }
            .count()
    }

    fun tick() {
        val next = cells.mapIndexed { idx, isAlive ->
            val liveNeighbors = countLiveNeighbors(idx)
            when {
                isAlive && (liveNeighbors == 2 || liveNeighbors == 3) -> true
                !isAlive && liveNeighbors == 3 -> true
                else -> false
            }
        }

        next.forEachIndexed { idx, nextAlive ->
            cells[idx] = nextAlive
        }
        ++generations
    }

    fun killAll() {
        cells.forEachIndexed { idx, _ ->
            cells[idx] = false
        }
        generations = 0
    }

    fun startSimulation(postTickWork: () -> Unit): Boolean {
        if (isSimulating && simulationTask != null)
            return isSimulating

        isSimulating = true
        simulationTask = CoroutineScope(Dispatchers.Default).launch {
            while (isSimulating) {
                tick()
                postTickWork()
                delay(simulationSpeed)
            }
        }

        simulationTask?.start()

        return isSimulating
    }

    fun stopSimulation(): Boolean {
        isSimulating = false
        simulationTask?.cancel()
        simulationTask = null

        return isSimulating
    }

    fun randomize() {
        for (idx in 0 until cells.size) {
            cells[idx] = Random.nextBoolean()
        }
        generations = 0
    }
}