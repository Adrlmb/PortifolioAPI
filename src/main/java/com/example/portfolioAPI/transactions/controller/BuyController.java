package com.example.portfolioAPI.transactions.controller;

import com.example.portfolioAPI.transactions.dto.BuyDTO;
import com.example.portfolioAPI.transactions.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/buy")
public class BuyController {

    @Autowired
    private MasterService masterService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listALl() throws IOException, InterruptedException {
        //add nullException
        List<BuyDTO> list = masterService.listAll();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "List returned successfully");
        response.put("total", list.size());
        response.put("transactions", list);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public BuyDTO listByID(@PathVariable("id") Long id) {
        return masterService.listByID(id);
    }

    @PostMapping
    public ResponseEntity<String> insert(@RequestBody BuyDTO dto) throws IOException, InterruptedException {
        masterService.insert(dto);
        return ResponseEntity.ok("Transaction added successfully");
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
