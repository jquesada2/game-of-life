import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import net.jquesada.gol.GameOfLifeCellGrid
import net.jquesada.gol.GameOfLifeViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun renderCellGrid(gridModel: GameOfLifeViewModel) {

    val liveCellColor = ButtonDefaults.buttonColors(backgroundColor = Color.Green, disabledBackgroundColor = Color.Transparent)
    val deadCellColor = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent, disabledBackgroundColor = Color.Transparent)

    LazyVerticalGrid(
        cells = GridCells.Fixed(gridModel.colCount.value),
        modifier = Modifier.fillMaxHeight().padding(Dp(7.5F))
    ) {
        gridModel.cells.forEachIndexed { idx, alive ->
            item {
                Button(
                    modifier = Modifier.width(Dp(1.0F)).aspectRatio(1.0F, true),
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

    // size control row
    var rowTextValue by remember { gridModel.rowCount }
    var colTextValue by remember { gridModel.colCount }


    // simple controls for manual tick, reset, and randomize
    Column(modifier = Modifier.fillMaxWidth(0.2F).defaultMinSize(minWidth = Dp(1.0F)).padding(Dp(7.5F))) {

        Text("Simulation Size (RxC)")
        Spacer(modifier = Modifier.height(Dp(4.0F)))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(value = rowTextValue.toString(), enabled = !simulating, modifier = Modifier.fillMaxWidth(0.45F), onValueChange = {
                if (it.isNotBlank() && it.all(Character::isDigit)) {
                    val newRowCount = it.toInt()
                    println("new row count: $newRowCount")
                    rowTextValue = newRowCount
                    gridModel.rowCount.value = newRowCount
                    gridModel.cellGrid.resize(newRowCount, colTextValue)
                    gridModel.refresh()
                }
            })
            Text(" x ")
            TextField(value = colTextValue.toString(), enabled = !simulating, modifier = Modifier.fillMaxWidth(), onValueChange = {
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

        // Clear and Randomize
        Spacer(modifier = Modifier.height(Dp(4.0F)))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
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
                modifier = Modifier.fillMaxWidth().padding(horizontal = Dp(4.0F)),
                onClick = {
                    gridModel.cellGrid.randomize()
                    currentGen = 0
                    gridModel.refresh()
                }) {
                    Text("Randomize")
                }
        }

        // Generation Info and Controls
        Spacer(modifier = Modifier.height(Dp(20.0F)))
        Text("Current Generation: $currentGen")
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    simulating = if (simulating) {
                        gridModel.cellGrid.stopSimulation()
                    } else {
                        gridModel.cellGrid.startSimulation { ++currentGen; gridModel.refresh() }
                    }
                }) {
                Text(if(simulating) "Pause" else "Run")
            }

            Button(
                enabled = !simulating,
                modifier = Modifier.padding(horizontal = Dp(4.0F)).fillMaxWidth(),
                onClick = {
                    gridModel.cellGrid.tick()
                    ++currentGen
                    gridModel.refresh()
                }) {
                    Text("Next Gen")
                }
        }

        Spacer(modifier = Modifier.height(Dp(20.0F)))
        var generationTime by remember { mutableStateOf(50.0F) }
        Text("Speed (ms/gen) [20-2000]: ${generationTime.toLong()}")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Slider(
                value = generationTime,
                valueRange = 20F..2000F,
                onValueChange = {
                    generationTime = it
                },
                onValueChangeFinished = {
                    gridModel.cellGrid.simulationSpeed = generationTime.toLong()
                },
                modifier = Modifier.padding(horizontal = Dp(4.0F))
            )
        }
    }
}

fun main() = application {
    val cellGrid = GameOfLifeCellGrid(18, 20)
    val viewModel = GameOfLifeViewModel(cellGrid)
    Window(
        title = "Game of Life",
        state = WindowState(width = Dp(1200.0F), height = Dp(900.0F)),
        onCloseRequest = ::exitApplication) {
        Row {
            renderCellGridControls(viewModel)
            Divider(color = Color.Transparent, modifier = Modifier.fillMaxHeight().width(Dp(4.0F)))
            renderCellGrid(viewModel)
        }
    }

    viewModel.refresh()
}
