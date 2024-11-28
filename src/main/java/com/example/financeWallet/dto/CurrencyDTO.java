package com.example.financeWallet.dto;

import com.example.financeWallet.entity.CurrencyEntity;
import org.springframework.beans.BeanUtils;

public class CurrencyDTO {
    private Long id;
    private String code; // code of the crypto bought
    private String codein; // name of the crypto used to buy
    private String bid; // current price of crypto bought

    public CurrencyDTO(CurrencyEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }

    public CurrencyDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodein() {
        return codein;
    }

    public void setCodein(String codein) {
        this.codein = codein;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", codein='" + codein + '\'' +
                ", bid='" + bid + '\'' +
                '}';
    }
}
