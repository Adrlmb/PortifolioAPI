package com.example.financeWallet.service;

import com.example.financeWallet.dto.BuyDTO;
import com.example.financeWallet.dto.CurrencyDTO;
import com.example.financeWallet.entity.BuyEntity;
import com.example.financeWallet.entity.CurrencyEntity;
import com.example.financeWallet.repository.BuyRepository;
import com.example.financeWallet.repository.CurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
public class MasterService {
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private BuyRepository buyRepository;

    public void insert(BuyDTO dto) throws IOException, InterruptedException {
        CurrencyDTO currencyDTO = new CurrencyDTO();
        BuyEntity buyEntity = new BuyEntity(dto);// pega o que foi digitado no post
        currencyDTO = cryptoAPi(dto.getPurchasedCryptoCode(), dto.getCryptoUsed()); // grava os dados da api diretamento no dto de acordo com a crypto digitada anteriormente
        CurrencyEntity finished = new CurrencyEntity(currencyDTO); // manda os dados para o banco de dados
        currencyRepository.save(finished);//Salva na tabela currency
        buyRepository.save(buyEntity);// Salva na tabela buy
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
