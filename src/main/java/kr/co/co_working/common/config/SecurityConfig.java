package kr.co.co_working.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfigurationSource corsConfig;
    private static final List<Endpoint> AUTH_IGNORE_LIST = new ArrayList<>();

    static {
        AUTH_IGNORE_LIST.add(new Endpoint("/", HttpMethod.GET));
        AUTH_IGNORE_LIST.add(new Endpoint("/policy-form", HttpMethod.GET));
        AUTH_IGNORE_LIST.add(new Endpoint("/join-form", HttpMethod.GET));
        AUTH_IGNORE_LIST.add(new Endpoint("/join-complete-form", HttpMethod.GET));
        AUTH_IGNORE_LIST.add(new Endpoint("/api/v1/member", HttpMethod.POST));
    }

    private record Endpoint(String url, HttpMethod method) { }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfig))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> {
                for (Endpoint endpoint : AUTH_IGNORE_LIST) {
                    if (endpoint.method() == null) {
                        authorize.requestMatchers(endpoint.url()).permitAll();
                    } else {
                        authorize.requestMatchers(endpoint.method(), endpoint.url()).permitAll();
                    }
                }
                authorize.anyRequest().authenticated();
            });

        return http.build();
    }
}