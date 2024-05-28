package com.example.a2048.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a2048.ui.theme.Colors

@Composable
fun GameScreen(){

    Surface(
        color = Colors.background,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()

                       // offsetX += dragAmount.x
                    //    offsetY += dragAmount.y
                    },
                    onDragEnd = {
                     //   onEvent(GameUiEvent.Push(offsetX, offsetY))
                      //  offsetX = 0f
                      //  offsetY = 0f
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

            HeaderView()

            Board()
        }
    }
}

@Composable
fun HeaderView(

) {
    Column(
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "2048",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Colors.title
            )

            Spacer(modifier = Modifier.weight(1f))

           // ScoreView("SCORE", score)
            Spacer(modifier = Modifier.width(4.dp))
          //  ScoreView("HIGH SCORE", highScore)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
               // ButtonView(btnText = "UNDO") { onUndoClick() }
                Spacer(modifier = Modifier.width(4.dp))
              //  ButtonView(btnText = "RESET") { onResetClick() }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun Board() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Colors.board)
            .aspectRatio(1f)
            .padding(4.dp)
    ) {
      //  EmptyGrid(rows = matrix.rows, cols = matrix.cols) {
        //    TileView(tile = Tile.EMPTY, shouldRenderEmptyTile = true)
        }
        //AnimatedGrid(matrix = matrix) {
          //  TileView(tile = it)
        }
  //  }
//}