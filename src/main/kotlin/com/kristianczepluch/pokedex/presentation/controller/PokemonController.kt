package com.kristianczepluch.pokedex.presentation.controller

import com.kristianczepluch.pokedex.data.repository.PokemonRepository
import com.kristianczepluch.pokedex.presentation.model.PokemonDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import kotlin.jvm.optionals.getOrNull


@RestController
@RequestMapping("/pokemon")
class PokemonController(
    private val repository: PokemonRepository,
) {

    @GetMapping("/{id}")
    private fun findById(@PathVariable id: Int): ResponseEntity<PokemonDto> {
        val pokemon = repository.findById(id).getOrNull()
        return pokemon?.let { pokemonDto ->
            ResponseEntity.ok(pokemonDto)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    private fun create(
        @RequestBody pokemonDto: PokemonDto,
        ucb: UriComponentsBuilder
    ): ResponseEntity<Void> {
        val savedPokemon = repository.save(pokemonDto)
        val locationOfNewCashCard: URI = ucb
            .path("pokemon/{id}")
            .buildAndExpand(savedPokemon.id)
            .toUri()
        return ResponseEntity.created(locationOfNewCashCard).build()
    }

    @GetMapping
    private fun findAll(pageable: Pageable): ResponseEntity<List<PokemonDto>> {
        val page: Page<PokemonDto> = repository.findAll(
            PageRequest.of(
                pageable.pageNumber,
                pageable.pageSize,
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
            )
        )
        return ResponseEntity.ok(page.content)
    }

}