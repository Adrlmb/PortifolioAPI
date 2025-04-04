package com.example.financeWallet.dto;

import com.example.financeWallet.entity.BuyEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyDTO {
    private Long id;
    private String code; // code of the crypto bought
    private String codein; // name of the crypto used to buy
    private String bid; // current price of crypto bought
    private String amountCryptoPurchased;
    private String amountSpent;
    private String taxCryptoCode;
    private String taxAmount;
    private String buyDate;
    private String exchange;
    private String profit;

    public BuyDTO(BuyEntity entity){
        BeanUtils.copyProperties(entity, this);
    }
}
