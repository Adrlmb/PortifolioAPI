package com.example.portfolioAPI.service;

import com.example.portfolioAPI.dto.CsvDTO;
import com.example.portfolioAPI.entity.CsvEntity;
import com.example.portfolioAPI.repository.CsvRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    CsvRepository csvRepository;
    @Autowired
    private  MasterService masterService;

    public CsvService(CsvRepository csvRepository){
        this.csvRepository = csvRepository;
    }

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

        for(CsvEntity row : entity){
            if(row.getCode().equals(lastCode) && row.getCodein().equals(lastCodein)){
                row.setBid(lastBid);
            }else {
                BigDecimal bid = masterService.apiBid(row.getCode(), row.getCodein());
                row.setBid(bid);
                lastCode = row.getCode();
                lastCodein = row.getCodein();;
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

                dto.setCode(linha[1]);
                dto.setCodein(linha[7]);
                dto.setBuyDate(linha[0]);
                String price = linha[3].replace(",",""); //retira a virgula da string
                dto.setCryptoValue(masterService.bigDecimalConverter(price));
                dto.setAmountCryptoPurchased(linha[4]);
                dto.setTaxAmount(masterService.bigDecimalConverter(linha[6]));
                dto.setTaxCryptoCode(linha[7]);

                transacoes.add(new CsvEntity(dto));
            }

            csvRepository.saveAll(transacoes);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar CSV: " + e.getMessage());
        }
    }
}
