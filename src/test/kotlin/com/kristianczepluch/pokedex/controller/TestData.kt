package com.kristianczepluch.pokedex.controller

import com.kristianczepluch.pokedex.presentation.model.PokemonDto


val SCHIGGY_TEST = PokemonDto(
    99,
    "Schiggy",
    60,
    6.0,
    "Kristian"
)

val POKEMON_TEST_LIST = listOf(
    SCHIGGY_TEST,
    PokemonDto(
        100,
        "Glumanda",
        70,
        7.0,
        "Kristian"
    ),
    PokemonDto(
        101,
        "Bisasam",
        50,
        5.0,
        "Kristian"
    )
)