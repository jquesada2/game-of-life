import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import net.jquesada.gol.GameOfLifeCellGrid

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

    LazyColumn {
        items(golGrid.rowCount) { rowIndex ->
            LazyRow {
                items(golGrid.colCount) { colIndex ->
                    var alive by remember { mutableStateOf(golGrid.getCellAtRowAndColumn(rowIndex, colIndex)) }
                    Button(onClick = {
                        alive = golGrid.toggleCellAtRowAndColumn(rowIndex, colIndex)
                    }) {
                        Text(if(alive) "X" else " ")
                    }
                }
            }
        }
    }

//    MaterialTheme {
//        Button(onClick = {
//            text = Random.nextInt(1000, 9999).toString()
//        }) {
//            Text(text)
//        }
//    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
