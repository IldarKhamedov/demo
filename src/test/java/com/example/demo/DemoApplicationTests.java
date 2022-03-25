package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

//integration test
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)// сервер будет использовать сокет TCP
                                                                            // и прослушивать HTTP-запросы на определенном порту
                                                                           //прописываем порт в application-test.yaml
@ActiveProfiles(value = "test") //application-test.yaml
                                // используется для объявления того,
                                // какие профили определения active bean следует использовать
                                // при загрузке  ApplicationContext  классов
@Import(DemoApplicationTests.MyTestConfig.class)
@ContextConfiguration(initializers = DemoApplicationTests.PostgreSQLInitializer.class)
@Testcontainers
class DemoApplicationTests {

	@Container
	public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
			.withDatabaseName("mydb")
			.withUsername("myuser")
			.withPassword("mypass");

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void testApi() {

		String body = "{\"id\":1,\"value\":100}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);// сущность HTTP-запроса или ответа, состоящую из заголовков и тела
		ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8082/api/v1/account/edit", HttpMethod.PUT, requestEntity, String.class);

		HttpStatus statusCode = responseEntity.getStatusCode();
		assertEquals(HttpStatus.OK, statusCode);
	}

	/*@Test
	void testApiInfo() {
		String body = "{\"id\":1,\"value\":1000}";
		//String body = "{\"id\":1}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);// сущность HTTP-запроса или ответа, состоящую из заголовков и тела
		ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8082/api/v1/account/remove", HttpMethod.DELETE, requestEntity, String.class);

		HttpStatus statusCode = responseEntity.getStatusCode();
		assertEquals(HttpStatus.OK, statusCode);
	}*/


	@TestConfiguration
	public static class MyTestConfig {
		@Bean
		public RestTemplate restTemplate(){
			return new RestTemplate();
		}
	}

	static class PostgreSQLInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of(
					"spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
					"spring.datasource.username=" + postgreSQLContainer.getUsername(),
					"spring.datasource.password=" + postgreSQLContainer.getPassword()
			).applyTo(configurableApplicationContext.getEnvironment());
		}
	}

}
