package com.example.a2048.model

sealed interface GameEvents {

    data object Reset : GameEvents

    data class Move(val offsetX: Float, val offsetY: Float) : GameEvents
}