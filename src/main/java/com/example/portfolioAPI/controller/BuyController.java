package com.example.portfolioAPI.controller;

import com.example.portfolioAPI.dto.BuyDTO;
import com.example.portfolioAPI.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/buy")
public class BuyController {

    @Autowired
    private MasterService masterService;

    @GetMapping
    public List<BuyDTO> listALl() {
        return masterService.listAll();
    }

    @GetMapping("/{id}")
    public List<BuyDTO> listByID(@PathVariable("id") Long id) {
        return masterService.listByID(id);
    }

    @PostMapping
    public void insert(@RequestBody BuyDTO dto) throws IOException, InterruptedException {
        masterService.insert(dto);
    }

    @PatchMapping("/{id}")
    public BuyDTO modifyById(@PathVariable Long id,@RequestBody BuyDTO dto) throws IOException, InterruptedException {
        return masterService.modifyById(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        masterService.delete(id);
        return ResponseEntity.ok().build();
    }

}
