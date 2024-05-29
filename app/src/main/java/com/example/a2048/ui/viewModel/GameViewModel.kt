package com.example.a2048.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.a2048.model.Cell
import com.example.a2048.model.GameEvents
import com.example.a2048.model.Matriz
import com.example.a2048.model.Movement
import kotlin.math.abs

class GameViewModel : ViewModel() {
    var currentScore: Int by mutableIntStateOf(0);
    var highScore: Int by mutableIntStateOf(0);
    var board: Matriz by mutableStateOf(Matriz.emptyMatriz());
    var previousBoard: Matriz by mutableStateOf(Matriz.emptyMatriz());

    private var moveScore = 0
    private var anyMoved = false
    private var anyCombined = false

    init {
        resetBoard()
    }

    fun onEvent(events: GameEvents) {
        when (events) {
            is GameEvents.Move -> _move(events.offsetX, events.offsetY)
            GameEvents.Reset -> resetBoard()
        }
    }

    fun resetBoard() {
        board = Matriz.emptyMatriz()
        repeat(2) { addTile() }
        previousBoard = board.copy()
        currentScore = 0
    }

    fun move(dir: Movement) {
        initNewMove()
        val tempBoard = board.copy()
        when (dir) {
            Movement.Left -> pushLeft()
            Movement.Right -> pushRight()
            Movement.Up -> pushUp()
            Movement.Down -> pushDown()
        }

        updateScoreBy(moveScore)
        if (anyMoved || anyCombined) {
            addTile()
            previousBoard = tempBoard
        }
    }

    private fun addTile() {
        val (row, col) = getEmptyPositions().random()
        board[row][col] = Cell.twoOrFour()
    }

    private fun getEmptyPositions(): List<Pair<Int, Int>> {
        return board.flatMapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, tile ->
                if (tile.isEmpty) Pair(rowIndex, colIndex) else null
            }
        }
    }

    private fun updateScoreBy(points: Int) {
        currentScore += points
        if (currentScore > highScore) {
            highScore = currentScore
        }
    }

    private fun initNewMove() {
        anyMoved = false
        anyCombined = false
        moveScore = 0
    }

    private fun pushLeft() {
        slide()
        combine()
        slide()
    }

    private fun slide() {
        board.forEach { row ->
            val newRow = row.filter { !it.isEmpty } + row.filter { it.isEmpty }
            newRow.zip(row).forEach {
                if (it.first.value != it.second.value) anyMoved = true
            }
            for (col in 0 until row.size) {
                row[col] = newRow[col]
            }
        }
    }

    private fun combine() {
        for (row in board) {
            for (col in 0 until row.size - 1) {
                if (Cell.canCombine(row[col], row[col + 1])) {
                    row[col] = Cell.combine(row[col], row[col + 1])
                    row[col + 1] = Cell.EMPTY
                    moveScore += row[col].value
                    anyCombined = true
                }
            }
        }
    }

    private fun pushRight() {
        board.flipHorizontally()
        pushLeft()
        board.flipHorizontally()
    }

    private fun pushUp() {
        board.transpose()
        pushLeft()
        board.transpose()
    }

    private fun pushDown() {
        board.transpose()
        board.flipHorizontally()
        pushLeft()
        board.flipHorizontally()
        board.transpose()
    }

    private fun _move(offSetX: Float, offSetY: Float) {
        if (abs(offSetX) > abs(offSetY)) {
            when {
                offSetX > 0 -> move(Movement.Right)
                offSetX < 0 -> move(Movement.Left)
            }
        } else {
            when {
                offSetY > 0 -> move(Movement.Down)
                offSetY < 0 -> move(Movement.Up)
            }
        }
    }

}
