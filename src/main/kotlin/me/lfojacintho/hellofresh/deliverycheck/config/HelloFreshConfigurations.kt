package me.lfojacintho.hellofresh.deliverycheck.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("hf.client")
data class HelloFreshClientConfiguration(
    val baseUrl: String,
    val authToken: String
)

@ConstructorBinding
@ConfigurationProperties(prefix = "hf.product")
data class HelloFreshProductConfiguration(
    val country: String?,
    val locale: String?,
    val sku: String?,
    val preference: String?,
    val servings: Int?,
    val subscription: String?,
    val postcode: String?,
    val deliveryOption: String?
)
