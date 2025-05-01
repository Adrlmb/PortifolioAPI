package com.example.portfolioAPI.controller;

import com.example.portfolioAPI.dto.BuyDTO;
import com.example.portfolioAPI.repository.CsvRepository;
import com.example.portfolioAPI.service.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/csv")
public class CsvController {

    @Autowired
    private CsvService csvService;

    @Autowired
    private CsvRepository csvRepository;

    @PostMapping
    public ResponseEntity<String> importar(@RequestParam("file") MultipartFile file) {
        csvService.importarCSV(file);
        return ResponseEntity.ok("Arquivo importado e salvo com sucesso!");
    }

    @GetMapping
    public List<BuyDTO> listALl() throws IOException, InterruptedException {
        return csvService.listAll();
    }

}
