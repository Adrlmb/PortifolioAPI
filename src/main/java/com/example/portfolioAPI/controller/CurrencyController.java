package com.example.portfolioAPI.controller;

import com.example.portfolioAPI.dto.CurrencyDTO;
import com.example.portfolioAPI.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/currency")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public List<CurrencyDTO> listALl(){
        return currencyService.listAll();
    }

    @PostMapping
    public void insert(@RequestBody CurrencyDTO dto) throws IOException, InterruptedException {
        currencyService.insert(dto);
    }
}
