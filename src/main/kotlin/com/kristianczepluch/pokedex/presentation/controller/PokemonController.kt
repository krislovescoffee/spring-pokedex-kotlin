package com.kristianczepluch.pokedex.presentation.controller

import com.kristianczepluch.pokedex.presentation.model.PokemonDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/pokemon")
class PokemonController {

    @GetMapping("/{name}")
    private fun findById(@PathVariable name: String): ResponseEntity<PokemonDto> {
        if (name == "glumanda") {
            val pokemon = PokemonDto(
                name = "Glumanda",
                heightInCm = 70,
                weightInKg = 7.0,
                abilities = listOf("Glut", "Tackle"),
            )
            return ResponseEntity.ok(pokemon)
        } else {
            return ResponseEntity.notFound().build()
        }
    }
}