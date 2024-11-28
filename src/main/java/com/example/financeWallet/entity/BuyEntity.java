package com.example.financeWallet.entity;

import com.example.financeWallet.dto.BuyDTO;
import jakarta.persistence.*;
import org.springframework.beans.BeanUtils;

@Entity
@Table(name = "FNW_BUY")
public class BuyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CRYPTO_CODE") // crypto bought
    private String purchasedCryptoCode;

    @Column(name = "CRYPTO") // crypto used to buy
    private String cryptoUsed;

    @Column(name = "BUY_DATE")
    private String buyDate;

    @Column(name = "AMOUNT_PURCHASED")
    private String amountCryptoPurchased;

    @Column(name = "AMOUNT_SPENT")
    private String amountSpent; // amount used to buy crypto

    @Column(name = "TAX_CODE")
    private String taxCryptoCode;

    @Column(name = "TAX_AMOUNT")
    private String taxAmount;

    @Column(name = "EXCHANGE")
    private String exchange;

    public BuyEntity(BuyDTO dto){
        BeanUtils.copyProperties(dto, this);
    }

    public BuyEntity(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurchasedCryptoCode() {
        return purchasedCryptoCode;
    }

    public void setPurchasedCryptoCode(String purchasedCryptoCode) {
        this.purchasedCryptoCode = purchasedCryptoCode;
    }

    public String getCryptoUsed() {
        return cryptoUsed;
    }

    public void setCryptoUsed(String cryptoUsed) {
        this.cryptoUsed = cryptoUsed;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getAmountCryptoPurchased() {
        return amountCryptoPurchased;
    }

    public void setAmountCryptoPurchased(String amountCryptoPurchased) {
        this.amountCryptoPurchased = amountCryptoPurchased;
    }

    public String getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(String amountSpent) {
        this.amountSpent = amountSpent;
    }

    public String getTaxCryptoCode() {
        return taxCryptoCode;
    }

    public void setTaxCryptoCode(String taxCryptoCode) {
        this.taxCryptoCode = taxCryptoCode;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}
