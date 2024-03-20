package com.kristianczepluch.pokedex.controller

import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CashCardApplicationTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun `WHEN get pokemon is called with a valid id THEN 200 ok is returned`() {
        val response = restTemplate.getForEntity("/pokemon/glumanda", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `WHEN get pokemon is called with a invalid id THEN 404 ok is returned`() {
        val response = restTemplate.getForEntity("/pokemon/schigggy", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `WHEN get pokemon is called with a valid id THEN pokemon content is returned`() {
        val response = restTemplate.getForEntity("/pokemon/glumanda", String::class.java)
        val responseBody = JsonPath.parse(response.body)

        val name = responseBody.read<String>("$.name")
        val heightInCm = responseBody.read<Int>("$.heightInCm")
        val weightInKg = responseBody.read<Double>("$.weightInKg")
        val abilities = responseBody.read<List<String>>("$.abilities")

        assertThat(name).isEqualTo("Glumanda")
        assertThat(heightInCm).isEqualTo(70)
        assertThat(weightInKg).isEqualTo(7.0)
        assertThat(abilities).isEqualTo(listOf("Glut", "Tackle"))
    }
}