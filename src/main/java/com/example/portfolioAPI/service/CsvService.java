package com.example.portfolioAPI.service;

import com.example.portfolioAPI.dto.CsvDTO;
import com.example.portfolioAPI.entity.CsvEntity;
import com.example.portfolioAPI.repository.CsvRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
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

    public List<CsvDTO> listAll(){
        List<CsvEntity> csv = csvRepository.findAll();
        return csv.stream().map(CsvDTO::new).toList();
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

                CsvEntity entity = new CsvEntity();
                entity.setBuyDate(linha[0]);
                entity.setCode(linha[1]);
                entity.setCryptoValue(linha[3]);
                entity.setAmountCryptoPurchased(linha[4]);
                entity.setTaxAmount(masterService.bigDecimalConverter(linha[6]));
                entity.setTaxCryptoCode(linha[7]);

                transacoes.add(entity);
            }

            csvRepository.saveAll(transacoes);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar CSV: " + e.getMessage());
        }
    }
}
