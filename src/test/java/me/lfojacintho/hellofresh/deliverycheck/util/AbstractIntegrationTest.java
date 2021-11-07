package me.lfojacintho.hellofresh.deliverycheck.util;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static me.lfojacintho.hellofresh.deliverycheck.util.WireMockTestExtension.getWireMockServer;

@SpringBootTest
@ExtendWith(WireMockTestExtension.class)
public abstract class AbstractIntegrationTest {

    @DynamicPropertySource
    static void applicationPropertiesOverride(final DynamicPropertyRegistry registry) {
        registry.add("wiremock-port", getWireMockServer()::port);
    }
}
