package com.example.a2048.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a2048.model.Cell
import com.example.a2048.model.GameEvents
import com.example.a2048.model.Matriz
import com.example.a2048.ui.theme.Colors
import com.example.a2048.ui.viewModel.GameViewModel

typealias ItemOffsetAnimatable = Animatable<DpOffset, AnimationVector2D>
typealias ScaleAnimatable = Animatable<Float, AnimationVector1D>


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onEvent: (GameEvents) -> Unit,
) {

    var offSetX by remember { mutableStateOf(0f) }
    var offSetY by remember { mutableStateOf(0f) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "2048",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                    )
                }
            )
        },
    ) {
        Surface(
            color = Colors.background,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()

                            offSetX += dragAmount.x
                            offSetY += dragAmount.y
                        },
                        onDragEnd = {
                            onEvent(GameEvents.Move(offSetX, offSetY))
                            offSetX = 0f
                            offSetY = 0f
                        }
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Score(
                    score = viewModel.currentScore,
                    highScore = viewModel.highScore,
                )
                Spacer(modifier = Modifier.height(20.dp))

                Board(matriz = viewModel.board)
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp)),
                    onClick = { viewModel.resetBoard() }
                ) {
                    Text(
                        text = "Reiniciar Juego",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Colors.buttonText,
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }


            }
        }
    }
}


@Composable
fun Board(matriz: Matriz) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Colors.board)
            .aspectRatio(1f)
            .padding(4.dp)
    ) {
        EmptyGrid(rows = matriz.rows, cols = matriz.cols) {
            CellSquare(cell = Cell.EMPTY, shouldRenderEmptyTile = true)
        }
        AnimatedGrid(matriz = matriz) {
            CellSquare(cell = it)
        }
    }
}

@Composable
fun ScoreSquare(text: String, score: Int) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Colors.board)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(255, 255, 255, 200)
        )

        Text(
            text = score.toString(),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Colors.title
        )
    }
}


@Composable
fun Score(
    score: Int, highScore: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ScoreSquare("Puntaje", score)
        Spacer(modifier = Modifier.width(15.dp))
        ScoreSquare("Mejor Puntaje", highScore)
    }
}


@Composable
fun EmptyGrid(
    modifier: Modifier = Modifier,
    rows: Int,
    cols: Int,
    itemContent: @Composable BoxScope.() -> Unit
) = BoxWithConstraints(modifier) {
    val itemSize = remember(rows, cols) {
        DpSize(maxWidth / cols, maxHeight / rows)
    }

    val gridOffsets = remember(rows, cols, itemSize) {
        (0 until rows).map { rowIndex ->
            (0 until cols).map { colIndex ->
                DpOffset(itemSize.width * colIndex, itemSize.height * rowIndex)
            }
        }
    }

    gridOffsets.forEach { row ->
        row.forEach { it ->
            Box(
                Modifier
                    .size(itemSize)
                    .offset(it.x, it.y)
            ) {
                itemContent()
            }
        }
    }
}


@Composable
fun CellSquare(cell: Cell, shouldRenderEmptyTile: Boolean = false) {
    if (!shouldRenderEmptyTile && cell.isEmpty) return
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(4.dp))
            .background(cell.tileColor)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = cell.displayText,
            fontSize = cell.fontSize,
            fontWeight = FontWeight.Bold,
            color = cell.fontColor
        )
    }
}

@SuppressLint("UnrememberedAnimatable")
@Composable
fun AnimatedGrid(
    modifier: Modifier = Modifier,
    matriz: Matriz,
    itemContent: @Composable BoxScope.(Cell) -> Unit
) = BoxWithConstraints(modifier) {
    val itemSize = remember(matriz.rows, matriz.cols) {
        DpSize(maxWidth / matriz.cols, maxHeight / matriz.rows)
    }

    val gridOffsets = remember(matriz.rows, matriz.cols, itemSize) {
        matriz.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, _ ->
                DpOffset(itemSize.width * colIndex, itemSize.height * rowIndex)
            }
        }
    }

    val oldOffsets = remember { mutableMapOf<Int, ItemOffsetAnimatable>() }
    val oldScales = remember { mutableMapOf<Int, ScaleAnimatable>() }
    matriz.iterateIndexed { rowIndex, colIndex, cell ->
        key(cell.id) {
            oldOffsets[cell.id] = oldOffsets[cell.id]
                ?: ItemOffsetAnimatable(gridOffsets[rowIndex][colIndex], DpOffset.VectorConverter)

            oldScales[cell.id] = oldScales[cell.id]
                ?: ScaleAnimatable(0f, Float.VectorConverter)
        }
    }

    matriz.iterateIndexed { rowIndex, colIndex, cell ->
        val oldOffset = oldOffsets.getValue(cell.id)
        val oldScale = oldScales.getValue(cell.id)

        LaunchedEffect(cell.id, rowIndex, colIndex) {
            val newOffset = gridOffsets[rowIndex][colIndex]
            oldOffset.animateTo(newOffset, tween(200))
            oldScale.animateTo(1f, tween(200, 50))
        }

        Box(
            Modifier
                .size(itemSize)
                .offset(oldOffset.value.x, oldOffset.value.y)
                .scale(oldScale.value)
        ) {
            itemContent(cell)
        }
    }
}

