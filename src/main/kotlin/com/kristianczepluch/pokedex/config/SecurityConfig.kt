package com.kristianczepluch.pokedex.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { request ->
                request
                    .requestMatchers("/pokemon/**")
                    .hasRole("CARD-OWNER")
            }
            .httpBasic(Customizer.withDefaults())
            .csrf { csrf -> csrf.disable() }
        return http.build()
    }

    @Bean
    fun testOnlyUsers(passwordEncoder: PasswordEncoder): UserDetailsService {
        val users: User.UserBuilder = User.builder()
        val sarah: UserDetails = users
            .username("Kristian")
            .password(passwordEncoder.encode("abc123"))
            .roles("CARD-OWNER")
            .build()

        val hankOwnsNoCards = users
            .username("hank-owns-no-cards")
            .password(passwordEncoder.encode("qrs456"))
            .roles("NON-OWNER")
            .build()

        val kumar = users
            .username("Denise")
            .password(passwordEncoder.encode("xyz789"))
            .roles("CARD-OWNER")
            .build()

        return InMemoryUserDetailsManager(sarah, hankOwnsNoCards, kumar)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}