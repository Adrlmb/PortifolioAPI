package com.example.financeWallet.service;

import com.example.financeWallet.dto.BuyDTO;
import com.example.financeWallet.entity.BuyEntity;
import com.example.financeWallet.repository.BuyRepository;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BuyService {
    @Autowired
    private BuyRepository buyRepository;
    @Autowired
    private MasterService masterService;

    public List<BuyDTO> listAll() {
        List<BuyEntity> buy = buyRepository.findAll();
        return buy.stream().map(BuyDTO::new).toList();
    }

    public List<BuyDTO> listByID(Long id) {
        Optional<BuyEntity> buy = buyRepository.findById(id);
        return buy.stream().map(BuyDTO::new).toList();
    }

    public void insert(BuyDTO dto){
        BuyEntity buy = new BuyEntity(dto);
        buyRepository.save(buy);
    }

    @Transactional
    public BuyDTO modifyById(Long id, BuyDTO dto) throws IOException, InterruptedException {
        BuyEntity currentTransaction = buyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if(dto.getCode() != null){
            currentTransaction.setCode(dto.getCode());
            masterService.setBidValue(currentTransaction.getCode(), currentTransaction.getCodein(), dto);

        }
        return new BuyDTO(buyRepository.save(currentTransaction));
    }

    public void delete(Long id) {
        BuyEntity buy = buyRepository.findById(id).get();
        buyRepository.delete(buy);
    }

    public BuyDTO findById(Long id) {
        return new BuyDTO(buyRepository.findById(id).get());
    }
}
