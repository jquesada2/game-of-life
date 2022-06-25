import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
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
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {
    val golGrid = GameOfLifeCellGrid(3, 5)

    LazyColumn {
        items(golGrid.rowCount) { rowIndex ->
            LazyRow {
                items(golGrid.colCount) { colIndex ->
                    var text by remember { mutableStateOf(golGrid.getCell(rowIndex, colIndex)) }
                    Button(onClick = {
                        text = golGrid.toggleCell(rowIndex, colIndex)
                        println(golGrid.cells.joinToString(","))
                        println("row[$rowIndex] col[$colIndex]")
                    }) {
                        Text(if(text) "X" else " ")
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
