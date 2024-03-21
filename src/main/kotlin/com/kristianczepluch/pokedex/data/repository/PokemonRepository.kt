package com.kristianczepluch.pokedex.data.repository

import com.kristianczepluch.pokedex.presentation.model.PokemonDto
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository

interface PokemonRepository: PagingAndSortingRepository<PokemonDto, Int>, CrudRepository<PokemonDto, Int>