package me.lfojacintho.hellofresh.deliverycheck.util;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(WireMockTestExtension.class)
public abstract class AbstractIntegrationTest {
}
