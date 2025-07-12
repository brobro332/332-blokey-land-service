package xyz.samsami.blokey_land.common;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class ContainerBaseTest {
    @Container
    static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    static {
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("DATA_POSTGRES_URI", postgresContainer::getJdbcUrl);
        registry.add("DATA_POSTGRES_USERNAME", postgresContainer::getUsername);
        registry.add("DATA_POSTGRES_PASSWORD", postgresContainer::getPassword);
        registry.add("FILE_UPLOAD_DIR", () -> "/test-uploads/");
    }
}