import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import net.jquesada.gol.GameOfLifeCellGrid
import net.jquesada.gol.GameOfLifeViewModel

//object Defaults {
//    val LiveCellButtonColor = ButtonDefaults.buttonColors(backgroundColor = Color.Green, disabledBackgroundColor = Color.Transparent)
//    val DeadCellButtonColor = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent, disabledBackgroundColor = Color.Transparent)
//}

@Composable
@Preview
fun App() {
    val golGrid = GameOfLifeCellGrid(3, 5)

//    LazyRow {
//        Button(onClick = {
//            golGrid.advanceGeneration()
//        }) {
//            Text("Next Generation")
//        }
//    }



//    MaterialTheme {
//        Button(onClick = {
//            text = Random.nextInt(1000, 9999).toString()
//        }) {
//            Text(text)
//        }
//    }
}

//@Composable
//fun renderCellGrid(grid: GameOfLifeCellGrid) {
//    var cellStates = remember { mutableStateListOf(grid.cells) }
//    LazyColumn {
//        item {
//            Button(onClick = {
////                println(cellStates)
//                grid.tick()
////                grid.cells.forEachIndexed { idx, next -> cellStates[idx] = next }
////                println(grid.cells)
//            }) {
//                Text("Tick Generation")
//            }
//        }
//
//        items(grid.rowCount) { rowIndex ->
//            LazyRow {
//                items(grid.colCount) { colIndex ->
//                    val linearIndex = grid.getLinearIndex(rowIndex, colIndex)
//                    var alive = cellStates[linearIndex]
//                    ali
//                    Button(onClick = {
//                        alive = !alive
//                        grid.cells[linearIndex] = alive
////                        alive = grid.cells[linearIndex]
//                    }) {
//                        Text(if(alive) "X" else " ")
//                    }
//                }
//            }
//        }
//    }
//}

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
    Row {
        Button(onClick = {
            gridModel.cellGrid.tick()
            gridModel.refresh()
        }) {
            Text("Tick Generation")
        }
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
