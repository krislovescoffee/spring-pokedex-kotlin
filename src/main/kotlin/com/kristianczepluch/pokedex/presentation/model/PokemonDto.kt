package com.kristianczepluch.pokedex.presentation.model

import org.springframework.data.annotation.Id

data class PokemonDto(
    @Id
    val id: Int?,
    val name: String,
    val heightInCm: Int,
    val weightInKg: Double,
    val owner: String,
)
