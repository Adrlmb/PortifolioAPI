package com.example.portfolioAPI.service;

import com.example.portfolioAPI.dto.BuyDTO;
import com.example.portfolioAPI.entity.BuyEntity;
import com.example.portfolioAPI.repository.BuyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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
import java.util.List;
import java.util.Optional;


@Service
public class MasterService {

    private final BuyRepository buyRepository;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public MasterService(BuyRepository buyRepository){
        this.buyRepository = buyRepository;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<BuyDTO> listAll() throws IOException, InterruptedException {
        List<BuyEntity> buy = buyRepository.findAll();
        return buy.stream().map(BuyDTO::new).toList();
    }

    public List<BuyDTO> listByID(Long id){
        Optional<BuyEntity> buy = buyRepository.findById(id);
        return buy.stream().map(BuyDTO::new).toList();
    }

    public void updateBid() throws IOException, InterruptedException {
        List<BuyEntity> entity = buyRepository.findAll();
        String lastCode = null;
        String lastCodein = null;
        BigDecimal lastBid = null;
        BigDecimal profitLoss = null;

        for(BuyEntity row : entity){
            if(row.getCode().equals(lastCode) && row.getCodein().equals(lastCodein)){
                profitLoss = (profit(row.getAmountCryptoPurchased(), lastBid, row.getAmountSpent()));

                row.setBid(lastBid);
                row.setProfit(profitLoss.setScale(2, RoundingMode.DOWN));
            }else {
                BigDecimal bid = apiBid(row.getCode(), row.getCodein());
                profitLoss = (profit(row.getAmountCryptoPurchased(), bid, row.getAmountSpent()));

                row.setProfit(profitLoss.setScale(2, RoundingMode.DOWN));
                row.setBid(bid);

                lastCode = row.getCode();
                lastCodein = row.getCodein();
                lastBid = bid;
            }
        }
        buyRepository.saveAll(entity);
    }

    @Transactional
    public BuyDTO modifyById(Long id, BuyDTO dto) throws IOException, InterruptedException {
        BuyEntity currentTransaction = buyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if(dto.getCodein() != null){
            currentTransaction.setCodein(dto.getCodein());
        }

        if(dto.getCode() != null){
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

        return new BuyDTO(buyRepository.save(currentTransaction));
    }

    public void delete(Long id) {
        BuyEntity buy = buyRepository.findById(id).get();
        buyRepository.delete(buy);
    }

    public void insert(BuyDTO dto) throws IOException, InterruptedException {
        BuyEntity buyEntity = new BuyEntity(dto);// pega o que foi digitado no post
        buyRepository.save(buyEntity);// Salva na tabela buy
        updateBid();
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
        }else {
            System.out.println("Erro ao buscar cotação");
            return null;
        }
    }

    public BigDecimal profit(String amount, BigDecimal currentPrice, BigDecimal totalSpent){
        BigDecimal amountBrought = bigDecimalConverter(amount);
        return amountBrought.multiply(currentPrice).subtract(totalSpent);
    }

    public BigDecimal bigDecimalConverter(String value){
        try{
            if(value == null || value.isBlank()){
                return BigDecimal.ZERO;
            }else {
                return new BigDecimal(value);
            }
        }catch (NullPointerException e){
            System.out.println("Erro ao converter");
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal totalProfit(BigDecimal profit){
        try{
            BigDecimal total = buyRepository.sumProfit();
            return total != null ? total : profit;
        }catch (DataAccessException e){
            System.err.println("Erro ao acessar o banco de dados: " + e.getMessage());
            return BigDecimal.ZERO;
        }catch (Exception e){
            System.err.println("Erro inesperado: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal average(){
        System.out.println(buyRepository.average());
        return buyRepository.average();
    }
}
