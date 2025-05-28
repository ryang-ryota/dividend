package io.github.ryang_ryota.dividend.service;

import io.github.ryang_ryota.dividend.feign.DividendFeignClient;
import io.github.ryang_ryota.dividend.model.DividendRawResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DividendService {

    private final DividendFeignClient feignClient;

    public DividendService(DividendFeignClient feignClient) {
        this.feignClient = feignClient;
    }

    public Double getAnnualDividend2025(String code) {
        DividendRawResponse response = feignClient.getDividendRaw(code);
        List<DividendRawResponse.DividendItem> dividends = response.getDividends();

        // 2025年の配当実績合計
        double sum = dividends.stream()
                .filter(item -> item.getDate().startsWith("2025"))
                .mapToDouble(DividendRawResponse.DividendItem::getAmount)
                .sum();

        if (sum > 0) {
            return sum;
        } else if (response.getCurrentPrice() != null && response.getDividendYield() != null) {
            // 実績がなければ予想配当金（現株価×予想利回り）
            return response.getCurrentPrice() * response.getDividendYield();
        } else {
            return null; // 情報なし
        }
    }
}
