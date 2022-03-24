package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = "test") //application-test.yaml
class DemoApplicationTests {

	private final RestTemplate restTemplate = new RestTemplate(); //fixme move to Bean

	@Test
	void testApi() {
		String body = "{\"id\":1, \"value\":100}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8082/api/v1/account/edit", HttpMethod.PUT, requestEntity, String.class);

		HttpStatus statusCode = responseEntity.getStatusCode();
		assertEquals(HttpStatus.OK, statusCode);
	}

}
