package com.example.portfolioAPI.entity;

import com.example.portfolioAPI.dto.CurrencyDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "FNW_CURRENCY")
public class CurrencyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String codein;
    private String bid;

    public CurrencyEntity(CurrencyDTO dto){
        BeanUtils.copyProperties(dto, this);
    }

}
