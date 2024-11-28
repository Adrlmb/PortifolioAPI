package com.example.financeWallet.service;

import com.example.financeWallet.dto.CurrencyDTO;
import com.example.financeWallet.entity.CurrencyEntity;
import com.example.financeWallet.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    public List<CurrencyDTO> listAll() {
        List<CurrencyEntity> buy = currencyRepository.findAll();
        return buy.stream().map(CurrencyDTO::new).toList();
    }

    public void insert(CurrencyDTO dto) {
        CurrencyEntity buy = new CurrencyEntity(dto);
        currencyRepository.save(buy);
    }
}
