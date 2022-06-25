package net.jquesada.gol

import kotlin.random.Random

class GameOfLifeCellGrid(rows: Int, cols: Int) {

    var rowCount: Int
        private set
    var colCount: Int
        private set

    lateinit var cells: Array<Boolean>

    init {
        rowCount = rows
        colCount = cols
        resize(rows, cols)
    }

    fun resize(newRowCount: Int, newColCount: Int) {
        rowCount = newRowCount
        colCount = newColCount
        val cellCount = rowCount * colCount
        cells = Array(cellCount) { Random.nextBoolean() }
    }

    fun setCell(row: Int, col: Int, newValue: Boolean) {
        cells[getLinearIndex(row, col)] = newValue
    }

    fun getCell(row: Int, col: Int): Boolean {
        val idx = row * colCount + col
        return cells[getLinearIndex(row, col)]
    }

    fun toggleCell(row: Int, col: Int): Boolean {
        val idx = getLinearIndex(row, col)
        cells[idx] = !cells[idx]
        return cells[idx]
    }

    inline fun getLinearIndex(row: Int, col: Int): Int {
        return row * colCount + col
    }
}