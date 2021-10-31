package me.lfojacintho.hellofresh.deliverycheck.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "hf.product")
public record HelloFreshProductConfiguration(
    String country,
    String locale,
    String sku,
    String preference,
    Integer servings,
    String subscription,
    String postcode,
    String deliveryOption
) {
}
