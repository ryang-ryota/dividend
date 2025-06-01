package io.github.ryang_ryota.dividend.model;

import lombok.Data;

@Data
public class DividendRawResponse {
    private String stockCode;
    private String stockName;
    private Double actualDividend;
    private Double forecastDividend;
    private Double previousClose;
    private Double dividendYield;
}
