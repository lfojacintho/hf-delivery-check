package me.lfojacintho.hellofresh.deliverycheck.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestConfiguration {

    private final HelloFreshClientConfiguration hfClientConfig;

    @Autowired
    public RestConfiguration(final HelloFreshClientConfiguration hfClientConfig) {
        this.hfClientConfig = hfClientConfig;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl(hfClientConfig.baseUrl())
            .defaultHeaders(this::configureDefaultHeaders)
            .build();
    }

    private void configureDefaultHeaders(final HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(hfClientConfig.authToken());
    }
}
