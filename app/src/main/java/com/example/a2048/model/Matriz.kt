package com.example.a2048.model

class Matriz(
    val base: MutableList<MutableList<Cell>>
) : MutableList<MutableList<Cell>> by base {

    companion object {
        fun emptyMatriz(size: Int = 4): Matriz {
            return Matriz(MutableList(size) { MutableList(size) { Cell.EMPTY } })
        }
    }

    val rows: Int
        get() = size

    val cols: Int
        get() = first().size

    fun flipHorizontally() {
        this.forEach {
            it.reverse()
        }
    }

    fun transpose() {
        for (row in 0 until size) {
            for (col in row + 1 until size) {
                this[row][col] = this[col][row].also {
                    this[col][row] = this[row][col]
                }
            }
        }
    }

    inline fun iterateIndexed(block: (rowIndex: Int, colIndex: Int, cell: Cell) -> Unit) {
        this.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, cell ->
                block(rowIndex, colIndex, cell)
            }
        }
    }

    fun copy(): Matriz {
        return Matriz(
            MutableList(size) { row ->
                MutableList(size) { col ->
                    this[row][col]
                }
            }
        )
    }
}
