package me.lfojacintho.hellofresh.deliverycheck.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "hf.client")
public record HelloFreshClientConfiguration(
    String baseUrl,
    String authToken
) {
}
