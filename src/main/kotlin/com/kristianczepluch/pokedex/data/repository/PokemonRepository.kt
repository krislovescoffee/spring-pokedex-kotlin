package com.kristianczepluch.pokedex.data.repository

import com.kristianczepluch.pokedex.presentation.model.PokemonDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*


interface PokemonRepository : PagingAndSortingRepository<PokemonDto, Int>, CrudRepository<PokemonDto, Int> {
    fun findByIdAndOwner(id: Int, owner: String): Optional<PokemonDto>
    fun findByOwner(owner: String, pageRequest: PageRequest): Page<PokemonDto>
}