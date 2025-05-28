package io.github.ryang_ryota.dividend.controller;

import io.github.ryang_ryota.dividend.service.DividendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dividend")
public class DividendController {
    private final DividendService dividendService;

    public DividendController(DividendService dividendService) {
        this.dividendService = dividendService;
    }

    @GetMapping("/{code}/annual/2025")
    public Double getAnnualDividend2025(@PathVariable String code) {
        return dividendService.getAnnualDividend2025(code);
    }
}
