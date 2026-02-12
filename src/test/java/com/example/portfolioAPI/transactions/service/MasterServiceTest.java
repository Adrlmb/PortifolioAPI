package com.example.portfolioAPI.transactions.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.SQLOutput;

@SpringBootTest
public class MasterServiceTest {

    @Autowired
    private MasterService masterService;

    @Test
    void deveBuscarCotacaoBTCBRL() throws Exception{
        BigDecimal bid = masterService.apiBid("BTC", "BRL");
        System.out.println("Cotação BTC-BRL: + " + bid);
        assertNotNull(bid);
        assertTrue(bid.compareTo(BigDecimal.ZERO) > 0);
    }
}
