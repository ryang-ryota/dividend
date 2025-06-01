package io.github.ryang_ryota.dividend.controller;

import io.github.ryang_ryota.dividend.dto.DividendResultDto;
import io.github.ryang_ryota.dividend.model.DividendRawResponse;
import io.github.ryang_ryota.dividend.entity.DividendSearchResult;
import io.github.ryang_ryota.dividend.service.DividendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dividend")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DividendController {

    private final DividendService dividendService;

    // 一覧取得
    @GetMapping("/actual/latest-year")
    public List<DividendResultDto> getLatestActualDividendsLastYear() {
        return dividendService.getLatestActualDividendsLastYear();
    }

    // 検索＆追加（株数も受け取る）
    @GetMapping("/search/{code}")
    public DividendResultDto searchAndSave(
            @PathVariable String code,
            @RequestParam(defaultValue = "100") Integer holdingCount) {
        return dividendService.searchAndSave(code, holdingCount);
    }

    // 保有株数の更新
    @PutMapping("/holding/{code}")
    public DividendResultDto updateHoldingCount(
            @PathVariable String code,
            @RequestParam Integer holdingCount) {
        return dividendService.updateHoldingCount(code, holdingCount);
    }

    // 削除
    @DeleteMapping("/{code}")
    public void deleteByStockCode(@PathVariable String code) {
        dividendService.deleteByStockCode(code);
    }
}
