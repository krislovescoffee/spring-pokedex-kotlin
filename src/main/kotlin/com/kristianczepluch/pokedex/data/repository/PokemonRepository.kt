package com.kristianczepluch.pokedex.data.repository

import com.kristianczepluch.pokedex.presentation.model.PokemonDto
import org.springframework.data.repository.CrudRepository

interface PokemonRepository: CrudRepository<PokemonDto, Int>