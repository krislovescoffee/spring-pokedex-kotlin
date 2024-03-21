package com.kristianczepluch.pokedex.controller

import com.jayway.jsonpath.JsonPath
import com.kristianczepluch.pokedex.presentation.model.PokemonDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext
import java.net.URI


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
internal class PokemonControllerTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun `WHEN get pokemon is called with a valid id THEN 200 ok is returned`() {
        val response = restTemplate.getForEntity("/pokemon/100", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `WHEN get pokemon is called with a invalid id THEN 404 ok is returned`() {
        val response = restTemplate.getForEntity("/pokemon/2", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `WHEN get pokemon is called with a valid id THEN pokemon content is returned`() {
        val response = restTemplate.getForEntity("/pokemon/100", String::class.java)
        val responseBody = JsonPath.parse(response.body)

        val name = responseBody.read<String>("$.name")
        val heightInCm = responseBody.read<Int>("$.heightInCm")
        val weightInKg = responseBody.read<Double>("$.weightInKg")

        assertThat(name).isEqualTo("Glumanda")
        assertThat(heightInCm).isEqualTo(70)
        assertThat(weightInKg).isEqualTo(7.0)
    }

    @Test
    fun shouldCreateANewCashCard() {
        val newPokemon = PokemonDto(null, "Schiggy", 60, 6.0)
        val createResponse = restTemplate.postForEntity("/pokemon", newPokemon, Void::class.java)
        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.CREATED)

        val locationOfNewCashCard: URI? = createResponse.headers.location
        val getResponse: ResponseEntity<String> = restTemplate.getForEntity(locationOfNewCashCard, String::class.java)
        assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
    }
}