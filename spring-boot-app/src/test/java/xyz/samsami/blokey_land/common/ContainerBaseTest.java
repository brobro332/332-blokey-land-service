package xyz.samsami.blokey_land.common;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class ContainerBaseTest {
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    static {
        Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );

        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine");
        POSTGRES_CONTAINER.start();
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        String originalJdbcUrl = POSTGRES_CONTAINER.getJdbcUrl();
        String p6spyJdbcUrl = originalJdbcUrl.replace("jdbc:postgresql:", "jdbc:p6spy:postgresql:");
        registry.add("spring.datasource.url", () -> p6spyJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.p6spy.engine.spy.P6SpyDriver");

        registry.add("spring.flyway.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.flyway.user", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.flyway.password", POSTGRES_CONTAINER::getPassword);

        registry.add("file.upload-dir", () -> "DEFAULT");
        registry.add("server.port", () -> "8081");
        registry.add("server.address", () -> "0.0.0.0");
    }
}