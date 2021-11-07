package me.lfojacintho.hellofresh.deliverycheck.util;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

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
        WIRE_MOCK_SERVER.stubFor(get(urlEqualTo(
            "/gw/my-deliveries/menu?locale=de-DE&product-sku=DE-CBT1-2-3-0&servings=2&subscription=12&week=2021-W45"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withStatus(200)
                .withBodyFile("/hellofresh/menu-response-200.json")
            ));

        WIRE_MOCK_SERVER.stubFor(get(urlMatching("/gw/recipes/recipes/([a-z0-9]+)\\?country=DE&locale=de-DE"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withStatus(200)
                .withBodyFile("/hellofresh/recipe-response-{{request.pathSegments.[3]}}.json")
            ));
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
