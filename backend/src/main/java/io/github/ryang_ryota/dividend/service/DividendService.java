package io.github.ryang_ryota.dividend.service;

import io.github.ryang_ryota.dividend.dto.DividendResultDto;
import io.github.ryang_ryota.dividend.entity.DividendSearchResult;
import io.github.ryang_ryota.dividend.feign.DividendFeignClient;
import io.github.ryang_ryota.dividend.model.DividendRawResponse;
import io.github.ryang_ryota.dividend.repository.DividendSearchResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DividendService {

    private final DividendSearchResultRepository repository;
    private final DividendFeignClient feignClient;

    // 一覧取得
    public List<DividendResultDto> getLatestActualDividendsLastYear() {
        log.info("getLatestActualDividendsLastYear: Start");
        LocalDate today = LocalDate.now();
        log.debug("Today: {}", today);

        // うるう年2/29対応
        LocalDate oneYearAgo = today.minusYears(1);
        if (today.getMonthValue() == 2 && today.getDayOfMonth() == 29) {
            oneYearAgo = LocalDate.of(today.getYear() - 1, 2, 28);
        }
        log.debug("One year ago date: {}", oneYearAgo);

        LocalDateTime from = oneYearAgo.atStartOfDay();
        LocalDateTime to = today.atTime(23, 59, 59);
        log.debug("Search period - from: {}, to: {}", from, to);

        List<DividendSearchResult> latestResults = repository.findLatestPerStockCodeWithinPeriod(from, to);
        log.debug("Found {} results", latestResults.size());

        List<DividendResultDto> dtos = latestResults.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        log.info("getLatestActualDividendsLastYear: End - Returned {} records", dtos.size());
        return dtos;
    }

    // 検索＆追加
    @Transactional
    public DividendResultDto searchAndSave(String code, Integer holdingCount) {
        log.info("searchAndSave: Start - code: {}, holdingCount: {}", code, holdingCount);

        DividendRawResponse response = feignClient.getDividendRaw(code);
        log.debug("Received response from feign client: {}", response);

        DividendSearchResult entity = new DividendSearchResult();
        entity.setStockCode(response.getStockCode());
        entity.setStockName(response.getStockName());
        entity.setActualDividend(response.getActualDividend());
        entity.setForecastDividend(response.getForecastDividend());
        entity.setPreviousClose(response.getPreviousClose());
        entity.setDividendYield(response.getDividendYield());
        entity.setHoldingCount(holdingCount);

        DividendSearchResult savedEntity = repository.save(entity);
        log.debug("Saved entity: {}", savedEntity);

        DividendResultDto result = toDto(savedEntity);
        log.info("searchAndSave: End - Successfully saved and converted to DTO");
        return result;
    }

    // 保有株数の更新
    @Transactional
    public DividendResultDto updateHoldingCount(String code, Integer holdingCount) {
        log.info("updateHoldingCount: Start - code: {}, new holdingCount: {}", code, holdingCount);

        DividendSearchResult entity = repository.findLatestByStockCode(code);
        log.debug("Found entity: {}", entity);

        entity.setHoldingCount(holdingCount);
        DividendSearchResult savedEntity = repository.save(entity);
        log.debug("Updated entity: {}", savedEntity);

        DividendResultDto result = toDto(savedEntity);
        log.info("updateHoldingCount: End - Successfully updated holding count");
        return result;
    }

    // 削除
    @Transactional
    public void deleteByStockCode(String code) {
        log.info("deleteByStockCode: Start - code: {}", code);
        repository.deleteByStockCode(code);
        log.info("deleteByStockCode: End - Successfully deleted records for code: {}", code);
    }

    private DividendResultDto toDto(DividendSearchResult e) {
        log.debug("Converting entity to DTO: {}", e);
        Double forecastAmount = (e.getHoldingCount() != null && e.getForecastDividend() != null)
                ? e.getHoldingCount() * e.getForecastDividend()
                : null;
        log.debug("Calculated forecastAmount: {}", forecastAmount);

        DividendResultDto dto = new DividendResultDto(
                e.getStockCode(),
                e.getStockName(),
                e.getHoldingCount(),
                e.getPreviousClose(),
                e.getDividendYield(),
                e.getForecastDividend(),
                forecastAmount,
                e.getUpdatedAt()
        );
        log.debug("Created DTO: {}", dto);
        return dto;
    }
}