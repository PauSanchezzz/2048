package com.example.a2048.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import com.example.a2048.ui.theme.Colors
import com.example.a2048.ui.theme.FontResponsiveSize
import java.lang.RuntimeException
import kotlin.random.Random
import kotlin.random.nextInt

class Cell(
    val value: Int,
    val id: Int = idCounter++
) {
    companion object {

        private var idCounter = 0

        val EMPTY = Cell(0, -1)

        fun twoOrFour() = if (Random.nextInt(1..10) <= 9) Cell(2) else Cell(4)

        fun canCombine(a: Cell, b: Cell): Boolean {
            if (a.value == 0 || b.value == 0) return false
            return a.value == b.value
        }

        fun combine(a: Cell, b: Cell): Cell {
            if (canCombine(a, b)) return Cell(a.value * 2, b.id)
            else throw RuntimeException("Tiles ${a.value} and ${b.value} cannot be combined")
        }
    }

    val isEmpty: Boolean
        get() = value == 0

    val displayText: String
        get() = if (isEmpty) "" else value.toString()

    val fontColor: Color
        get() = if (value <= 8) Colors.title else Colors.background

    val fontSize: TextUnit
        get() = FontResponsiveSize.NumberFontSize[value] ?: FontResponsiveSize.SingleDigitTileFontSize

    val tileColor: Color
        get() = Colors.numberColors[value] ?: Colors.title


}