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

    @Autowired
    lateinit var jsonList: JacksonTester<Array<PokemonDto>>

    @Test
    @Throws(IOException::class)
    fun pokemonSerializationTest() {
        assertThat(json.write(SCHIGGY_TEST)).isStrictlyEqualToJson("single.json")
        assertThat(json.write(SCHIGGY_TEST)).hasJsonPathNumberValue("@.id")
        assertThat(json.write(SCHIGGY_TEST)).extractingJsonPathNumberValue("@.id").isEqualTo(SCHIGGY_TEST.id)
        assertThat(json.write(SCHIGGY_TEST)).extractingJsonPathStringValue("@.name").isEqualTo(SCHIGGY_TEST.name)
        assertThat(json.write(SCHIGGY_TEST)).hasJsonPathNumberValue("@.heightInCm")
        assertThat(json.write(SCHIGGY_TEST)).extractingJsonPathNumberValue("@.heightInCm")
            .isEqualTo(SCHIGGY_TEST.heightInCm)
        assertThat(json.write(SCHIGGY_TEST)).hasJsonPathNumberValue("@.weightInKg")
        assertThat(json.write(SCHIGGY_TEST)).extractingJsonPathNumberValue("@.weightInKg")
            .isEqualTo(SCHIGGY_TEST.weightInKg)
        assertThat(json.write(SCHIGGY_TEST)).hasJsonPathStringValue("@.owner")
        assertThat(json.write(SCHIGGY_TEST)).extractingJsonPathStringValue("@.owner")
            .isEqualTo(SCHIGGY_TEST.owner)
    }

    @Test
    @Throws(IOException::class)
    fun cashCardDeserializationTest() {
        val pokemonJson = """
           {
                "id" : 99,
                "name" : "Schiggy",
                "heightInCm" : 60,
                "weightInKg" : 6.0,
                "owner" : "Kristian"
            }
           """.trimIndent()
        assertThat(json.parse(pokemonJson))
            .isEqualTo(SCHIGGY_TEST)
        assertThat(json.parseObject(pokemonJson).id).isEqualTo(99)
        assertThat(json.parseObject(pokemonJson).name).isEqualTo("Schiggy")
        assertThat(json.parseObject(pokemonJson).heightInCm).isEqualTo(60)
        assertThat(json.parseObject(pokemonJson).weightInKg).isEqualTo(6.0)
        assertThat(json.parseObject(pokemonJson).owner).isEqualTo("Kristian")
    }

    @Test
    @Throws(IOException::class)
    fun cashCardListSerializationTest() {
        assertThat(jsonList.write(POKEMON_TEST_LIST.toTypedArray())).isStrictlyEqualToJson("list.json")
    }

    @Test
    @Throws(IOException::class)
    fun cashCardListDeserializationTest() {
        val expected = """
        [
          {
            "id" : 99,
            "name" : "Schiggy",
            "heightInCm" : 60,
            "weightInKg" : 6.0,
            "owner" : "Kristian"
          },
          {
            "id" : 100,
            "name" : "Glumanda",
            "heightInCm" : 70,
            "weightInKg" : 7.0,
            "owner" : "Kristian"
          },
          {
            "id" : 101,
            "name" : "Bisasam",
            "heightInCm" : 50,
            "weightInKg" : 5.0,
            "owner" : "Kristian"
          }
        ]
         """.trimIndent()
        assertThat(jsonList.parse(expected)).isEqualTo(POKEMON_TEST_LIST.toTypedArray())
    }
}