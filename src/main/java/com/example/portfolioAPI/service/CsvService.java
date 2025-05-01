package com.example.portfolioAPI.service;

import com.example.portfolioAPI.dto.CsvDTO;
import com.example.portfolioAPI.entity.CsvEntity;
import com.example.portfolioAPI.repository.CsvRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class CsvService {

    private final CsvRepository csvRepository;
    private final MasterService masterService;

    public List<CsvDTO> listAll() throws IOException, InterruptedException {
        updateBid();
        List<CsvEntity> csv = csvRepository.findAll();
        return csv.stream().map(CsvDTO::new).toList();
    }

    public void updateBid() throws IOException, InterruptedException {
        List<CsvEntity> entity = csvRepository.findAll();
        String lastCode = null;
        String lastCodein = null;
        BigDecimal lastBid = null;
        BigDecimal profitLoss = null;

        for(CsvEntity row : entity){
            if(row.getCode().equals(lastCode) && row.getCodein().equals(lastCodein)){
                profitLoss = (profit(row.getAmountCryptoPurchased(), lastBid, row.getAmountSpent()));

                row.setBid(lastBid);
                row.setProfit(profitLoss.setScale(2, RoundingMode.DOWN));
            }else {
                BigDecimal bid = masterService.apiBid(row.getCode(), row.getCodein());
                profitLoss = (profit(row.getAmountCryptoPurchased(), bid, row.getAmountSpent()));

                row.setProfit(profitLoss.setScale(2, RoundingMode.DOWN));
                row.setBid(bid);

                lastCode = row.getCode();
                lastCodein = row.getCodein();
                lastBid = bid;
            }
        }

        csvRepository.saveAll(entity);
    }

    public void importarCSV(MultipartFile file) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<CsvEntity> transacoes = new ArrayList<>();
            String[] linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readNext()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false; // Pular cabeçalho
                    continue;
                }

                CsvDTO dto = new CsvDTO();
                BigDecimal fee = masterService.bigDecimalConverter(linha[6]);
                BigDecimal totalValue = masterService.bigDecimalConverter(linha[5]);

                dto.setCode(linha[1]);
                dto.setCodein(linha[7]);
                dto.setBuyDate(linha[0]);
                dto.setCryptoValue(linha[3]);
                dto.setAmountCryptoPurchased(linha[4]);
                dto.setTaxAmount(fee);
                dto.setTaxCryptoCode(linha[7]);
                dto.setAmountSpent(fee.add(totalValue));

                transacoes.add(new CsvEntity(dto));
            }

            csvRepository.saveAll(transacoes);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar CSV: " + e.getMessage());
        }
    }

    public BigDecimal profit(String amount, BigDecimal currentPrice, BigDecimal totalSpent){
        BigDecimal amountBrought = masterService.bigDecimalConverter(amount);
    return amountBrought.multiply(currentPrice).subtract(totalSpent);
    }
}
