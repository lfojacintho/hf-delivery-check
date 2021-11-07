package me.lfojacintho.hellofresh.deliverycheck.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class WireMockTestExtension implements BeforeAllCallback, AfterAllCallback {

    private static final WireMockServer WIRE_MOCK_SERVER;

    static {
        WIRE_MOCK_SERVER = new WireMockServer(
            WireMockConfiguration.options()
                .usingFilesUnderClasspath("wiremock")
                .extensions(new ResponseTemplateTransformer(true))
                .dynamicPort()
        );
    }

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        WIRE_MOCK_SERVER.start();
        WIRE_MOCK_SERVER.stubFor(get(urlEqualTo("/gw/my-deliveries/menu?locale=de-DE&product-sku=DE-CBT1-2-3-0&servings=2&subscription=12&week=2021-W45"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withStatus(200)
                .withBody(IOUtils.toString(
                    getClass().getResourceAsStream("/wiremock/hellofresh/menu-response-200.json"),
                    StandardCharsets.UTF_8
                ))));

        WIRE_MOCK_SERVER.stubFor(get(urlEqualTo("/gw/recipes/recipes/61508a0dcc9f2a786e62b22f?country=DE&locale=de-DE"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withStatus(200)
                .withBody(IOUtils.toString(
                    getClass().getResourceAsStream("/wiremock/hellofresh/recipe-response-200.json"),
                    StandardCharsets.UTF_8
                ))));
    }

    @Override
    public void afterAll(final ExtensionContext context) throws Exception {
        WIRE_MOCK_SERVER.resetAll();
        WIRE_MOCK_SERVER.resetMappings();
        WIRE_MOCK_SERVER.stop();
    }

    public static WireMockServer getWireMockServer() {
        if (!WIRE_MOCK_SERVER.isRunning()) {
            throw new IllegalStateException("WireMock server is not running.");
        }

        return WIRE_MOCK_SERVER;
    }
}
