package com.example.portfolioAPI.service;

import com.example.portfolioAPI.dto.CurrencyDTO;
import com.example.portfolioAPI.entity.CurrencyEntity;
import com.example.portfolioAPI.repository.CurrencyRepository;
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
        dto = cryptoAPi(dto.getCode(), dto.getCodein()); // grava os dados da api diretamento no dto de acordo com a crypto digitada anteriormente
        CurrencyEntity finished = new CurrencyEntity(dto); // manda os dados para o banco de dados
        currencyRepository.save(finished); // salva
    }

    public CurrencyDTO cryptoAPi (String code, String codein) throws IOException, InterruptedException {
        CurrencyDTO dto;
        code = code.toUpperCase();
        codein = codein.toUpperCase();
        String apiUrl = "https://economia.awesomeapi.com.br/last/"+ code +"-"+ codein;

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
                    .get(code + codein)
                    .traverse(objectMapper)
                    .readValueAs(CurrencyDTO.class);
        }
        return dto;
    }

}
