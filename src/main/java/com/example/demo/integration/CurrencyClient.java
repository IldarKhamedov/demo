package com.example.demo.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CurrencyClient {   //для обращения к веб-сервису
    @Autowired
    @Qualifier("production-rest-template")
    private RestTemplate restTemplate;
    @Autowired
    private final ObjectMapper objectMapper;

    /*private final RestTemplate restTemplate = new RestTemplate(); //todo
    private final ObjectMapper objectMapper = new ObjectMapper(); //todo, десериализатор-из строки/в java объект*/

    @Value("${application.currency_url}") //куда обращаемся
    private String currencyUrl;

    @SneakyThrows
    public Map<String, BigDecimal> getCurrencyRates() {
        ResponseEntity<String> response = restTemplate.getForEntity(currencyUrl, String.class);
        CurrencyRateResponse deserialized = objectMapper.readValue(response.getBody(), CurrencyRateResponse.class);
        return deserialized.getRates();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrencyRateResponse {//ответ базы с учетом веб=сервиса
        private Map<String, BigDecimal> rates;
    }


}
