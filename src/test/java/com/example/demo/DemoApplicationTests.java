package com.example.demo;

import com.example.demo.api.AccountController;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.nio.file.Files;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
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
	public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")//контейнер для базы данных
			.withDatabaseName("mydb") //создается и настройки подменяются в application.yaml на "новые настройки"
			.withUsername("myuser")
			.withPassword("mypass");

	@Autowired//бин
	@Qualifier("test-rest-template")
	private RestTemplate restTemplate;

	/*@Value("classpath:json/currency_response.json")//обращение к ресурсу-имитация JSON
	private Resource myResource;*/

	@Value("classpath:json/currency_response.json")//обращение к ресурсу-имитация JSON
	private Resource myResource;

	private static WireMockServer wireMockServer = new WireMockServer(8083);//контейнер для имитации веб-сервиса

	@BeforeAll//стартует до всего
	public static void init() {
		wireMockServer.start();
	}

	@Test
	void testApi() throws Exception {
		File file = myResource.getFile();
		String currencyResponse = new String(Files.readAllBytes(file.toPath()));

		wireMockServer.stubFor(get(urlEqualTo("/my-currency-path"))
				.willReturn(aResponse()
						.withBody(currencyResponse)));

		//String body = "{\"id\":1}";

		AccountController.AccountInfoRequest body = new AccountController.AccountInfoRequest();
		body.setId(1);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<AccountController.AccountInfoRequest> requestEntity = new HttpEntity<AccountController.AccountInfoRequest>(body, headers);// сущность HTTP-запроса или ответа, состоящую из заголовков и тела
		ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8082/api/v1/account/info", HttpMethod.GET, requestEntity, String.class);

		HttpStatus statusCode = responseEntity.getStatusCode();
		assertEquals(HttpStatus.OK, statusCode);
		//assertEquals("{\"id\":1,\"oldValue\":100,\"valueRub\":9567}", responseEntity.getBody());
	}




	@TestConfiguration
	public static class MyTestConfig {
		@Bean(name = "test-rest-template")
		public RestTemplate restTemplate(){
			return new RestTemplate();
		}
	}

	static class PostgreSQLInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues.of(
					"spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),//назначается новый порт
					"spring.datasource.username=" + postgreSQLContainer.getUsername(),//имя из .withUsername("myuser")
					"spring.datasource.password=" + postgreSQLContainer.getPassword()//пароль из .withPassword("mypass");
			).applyTo(configurableApplicationContext.getEnvironment());  //"новые настройки"
		}
	}

}
