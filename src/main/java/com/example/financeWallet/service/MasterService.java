package com.example.financeWallet.service;
import com.example.financeWallet.dto.BuyDTO;
import com.example.financeWallet.dto.CurrencyDTO;
import com.example.financeWallet.entity.BuyEntity;
import com.example.financeWallet.entity.CurrencyEntity;
import com.example.financeWallet.repository.BuyRepository;
import com.example.financeWallet.repository.CurrencyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    //GET list all
    public List<BuyDTO> listAll(){
        List<BuyEntity> buy = buyRepository.findAll();
        return buy.stream().map(BuyDTO::new).toList();
    }

    //GET list by id
    public List<BuyDTO> listByID(Long id){
        Optional<BuyEntity> buy = buyRepository.findById(id);
        return buy.stream().map(BuyDTO::new).toList();
    }

    //PUT 
    public BuyDTO modify(BuyDTO buyDTO){
        BuyEntity buy = new BuyEntity(buyDTO);
        return new BuyDTO(buyRepository.save(buy));
    }

    //DELETE
    public void delete(Long id) {
        BuyEntity buy = buyRepository.findById(id).get();
        buyRepository.delete(buy);
    }

    //POST insert
    public void insert(BuyDTO buyDTO) throws IOException, InterruptedException {
        setBidValue(buyDTO.getCode(), buyDTO.getCodein(), buyDTO);
        setProfitValue(buyDTO.getBid(), buyDTO.getAmountCryptoPurchased(), buyDTO.getAmountSpent(), buyDTO.getTaxAmount(), buyDTO);
        buyDTO.setTotalProfit(totalProfit(buyDTO.getProfit()));
        BuyEntity buyEntity = new BuyEntity(buyDTO);// pega o que foi digitado no post
        buyRepository.save(buyEntity);// Salva na tabela buy
    }


    public void setBidValue(String code, String codein, BuyDTO buyDTO) throws IOException, InterruptedException {
        String chave = code.toUpperCase() + codein.toUpperCase();
        String api = "https://economia.awesomeapi.com.br/last/" + code + "-" + codein;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode root = objectMapper.readTree(response.body())
                    .get(chave);

            BigDecimal bid = bigDecimalConverter(root.get("bid").asText());
            buyDTO.setBid(bid.setScale(2, RoundingMode.DOWN)); // Atualiza só o campo bid
        }else{
            System.out.println("Erro ao buscar cotação");
        }
    }

    public void setProfitValue(BigDecimal currentValue, String amountCrypto, BigDecimal amountSpent, BigDecimal taxAmount, BuyDTO buyDTO){
        BigDecimal mult = currentValue.multiply(bigDecimalConverter(amountCrypto));
        BigDecimal cost = amountSpent.add(taxAmount);
        buyDTO.setProfit(mult.subtract(cost));
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


// grava os dados da api em um dto com os campos identicos
//    public CurrencyDTO cryptoAPi (String code, String codein) throws IOException, InterruptedException {
//        code = code.toUpperCase();
//        codein = codein.toUpperCase();
//        String apiUrl = "https://economia.awesomeapi.com.br/last/"+ code +"-"+ codein;
//
//        //Configuração do HttpClient
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(apiUrl))
//                .GET()
//                .build();
//
//        //Enviando a requisição e obtendo a resposta
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//
//        if (response.statusCode() == 200) {
//            //Extraindo o objeto USDBRL do JSON
//            return objectMapper.readTree(response.body())
//                    .get(code + codein)
//                    .traverse(objectMapper)
//                    .readValueAs(CurrencyDTO.class);
//        }
//       return null;
//    }
}
