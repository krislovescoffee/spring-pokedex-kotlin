package com.kristianczepluch.pokedex.presentation.model

data class PokemonDto(
    val name: String,
    val heightInCm: Int,
    val weightInKg: Double,
    val abilities: List<String>
)
