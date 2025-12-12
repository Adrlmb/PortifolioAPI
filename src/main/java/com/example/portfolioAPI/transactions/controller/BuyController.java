package com.example.portfolioAPI.transactions.controller;

import com.example.portfolioAPI.transactions.dto.BuyDTO;
import com.example.portfolioAPI.transactions.service.MasterService;
import com.example.portfolioAPI.users.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // Pega o usuário logado
    private UserEntity getAuthenticatedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) auth.getPrincipal();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listALl() throws IOException, InterruptedException {
        UserEntity user = getAuthenticatedUser();
        List<BuyDTO> list = masterService.listAllByUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "List returned successfully");
        response.put("total", list.size());
        response.put("transactions", list);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuyDTO> listByID(@PathVariable("id") Long id) {
        UserEntity user = getAuthenticatedUser();
        BuyDTO dto =  masterService.listByIdAndUser(id, user);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<String> insert(@RequestBody BuyDTO dto) throws IOException, InterruptedException {
        UserEntity user = getAuthenticatedUser();
        masterService.insert(dto, user);

        return ResponseEntity.ok("Transaction added successfully");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BuyDTO> modifyById(@PathVariable Long id,@RequestBody BuyDTO dto) throws IOException, InterruptedException {
        UserEntity user = getAuthenticatedUser();
        BuyDTO updated = masterService.modifyById(id, dto, user);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        UserEntity user = getAuthenticatedUser();
        masterService.delete(id, user);
        return ResponseEntity.ok().build();
    }

}
