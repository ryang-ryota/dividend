package io.github.ryang_ryota.dividend.repository;

import io.github.ryang_ryota.dividend.entity.DividendSearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DividendSearchResultRepository extends JpaRepository<DividendSearchResult, Long> {

    @Query("SELECT d FROM DividendSearchResult d WHERE d.stockCode = :code ORDER BY d.createdAt DESC LIMIT 1")
    DividendSearchResult findLatestByStockCode(String code);

    List<DividendSearchResult> findByStockCodeOrderByCreatedAtDesc(String stockCode);

    // 銘柄ごとにcreatedAtが最大のレコードを1年以内で取得
    @Query("""
        SELECT d FROM DividendSearchResult d
        WHERE d.createdAt >= :from AND d.createdAt <= :to
          AND d.createdAt = (
              SELECT MAX(d2.createdAt) FROM DividendSearchResult d2
              WHERE d2.stockCode = d.stockCode AND d2.createdAt >= :from AND d2.createdAt <= :to
          )
    """)
    List<DividendSearchResult> findLatestPerStockCodeWithinPeriod(LocalDateTime from, LocalDateTime to);

    void deleteByStockCode(String stockCode);
}

