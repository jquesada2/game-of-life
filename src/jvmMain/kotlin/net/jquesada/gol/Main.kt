import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import net.jquesada.gol.GameOfLifeCellGrid
import net.jquesada.gol.GameOfLifeViewModel

object Data {
    val SimSpeeds = linkedMapOf(
        "Model T" to 2000L,
        "Pinto" to 1000L,
        "Focus" to 500L,
        "Ride the Lightning" to 200L,
        "Once this thing hits 88 mph..." to 10L
    )
}

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

    var simulating by remember { gridModel.isSimulating }
    var currentGen by remember { mutableStateOf(gridModel.cellGrid.generations) }

    // simple controls for manual tick, reset, and randomize
    Row {
        Button(
            enabled = !simulating,
            onClick = {
            gridModel.cellGrid.tick()
            ++currentGen
            gridModel.refresh()
        }) {
            Text("Next Generation")
        }

        Button(
            enabled = !simulating,
            onClick = {
            gridModel.cellGrid.killAll()
            currentGen = 0
            gridModel.refresh()
        }) {
            Text("Clear")
        }

        Button(
            enabled = !simulating,
            onClick = {
            gridModel.cellGrid.randomize()
            currentGen = 0
            gridModel.refresh()
        }) {
            Text("Randomize")
        }

        Text("Current Generation: $currentGen")
    }

    // simulation controls
    Row {
        Button(onClick = {
//            println("simulate button clicked")
            simulating = if (simulating) {
//                println("is simulating: stopping")
                gridModel.cellGrid.stopSimulation()
            } else {
//                println("is stopped: starting simulation")
                gridModel.cellGrid.startSimulation { ++currentGen; gridModel.refresh() }
            }
        }) {
            Text(if(simulating) "Stop Simulation" else "Start Simulation")
        }

        Text("Simulation Speed: ")

        var expanded by remember { mutableStateOf(false) }
        var selectedSimSpeedKey by remember { mutableStateOf("Ride The Lightning") }
        Box(modifier = Modifier.wrapContentSize(Alignment.TopStart).background(Color.Transparent)) {
            Text(selectedSimSpeedKey, modifier = Modifier.fillMaxWidth().clickable(onClick = { expanded = true }).background(
                Color.LightGray))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth().background(Color.White)
            ) {
                Data.SimSpeeds.entries.forEachIndexed { index, simspeed ->
                    DropdownMenuItem(onClick = {
                        selectedSimSpeedKey = simspeed.key
                        gridModel.cellGrid.simulationSpeed = simspeed.value
                        expanded = false
                    }) {
                        Text(text = simspeed.key)
                    }
                }
            }
        }
    }

    // grid size controls
    Row {
        var rowTextValue by remember { gridModel.rowCount }
        var colTextValue by remember { gridModel.colCount }

        Text("#Rows: ")
        TextField(value = rowTextValue.toString(), enabled = !simulating, onValueChange = {
            if (it.isNotBlank() && it.all(Character::isDigit)) {
                val newRowCount = it.toInt()
                println("new row count: $newRowCount")
                rowTextValue = newRowCount
                gridModel.rowCount.value = newRowCount
                gridModel.cellGrid.resize(newRowCount, colTextValue)
                gridModel.refresh()
            }
        })

        Text("#Cols: ")
        TextField(value = colTextValue.toString(), enabled = !simulating, onValueChange = {
            if (it.isNotBlank() && it.all(Character::isDigit)) {
                val newColCount = it.toInt()
                println("new col count: $newColCount")
                colTextValue = newColCount
                gridModel.colCount.value = newColCount
                gridModel.cellGrid.resize(rowTextValue, newColCount)
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
