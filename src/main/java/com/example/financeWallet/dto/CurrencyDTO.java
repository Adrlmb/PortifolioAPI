package com.example.financeWallet.dto;

import com.example.financeWallet.entity.CurrencyEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyDTO {
    private Long id;
    private String code; // code of the crypto bought
    private String codein; // name of the crypto used to buy
    private String bid; // current price of crypto bought
    private BigDecimal profit;

    public CurrencyDTO(CurrencyEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }

    public BigDecimal getProfit() {
        BigDecimal currentValue = convertToBigDecimal(bid);
        BigDecimal buyPrice = BigDecimal.valueOf(5000);

        return currentValue.subtract(buyPrice).setScale(2, RoundingMode.DOWN);
    }

    private BigDecimal convertToBigDecimal(String value){
        try{
            return new BigDecimal(value);
        }catch (NumberFormatException | NullPointerException e){
            return BigDecimal.ZERO;
        }
    }
}
