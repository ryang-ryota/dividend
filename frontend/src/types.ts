export interface DividendResult {
    stockCode: string;
    stockName: string;
    holdingCount: number;
    previousClose: number | null;
    dividendYield: number | null;
    forecastDividend: number | null;
    updatedAt: string;
}
