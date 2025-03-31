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

    private final CurrencyRepository currencyRepository;
    private final BuyRepository buyRepository;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public MasterService(CurrencyRepository currencyRepository, BuyRepository buyRepository){
        this.currencyRepository = currencyRepository;
        this.buyRepository = buyRepository;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public void insert(BuyDTO buyDTO) throws IOException, InterruptedException {

        CurrencyDTO currencyDTO = cryptoAPi(buyDTO.getPurchasedCryptoCode(), buyDTO.getCryptoUsed()); // grava os dados da api diretamento no dto de acordo com a crypto digitada anteriormente
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyDTO); // manda os dados para o banco de dados
        BuyEntity buyEntity = new BuyEntity(buyDTO);// pega o que foi digitado no post
        currencyRepository.save(currencyEntity);//Salva na tabela currency
        buyRepository.save(buyEntity);// Salva na tabela buy
    }

    public CurrencyDTO cryptoAPi (String code, String codein) throws IOException, InterruptedException {
        code = code.toUpperCase();
        codein = codein.toUpperCase();
        String apiUrl = "https://economia.awesomeapi.com.br/last/"+ code +"-"+ codein;

        //Configuração do HttpClient
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        //Enviando a requisição e obtendo a resposta
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        if (response.statusCode() == 200) {
            //Extraindo o objeto USDBRL do JSON
            return objectMapper.readTree(response.body())
                    .get(code + codein)
                    .traverse(objectMapper)
                    .readValueAs(CurrencyDTO.class);
        }
       return null;
    }
}
