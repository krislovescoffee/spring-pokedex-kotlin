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
import java.security.Principal
import kotlin.jvm.optionals.getOrNull


@RestController
@RequestMapping("/pokemon")
class PokemonController(
    private val repository: PokemonRepository,
) {

    @GetMapping("/{id}")
    private fun findById(@PathVariable id: Int, principal: Principal): ResponseEntity<PokemonDto> {
        val pokemon = repository.findByIdAndOwner(id, principal.name)
        return pokemon?.let { pokemonDto ->
            ResponseEntity.ok(pokemonDto)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    private fun create(
        @RequestBody pokemonDto: PokemonDto,
        ucb: UriComponentsBuilder,
        principal: Principal,
    ): ResponseEntity<Void> {
        val updatedPokemonDto = pokemonDto.copy(owner = principal.name)
        val savedPokemon = repository.save(updatedPokemonDto)
        val locationOfNewCashCard: URI = ucb
            .path("pokemon/{id}")
            .buildAndExpand(savedPokemon.id)
            .toUri()
        return ResponseEntity.created(locationOfNewCashCard).build()
    }

    @GetMapping
    private fun findAll(pageable: Pageable, principal: Principal): ResponseEntity<List<PokemonDto>> {
        val page: Page<PokemonDto> = repository.findByOwner(
            principal.name,
            PageRequest.of(
                pageable.pageNumber,
                pageable.pageSize,
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
            )
        )
        return ResponseEntity.ok(page.content)
    }

    @PutMapping("/{requestedId}")
    private fun put(
        @PathVariable requestedId: Int,
        @RequestBody pokemonDto: PokemonDto,
        principal: Principal
    ): ResponseEntity<Void> {
        val updatedDto = repository.findByIdAndOwner(requestedId, principal.name)
        updatedDto?.id?.let { id ->
            val newPokemonDto = PokemonDto(
                id,
                pokemonDto.name,
                pokemonDto.heightInCm,
                pokemonDto.weightInKg,
                principal.name,
            )
            repository.save(newPokemonDto)
            return ResponseEntity.noContent().build()
        } ?: return ResponseEntity.notFound().build()
    }

}