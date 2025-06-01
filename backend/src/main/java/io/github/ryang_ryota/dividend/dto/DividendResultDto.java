package io.github.ryang_ryota.dividend.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class DividendResultDto {
    private String stockCode;
    private String stockName;
    private Integer holdingCount;
    private Double previousClose;
    private Double dividendYield;
    private Double forecastDividend;
    private Double forecastAmount;
    private LocalDateTime updatedAt;
}
