package com.example.financeWallet.controller;

import com.example.financeWallet.dto.BuyDTO;
import com.example.financeWallet.service.BuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/buy")
public class BuyController {
    @Autowired
    private BuyService buyService;

    @GetMapping
    public List<BuyDTO> listALl() {
        return buyService.listAll();
    }

    @GetMapping("/{id}")
    public List<BuyDTO> listByID(@PathVariable("id") Long id) {
        return buyService.listByID(id);
    }

    @PostMapping
    public void insert(@RequestBody BuyDTO dto) {
        buyService.insert(dto);
    }

    @PutMapping
    public BuyDTO modify(@RequestBody BuyDTO dto) {
        return buyService.modify(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        buyService.delete(id);
        return ResponseEntity.ok().build();
    }

}
