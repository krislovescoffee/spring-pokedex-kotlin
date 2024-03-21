package com.kristianczepluch.pokedex.controller

import com.jayway.jsonpath.JsonPath
import net.minidev.json.JSONArray
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
internal class PokemonControllerTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun `WHEN get pokemon is called with a valid id THEN 200 ok is returned`() {
        val response = restTemplate
            .withBasicAuth("Kristian", "abc123")
            .getForEntity("/pokemon/100", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun `WHEN get pokemon is called with a invalid id THEN 404 ok is returned`() {
        val response = restTemplate
            .withBasicAuth("Kristian", "abc123")
            .getForEntity("/pokemon/1000", String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `WHEN get pokemon is called with a valid id THEN pokemon content is returned`() {
        val response = restTemplate
            .withBasicAuth("Kristian", "abc123")
            .getForEntity("/pokemon/100", String::class.java)
        val responseBody = JsonPath.parse(response.body)

        val name = responseBody.read<String>("$.name")
        val heightInCm = responseBody.read<Int>("$.heightInCm")
        val weightInKg = responseBody.read<Double>("$.weightInKg")

        assertThat(name).isEqualTo("Glumanda")
        assertThat(heightInCm).isEqualTo(70)
        assertThat(weightInKg).isEqualTo(7.0)
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    fun shouldCreateANewCashCard() {
        val createResponse = restTemplate
            .withBasicAuth("Kristian", "abc123")
            .postForEntity("/pokemon", SCHIGGY_TEST, Void::class.java)
        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.CREATED)

        val locationOfNewCashCard: URI? = createResponse.headers.location
        val getResponse: ResponseEntity<String> = restTemplate
            .withBasicAuth("Kristian", "abc123")
            .getForEntity(locationOfNewCashCard, String::class.java)
        assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun shouldReturnAllCashCardsWhenListIsRequested() {
        val response = restTemplate
            .withBasicAuth("Kristian", "abc123")
            .getForEntity("/pokemon", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val documentContext = JsonPath.parse(response.body)
        val cashCardCount = documentContext.read<Int>("$.length()")
        assertThat(cashCardCount).isEqualTo(3)

        val ids: JSONArray = documentContext.read("$..id")
        assertThat(ids).containsExactlyInAnyOrder(99, 100, 101)

        val names: JSONArray = documentContext.read("$..name")
        assertThat(names).containsExactlyInAnyOrder("Schiggy", "Bisasam", "Glumanda")
    }

    @Test
    fun shouldReturnAPageOfCashCards() {
        val response = restTemplate
            .withBasicAuth("Kristian", "abc123")
            .getForEntity("/pokemon?page=0&size=1", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val documentContext = JsonPath.parse(response.body)
        val page = documentContext.read<JSONArray>("$[*]")
        assertThat(page.size).isEqualTo(1)
    }

    @Test
    fun shouldReturnASortedPageOfCashCards() {
        val response = restTemplate
            .withBasicAuth("Kristian", "abc123")
            .getForEntity("/pokemon?page=0&size=1&sort=name,asc", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val documentContext = JsonPath.parse(response.body)
        val read = documentContext.read<JSONArray>("$[*]")
        assertThat(read.size).isEqualTo(1)

        val amount = documentContext.read<String>("$[0].name")
        assertThat(amount).isEqualTo("Bisasam")
    }

    @Test
    fun shouldReturnASortedPageOfCashCardsWithNoParametersAndUseDefaultValues() {
        val response = restTemplate
            .withBasicAuth("Kristian", "abc123")
            .getForEntity("/pokemon", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        val documentContext = JsonPath.parse(response.body)
        val page = documentContext.read<JSONArray>("$[*]")
        assertThat(page.size).isEqualTo(3)

        val amounts = documentContext.read<JSONArray>("$..name")
        assertThat(amounts).containsExactly("Bisasam", "Glumanda", "Schiggy")
    }

    @Test
    fun shouldNotReturnACashCardWhenUsingBadCredentials() {
        var response = restTemplate
            .withBasicAuth("BAD-USER", "abc123")
            .getForEntity("/pokemon", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)

        response = restTemplate
            .withBasicAuth("Kristian", "BAD-PASSWORD")
            .getForEntity("/pokemon", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun shouldRejectUsersWhoAreNotCardOwners() {
        val response = restTemplate
            .withBasicAuth("hank-owns-no-cards", "qrs456")
            .getForEntity("/pokemon/99", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun shouldNotAllowAccessToCashCardsTheyDoNotOwn() {
        val response = restTemplate
            .withBasicAuth("Kristian", "abc123")
            .getForEntity("/pokemon/102", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}