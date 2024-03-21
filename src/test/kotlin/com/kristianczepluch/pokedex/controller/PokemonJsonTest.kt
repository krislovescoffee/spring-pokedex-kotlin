package com.kristianczepluch.pokedex.controller

import com.kristianczepluch.pokedex.presentation.model.PokemonDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.io.IOException


@JsonTest
internal class PokemonJsonTest {

    @Autowired
    lateinit var json: JacksonTester<PokemonDto>

    @Test
    @Throws(IOException::class)
    fun pokemonSerializationTest() {
        val newPokemon = PokemonDto(1, "Schiggy", 60, 6.0)
        assertThat(json.write(newPokemon)).isStrictlyEqualToJson("expected.json")
        assertThat(json.write(newPokemon)).hasJsonPathNumberValue("@.id")
        assertThat(json.write(newPokemon)).extractingJsonPathNumberValue("@.id").isEqualTo(1)
        assertThat(json.write(newPokemon)).extractingJsonPathStringValue("@.name").isEqualTo("Schiggy")
        assertThat(json.write(newPokemon)).hasJsonPathNumberValue("@.heightInCm")
        assertThat(json.write(newPokemon)).extractingJsonPathNumberValue("@.heightInCm").isEqualTo(60)
        assertThat(json.write(newPokemon)).hasJsonPathNumberValue("@.weightInKg")
        assertThat(json.write(newPokemon)).extractingJsonPathNumberValue("@.weightInKg").isEqualTo(6.0)
    }

    @Test
    @Throws(IOException::class)
    fun cashCardDeserializationTest() {
        val expectedPokemon = PokemonDto(1, "Schiggy", 60, 6.0)
        val pokemonJson = """
           {
                "id" : 1,
                "name" : "Schiggy",
                "heightInCm" : 60,
                "weightInKg" : 6.0
            }
           """.trimIndent()
        assertThat(json.parse(pokemonJson))
            .isEqualTo(expectedPokemon)
        assertThat(json.parseObject(pokemonJson).id).isEqualTo(1)
        assertThat(json.parseObject(pokemonJson).name).isEqualTo("Schiggy")
        assertThat(json.parseObject(pokemonJson).heightInCm).isEqualTo(60)
        assertThat(json.parseObject(pokemonJson).weightInKg).isEqualTo(6.0)
    }
}