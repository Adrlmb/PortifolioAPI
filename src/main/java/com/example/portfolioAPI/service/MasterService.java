package com.example.portfolioAPI.service;
import com.example.portfolioAPI.dto.BuyDTO;
import com.example.portfolioAPI.entity.BuyEntity;
import com.example.portfolioAPI.repository.BuyRepository;
import com.example.portfolioAPI.repository.CurrencyRepository;
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

    //PATCH
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
    public void insert(BuyDTO dto) throws IOException, InterruptedException {

        dto.setBid(apiBid(dto.getCode(), dto.getCodein(), dto));
        dto.setProfit(profitCalculation(dto.getBid(), dto.getAmountCryptoPurchased(), dto.getAmountSpent(), dto.getTaxAmount(), dto));
        dto.setTotalProfit(totalProfit(dto.getProfit()));
        dto.setAverageValue(average());

        BuyEntity buyEntity = new BuyEntity(dto);// pega o que foi digitado no post
        buyRepository.save(buyEntity);// Salva na tabela buy
    }


    public BigDecimal apiBid(String code, String codein, BuyDTO buyDTO) throws IOException, InterruptedException {

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

    public BigDecimal profitCalculation(BigDecimal currentValue, String amountCrypto, BigDecimal amountSpent, BigDecimal taxAmount, BuyDTO buyDTO){

        BigDecimal mult = currentValue.multiply(bigDecimalConverter(amountCrypto));
        BigDecimal cost = amountSpent.add(taxAmount);
        return (mult.subtract(cost));
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
