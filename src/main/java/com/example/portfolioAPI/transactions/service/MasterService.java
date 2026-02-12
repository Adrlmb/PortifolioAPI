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
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service

public class MasterService {

    private final BuyRepository buyRepository;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public MasterService(BuyRepository buyRepository) {
        this.buyRepository = buyRepository;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Lista apenas as transações do usuário logado
    public List<BuyDTO> listAllByUser(UserEntity user) throws IOException, InterruptedException {
        log.info("Procurando transações...");
        System.out.println("já passou do log");
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

    public void updateBid() {
        log.info("Iniciando atualização de cotações para todas as entidades...");
        List<BuyEntity> entities = buyRepository.findAll();

        // Mapa para servir de "cache" temporário nesta execução
        // Chave: "BTC-BRL", Valor: 50000.00
        Map<String, BigDecimal> priceCache = new HashMap<>();

        for (BuyEntity row : entities) {
            String key = (row.getCode() + "-" + row.getCodein()).toUpperCase();

            try {
                BigDecimal bid;

                // Se já buscamos o preço dessa moeda neste loop, usamos o que está no mapa
                if (priceCache.containsKey(key)) {
                    bid = priceCache.get(key);
                    log.debug("Usando preço do cache para: {}", key);
                } else {
                    // Se não está no mapa, faz a chamada à API
                    log.info("Buscando cotação real-time para: {}", key);
                    bid = apiBid(row.getCode(), row.getCodein());

                    if (bid != null) {
                        priceCache.put(key, bid);
                    }
                }

                if (bid != null) {
                    row.setBid(bid);
                    BigDecimal profit = profit(row.getAmountCryptoPurchased(), bid, row.getAmountSpent());
                    row.setProfit(profit);
                }

            } catch (Exception e) {
                // Se uma moeda der erro, logamos o erro e continuamos para a próxima
                log.error("Erro ao atualizar cotação da moeda {}: {}", key, e.getMessage());
            }
        }

        buyRepository.saveAll(entities);
        log.info("Atualização concluída com sucesso para {} registros.", entities.size());
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
        String api = "https://api.binance.com/api/v3/ticker/price?symbol=" + chave;

        try {
            log.info("\nChamando API para cotação: {}\n", chave);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(api))
                    .GET()
                    .timeout(java.time.Duration.ofSeconds(20)) // Define um limite de 5 segundos
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String bid = objectMapper.readTree(response.body())
                        .get("price").asText();
                System.out.println("JSON RECEBIDO: " + response.body());
                return bigDecimalConverter(bid).setScale(2, RoundingMode.DOWN); // Converte a string bid em bigDecimal e fixa em apenas 2 casas decimais.
            } else {
                log.warn("API retornou status {}. Cotação não disponível para {}", response.statusCode(), chave);
                return BigDecimal.ZERO;
            }
        }catch (java.net.ConnectException e){
            log.error("SERVIDOR DA API FORA DO AR: Não foi possível conectar a {}. Verifique a conexão.", api);
            return BigDecimal.ZERO;
        }catch (java.net.http.HttpConnectTimeoutException e){
            log.error("TIMEOUT: A API demorou demais para responder. Pulando atualização de {}", chave);
            return BigDecimal.ZERO;
        }catch (Exception e){
            log.error("ERRO INESPERADO ao buscar cotação: {}", e.getMessage());
            return BigDecimal.ZERO;
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
