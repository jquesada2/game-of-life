import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import net.jquesada.gol.GameOfLifeCellGrid
import net.jquesada.gol.GameOfLifeViewModel

@Composable
@Preview
fun App() {
    val golGrid = GameOfLifeCellGrid(3, 5)
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun renderCellGrid(gridModel: GameOfLifeViewModel) {

    val liveCellColor = ButtonDefaults.buttonColors(backgroundColor = Color.Green, disabledBackgroundColor = Color.Transparent)
    val deadCellColor = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent, disabledBackgroundColor = Color.Transparent)

    LazyVerticalGrid(cells = GridCells.Fixed(gridModel.colCount.value)) {
        gridModel.cells.forEachIndexed { idx, alive ->
            item {
                Button(
                    onClick = {
                        gridModel.cellGrid.cells[idx] = !alive
                        gridModel.cells[idx] = !alive
                    },
                    colors = if (alive) liveCellColor else deadCellColor
                ) { }
            }
        }
    }
}

@Composable
fun renderCellGridControls(gridModel: GameOfLifeViewModel) {

    val simulating by remember { gridModel.simulationRunning }

    // simple controls for tick and reset
    Row {
        Button(
            enabled = !simulating,
            onClick = {
            gridModel.cellGrid.tick()
            gridModel.refresh()
        }) {
            Text("Tick Generation")
        }

        Button(onClick = {
            gridModel.stopSimulation()
            gridModel.cellGrid.killAll()
            gridModel.refresh()
        }) {
            Text("Clear")
        }
    }

    // simulation controls
    Row {
        Button(onClick = {
            println("simulate button clicked")
            if (simulating) {
                println("is simulating: stopping")
                gridModel.stopSimulation()
            } else {
                println("is stopped: starting simulation")
                gridModel.startSimulation()
            }
        }) {
            Text(if(simulating) "Stop Simulation" else "Start Simulation")
        }
    }

    // grid size
    Row {
        var rowTextValue = remember { gridModel.rowCount }
        var colTextValue = remember { gridModel.colCount }

        Text("#Rows: ")
        TextField(value = rowTextValue.value.toString(), onValueChange = {
            if (it.isNotBlank() && it.all(Character::isDigit)) {
                val newRowCount = it.toInt()
                println("new row count: $newRowCount")
                rowTextValue.value = newRowCount
                gridModel.rowCount.value = newRowCount
                gridModel.cellGrid.resize(newRowCount, colTextValue.value)
                gridModel.refresh()
            }
        })

        Text("#Cols: ")
        TextField(value = colTextValue.value.toString(), onValueChange = {
            if (it.isNotBlank() && it.all(Character::isDigit)) {
                val newColCount = it.toInt()
                println("new col count: $newColCount")
                colTextValue.value = newColCount
                gridModel.colCount.value = newColCount
                gridModel.cellGrid.resize(rowTextValue.value, newColCount)
                gridModel.refresh()
            }
        })
    }
}

fun main() = application {
    val cellGrid = GameOfLifeCellGrid(10, 10)
    val viewModel = GameOfLifeViewModel(cellGrid)
    Window(onCloseRequest = ::exitApplication) {
        Column {
            renderCellGridControls(viewModel)
            renderCellGrid(viewModel)
        }
    }

    viewModel.refresh()
}
