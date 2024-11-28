package com.example.financeWallet.service;

import com.example.financeWallet.dto.CurrencyDTO;
import com.example.financeWallet.entity.CurrencyEntity;
import com.example.financeWallet.repository.CurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    public List<CurrencyDTO> listAll() {
        List<CurrencyEntity> buy = currencyRepository.findAll();
        return buy.stream().map(CurrencyDTO::new).toList();
    }

    public void insert(CurrencyDTO dto) throws IOException, InterruptedException {
//        String apiUrl = "https://economia.awesomeapi.com.br/last/USD-BRL";
//
//        //Configuração do HttpClient
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(apiUrl))
//                .GET()
//                .build();
//
//        //Enviando a requisição e obtendo a resposta
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        dto = null;
//        if (response.statusCode() == 200) {
//            //Processando o JSON com Jackson
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonResponse = response.body();
//
//            //Extraindo o objeto USDBRL do JSON
//            dto = objectMapper.readTree(jsonResponse)
//                    .get("USDBRL")
//                    .traverse(objectMapper)
//                    .readValueAs(CurrencyDTO.class);
//            System.out.println(dto);
            dto = cryptoAPi();
            CurrencyEntity buy = new CurrencyEntity(dto);
            currencyRepository.save(buy);
    }

    public CurrencyDTO cryptoAPi () throws IOException, InterruptedException {
        CurrencyDTO dto;

        String apiUrl = "https://economia.awesomeapi.com.br/last/USD-BRL";

        //Configuração do HttpClient
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        //Enviando a requisição e obtendo a resposta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        dto = null;
        if (response.statusCode() == 200) {
            //Processando o JSON com Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = response.body();

            //Extraindo o objeto USDBRL do JSON
            dto = objectMapper.readTree(jsonResponse)
                    .get("USDBRL")
                    .traverse(objectMapper)
                    .readValueAs(CurrencyDTO.class);
            System.out.println(dto);

        }
        return dto;
    }

}
