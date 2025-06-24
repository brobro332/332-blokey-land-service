package xyz.samsami.blokey_land;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BlokeyLandApplication {
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		dotenv.entries().forEach(entry ->
			System.setProperty(entry.getKey(), entry.getValue())
		);
		SpringApplication.run(BlokeyLandApplication.class, args);
	}
}
