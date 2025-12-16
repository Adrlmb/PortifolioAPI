package com.example.portfolioAPI.transactions.service;

import com.example.portfolioAPI.exceptions.IdNotFoundException;
import com.example.portfolioAPI.transactions.dto.BuyDTO;
import com.example.portfolioAPI.transactions.entity.BuyEntity;
import com.example.portfolioAPI.transactions.repository.BuyRepository;
import com.example.portfolioAPI.users.dto.UserDTO;
import com.example.portfolioAPI.users.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MasterService {

    private final BuyRepository buyRepository;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;

    // Lista apenas as transações do usuário logado
    public List<BuyDTO> listAllByUser(UserEntity user) throws IOException, InterruptedException {
        List<BuyEntity> list = buyRepository.findAllByUser(user);
        return list.stream().map(BuyDTO::new).toList();
    }

    // Retorna a transação específica do usuário
    public BuyDTO listByIdAndUser(Long id, UserEntity user) {
        BuyEntity buy = buyRepository.findByIdAndUser(id, user).orElseThrow(() -> new IdNotFoundException(id));
        return new BuyDTO(buy);
    }

    // Insere transação e atribui ao usuário logado
    public void insert(BuyDTO dto, UserEntity user) throws IOException, InterruptedException {
        BuyEntity buyEntity = new BuyEntity(dto);// pega o que foi digitado no post
        buyEntity.setUser(user); // vincula o user
        buyRepository.save(buyEntity);// Salva na tabela buy
        updateBid();
    }

    public void updateBid() throws IOException, InterruptedException {
        //testar tempo de resposta entre esse e o código comentado
        List<BuyEntity> entity = buyRepository.findAll();

        Map<String, BigDecimal> hashBid = new HashMap<>();

        for (BuyEntity row : entity) {
            String key = row.getCode() + "-" + row.getCodein();
            BigDecimal bid = hashBid.get(key);

            if (bid == null) {
                bid = apiBid(row.getCode(), row.getCodein());
                hashBid.put(key, bid);
            }

            row.setBid(bid);
            BigDecimal profit = profit(row.getAmountCryptoPurchased(), bid, row.getAmountSpent());
            row.setProfit(profit);
        }

        buyRepository.saveAll(entity);
    }

    @Transactional
    public BuyDTO modifyById(Long id, BuyDTO dto, UserEntity user) throws IOException, InterruptedException {
        BuyEntity currentTransaction = buyRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IdNotFoundException(id));

        if (dto.getCodein() != null) {
            currentTransaction.setCodein(dto.getCodein());
        }

        if (dto.getCode() != null) {
            currentTransaction.setCode(dto.getCode());
            BigDecimal updatedBid = apiBid(currentTransaction.getCode(), currentTransaction.getCodein());
            currentTransaction.setBid(updatedBid);
        }

        //se não for nulo pega os dados do dto e altera o bd
        Optional.ofNullable(dto.getCodein()).ifPresent(currentTransaction::setCodein);
        Optional.ofNullable(dto.getBuyDate()).ifPresent(currentTransaction::setBuyDate);
        Optional.ofNullable(dto.getAmountCryptoPurchased()).ifPresent(currentTransaction::setAmountCryptoPurchased);
        Optional.ofNullable(dto.getCryptoValue()).ifPresent(currentTransaction::setCryptoValue);
        Optional.ofNullable(dto.getAmountSpent()).ifPresent(currentTransaction::setAmountSpent);
        Optional.ofNullable(dto.getTaxCryptoCode()).ifPresent(currentTransaction::setTaxCryptoCode);
        Optional.ofNullable(dto.getTaxAmount()).ifPresent(currentTransaction::setTaxAmount);
        Optional.ofNullable(dto.getExchange()).ifPresent(currentTransaction::setExchange);

        updateBid();
        return new BuyDTO(buyRepository.save(currentTransaction));
    }

    public void delete(Long id, UserEntity user) {
        BuyEntity buy = buyRepository.findByIdAndUser(id, user).orElseThrow(() -> new IdNotFoundException(id));
        buyRepository.delete(buy);
    }

    public BigDecimal apiBid(String code, String codein) throws IOException, InterruptedException {
        String chave = code.toUpperCase() + codein.toUpperCase();
        String api = "https://economia.awesomeapi.com.br/last/" + code + "-" + codein;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String bid = objectMapper.readTree(response.body())
                    .get(chave)
                    .get("bid").asText();

            return bigDecimalConverter(bid).setScale(2, RoundingMode.DOWN); // Converte a string bid em bigDecimal e fixa em apenas 2 casas decimais.
        } else {
            System.out.println("Erro ao buscar cotação");
            return null;
        }
    }

    public BigDecimal profit(String amount, BigDecimal currentPrice, BigDecimal totalSpent) {
        if (totalSpent == null) {
            totalSpent = bigDecimalConverter("0");
        }
        BigDecimal amountBrought = bigDecimalConverter(amount);
        return amountBrought.multiply(currentPrice).subtract(totalSpent);
    }

    public BigDecimal bigDecimalConverter(String value) {
        try {
            if (value == null || value.isBlank()) {
                return BigDecimal.ZERO;
            } else {
                return new BigDecimal(value);
            }
        } catch (NullPointerException e) {
            System.out.println("Erro ao converter");
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal totalProfit(BigDecimal profit) {
        try {
            BigDecimal total = buyRepository.sumProfit();
            return total != null ? total : profit;
        } catch (DataAccessException e) {
            System.err.println("Erro ao acessar o banco de dados: " + e.getMessage());
            return BigDecimal.ZERO;
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal average() {
        System.out.println(buyRepository.average());
        return buyRepository.average();
    }
}
