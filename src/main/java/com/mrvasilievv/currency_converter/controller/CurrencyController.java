package com.mrvasilievv.currencyconverter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import com.mrvasilievv.currencyconverter.model.ExchangeRateResponse;

@Controller
public class CurrencyController {

    @Value("${exchange.api.url}")
    private String apiUrl;

    @GetMapping("/")
    public String index() {
        return "index"; // Стартовая страница с формой
    }

    @GetMapping("/convert")
    public String convert(
            @RequestParam String from, // Валюта отправления
            @RequestParam String to,   // Валюта назначения
            @RequestParam double amount, // Сумма для конвертации
            Model model) {

        // Создаём RestTemplate для выполнения HTTP-запроса
        RestTemplate restTemplate = new RestTemplate();
        String url = apiUrl + "?base=" + from + "&symbols=" + to;

        try {
            // Отправка запроса и получение ответа
            ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);

            // Проверяем, что ответ корректный
            if (response == null || response.getRates() == null || !response.getRates().containsKey(to)) {
                model.addAttribute("error", "Не удалось получить курс для " + from + "→" + to);
                return "index";
            }

            // Получаем курс валюты
            double rate = response.getRates().get(to);
            double result = amount * rate; // Результат конвертации

            // Добавляем данные в модель для отображения на странице
            model.addAttribute("from", from);
            model.addAttribute("to", to);
            model.addAttribute("rate", result); // Конвертированная сумма
            model.addAttribute("amount", amount); // Исходная сумма

            return "result"; // Переход на страницу результата

        } catch (Exception e) {
            // Ловим ошибку, если API не отвечает или возникает другая проблема
            model.addAttribute("error", "Произошла ошибка при получении данных с API: " + e.getMessage());
            return "index"; // Возвращаем на главную страницу с ошибкой
        }
    }
}
