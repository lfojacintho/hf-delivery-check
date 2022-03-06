package me.lfojacintho.hellofresh.deliverycheck.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
open class RestConfiguration(private val hfClientConfig: HelloFreshClientConfiguration) {

    @Bean
    open fun webClient(): WebClient = WebClient.builder()
            .baseUrl(hfClientConfig.baseUrl)
            .defaultHeaders(this::configureDefaultHeaders)
            .build()

    private fun configureDefaultHeaders(headers: HttpHeaders) {
        headers.contentType = MediaType.APPLICATION_JSON
        headers.setBearerAuth(hfClientConfig.authToken)
    }
}
