package xyz.samsami.blokey_land;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class BlokeyLandApplicationTests {
	@Container
	static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
		"postgres:15-alpine");

	@DynamicPropertySource
	static void overrideProps(org.springframework.test.context.DynamicPropertyRegistry registry) {
		registry.add("DATA_POSTGRES_URI", postgresContainer::getJdbcUrl);
		registry.add("DATA_POSTGRES_USERNAME", postgresContainer::getUsername);
		registry.add("DATA_POSTGRES_PASSWORD", postgresContainer::getPassword);
		registry.add("FILE_UPLOAD_DIR", () -> "/test-uploads/");
	}

	@BeforeAll
	static void init() {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry ->
			System.setProperty(entry.getKey(), entry.getValue())
		);
	}

	@Test
	void contextLoads() {
	}
}
