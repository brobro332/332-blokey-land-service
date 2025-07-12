package xyz.samsami.blokey_land;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.samsami.blokey_land.common.ContainerBaseTest;

@SpringBootTest
class BlokeyLandApplicationTests extends ContainerBaseTest {

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
