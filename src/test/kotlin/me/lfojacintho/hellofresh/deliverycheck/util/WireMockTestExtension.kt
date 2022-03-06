package me.lfojacintho.hellofresh.deliverycheck.util

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class WireMockTestExtension : BeforeAllCallback, AfterAllCallback {
    private val wireMockServer = WireMockServer(
        WireMockConfiguration.options()
            .usingFilesUnderClasspath("wiremock")
            .extensions(ResponseTemplateTransformer(true))
            .port(9876)
    )

    override fun beforeAll(context: ExtensionContext) {
        wireMockServer.start()

        wireMockServer.stubFor(
            get(
                urlEqualTo(
                    "/gw/my-deliveries/menu?locale=de-DE&product-sku=DE-CBT1-2-3-0&servings=2&subscription=12&week=2021-W45"
                )
            )
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withStatus(200)
                        .withBodyFile("/hellofresh/menu-response-200.json")
                )
        )

        wireMockServer.stubFor(
            get(urlMatching("/gw/recipes/recipes/([a-z0-9]+)\\?locale=de-DE&country=DE"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withStatus(200)
                        .withBodyFile("/hellofresh/recipe-response-{{request.pathSegments.[3]}}.json")
                )
        )
    }

    override fun afterAll(context: ExtensionContext) {
        with(wireMockServer) {
            resetAll()
            resetMappings()
            stop()
        }
    }
}
