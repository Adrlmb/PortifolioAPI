package com.example.financeWallet;

import com.example.financeWallet.dto.CurrencyDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class FinanceWalletApplication {

	public static void main(String[] args) throws IOException, InterruptedException {

		SpringApplication.run(FinanceWalletApplication.class, args);

	}

}
