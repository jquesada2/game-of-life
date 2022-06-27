package net.jquesada.gol

import kotlin.random.Random

class GameOfLifeCellGrid(rows: Int = 10, cols: Int = 10) {

    var rowCount: Int = rows
        private set
    var colCount: Int = cols
        private set

    lateinit var cells: MutableList<Boolean>
    private lateinit var neighboringCellOffsets: Sequence<Int>

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

    fun setCell(row: Int, col: Int, newValue: Boolean) {
        cells[getLinearIndex(row, col)] = newValue
    }

    fun getCellAtRowAndColumn(row: Int, col: Int): Boolean {
        val idx = row * colCount + col
        return cells[getLinearIndex(row, col)]
    }

    fun toggleCellAtRowAndColumn(row: Int, col: Int): Boolean {
        val idx = getLinearIndex(row, col)
        cells[idx] = !cells[idx]
        return cells[idx]
    }

    inline fun getLinearIndex(row: Int, col: Int): Int {
        return row * colCount + col
    }

    fun countLiveNeighbors(row: Int, col: Int): Int {
        val idx = getLinearIndex(row, col)
        return countLiveNeighbors(idx)
    }

    fun countLiveNeighbors(idx: Int): Int {
        return neighboringCellOffsets
            .map { idx + it }
            .filter { it >= 0 && it < cells.size }
            .filter { cells[it] }
            .count()
    }

    fun tick() {
        println("--> tick")
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
    }

    fun killAll() {
        cells.forEachIndexed { idx, _ ->
            cells[idx] = false
        }
    }
}