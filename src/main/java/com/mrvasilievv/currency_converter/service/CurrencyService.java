package com.mrvasilievv.currencyconverter.service;


import com.mrvasilievv.currencyconverter.model.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

    @Value("${api.currency.url}")
    private String apiUrl;

    public double getExchangeRate(String fromCurrency, String toCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s?base=%s&symbols=%s", apiUrl, fromCurrency, toCurrency);
        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
        if (response != null && response.getRates() != null) {
            return response.getRates().get(toCurrency);
        }
        return 0.0;
    }
}