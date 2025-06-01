CREATE TABLE IF NOT EXISTS dividend_search_result (
    id SERIAL PRIMARY KEY,
    stock_code VARCHAR(16) NOT NULL,
    stock_name VARCHAR(64) NOT NULL,
    holding_count INTEGER DEFAULT 0, -- 保有株数
    previous_close NUMERIC(12,4),   -- 前営業日終値
    dividend_yield NUMERIC(8,4);    -- 予想配当利回り
    actual_dividend NUMERIC(12,4),
    forecast_dividend NUMERIC(12,4),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_dividend_search_result_code_created
  ON dividend_search_result(stock_code, created_at DESC);
