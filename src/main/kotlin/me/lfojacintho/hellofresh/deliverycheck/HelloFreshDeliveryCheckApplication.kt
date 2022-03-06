package me.lfojacintho.hellofresh.deliverycheck

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("me.lfojacintho.hellofresh.deliverycheck")
open class HellofreshDeliveryCheckApplication

fun main(args: Array<String>) {
    runApplication<HellofreshDeliveryCheckApplication>(*args)
}

