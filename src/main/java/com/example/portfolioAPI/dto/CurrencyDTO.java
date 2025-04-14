package com.example.portfolioAPI.dto;

import com.example.portfolioAPI.entity.CurrencyEntity;
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

    public CurrencyDTO(CurrencyEntity entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
