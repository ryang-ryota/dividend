package io.github.ryang_ryota.dividend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DividendRawResponse {
    private List<DividendItem> dividends; // 配当履歴
    private Double currentPrice;
    private Double dividendYield;

    @Getter
    @Setter
    public static class DividendItem {
        private String date; // "YYYY-MM-DD"
        private Double amount;
    }
}
