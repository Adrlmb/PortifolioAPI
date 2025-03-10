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

        CurrencyEntity buy = new CurrencyEntity(dto); //pega o que foi digitado no post
        dto = cryptoAPi(dto.getCode()); // grava os dados da api diretamento no dto de acordo com a crypto digitada anteriormente
        CurrencyEntity finished = new CurrencyEntity(dto); // manda os dados para o banco de dados
        currencyRepository.save(finished); // salva
    }

    public CurrencyDTO cryptoAPi (String code) throws IOException, InterruptedException {
        CurrencyDTO dto;
        String apiUrl = "https://economia.awesomeapi.com.br/last/"+ code.toUpperCase() +"-USD";

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
                    .get(code +"USD")
                    .traverse(objectMapper)
                    .readValueAs(CurrencyDTO.class);
        }
        return dto;
    }

}
