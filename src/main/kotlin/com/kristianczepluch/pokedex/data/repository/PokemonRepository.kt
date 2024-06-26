package com.kristianczepluch.pokedex.data.repository

import com.kristianczepluch.pokedex.presentation.model.PokemonDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository


interface PokemonRepository : PagingAndSortingRepository<PokemonDto, Int>, CrudRepository<PokemonDto, Int> {
    fun findByIdAndOwner(id: Int, owner: String): PokemonDto?
    fun findByOwner(owner: String, pageRequest: PageRequest): Page<PokemonDto>
    fun existsByIdAndOwner(id: Int, owner: String): Boolean
}