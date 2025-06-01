package io.github.ryang_ryota.dividend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dividend_search_result")
@Getter
@Setter
public class DividendSearchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16)
    private String stockCode;

    @Column(nullable = false, length = 64)
    private String stockName;

    @Column
    private Double actualDividend;

    @Column
    private Double forecastDividend;

    @Column
    private Integer holdingCount;

    @Column
    private Double previousClose;

    @Column
    private Double dividendYield;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
