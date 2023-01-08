package org.example.webflux.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;


@Configuration
@Slf4j
public class WebfluxConfig {

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient
                .create()
                .tcpConfiguration(
                        tc -> tc.bootstrap(
                                b -> BootstrapHandlers.updateLogSupport(b, new CustomLogger(HttpClient.class))));

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader(CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON.toString())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(logResponse());
                })
                .build();
        return webClient;

    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Sms REST CLIENT REQUEST: **********");
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("********** Sms REST CLIENT RESPONSE");
            return Mono.just(clientResponse);
        });
    }

}
