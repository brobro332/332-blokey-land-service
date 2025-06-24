package xyz.samsami.blokey_land.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${authentication.server.url}")
    private String authenticationServerUrl;

    @Bean
    public WebClient authenticationWebClient() {
        return WebClient.builder()
            .baseUrl(authenticationServerUrl)
            .build();
    }
}