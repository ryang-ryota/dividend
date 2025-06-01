import React, { useEffect, useState } from "react";
import { DividendResult } from "./types";
import "./App.css";

const API_URL = "/api/dividend/actual/latest-year";
const SEARCH_API_URL = "/api/dividend/search/";

function App() {
  const [dividends, setDividends] = useState<DividendResult[]>([]);
  const [searchCode, setSearchCode] = useState("");
  const [holdingCount, setHoldingCount] = useState(100);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchDividends();
  }, []);

  const fetchDividends = async () => {
    setLoading(true);
    setError("");
    try {
      const res = await fetch(API_URL);
      if (!res.ok) throw new Error("一覧取得に失敗しました");
      const data = await res.json();
      setDividends(data);
    } catch (e: any) {
      setError(e.message);
    }
    setLoading(false);
  };

  const handleSearch = async () => {
    if (!searchCode) return;
    setError("");
    setLoading(true);
    try {
      const res = await fetch(
        `${SEARCH_API_URL}${encodeURIComponent(searchCode)}?holdingCount=${holdingCount}`
      );
      if (!res.ok) throw new Error("検索に失敗しました");
      const newResult = await res.json();
      setDividends((prev) => [newResult, ...prev]);
      setSearchCode("");
      setHoldingCount(100);
    } catch (e: any) {
      setError(e.message);
    }
    setLoading(false);
  };

  const handleHoldingCountChange = (stockCode: string, value: number) => {
    setDividends((prev) =>
      prev.map((item) =>
        item.stockCode === stockCode
          ? { ...item, holdingCount: value }
          : item
      )
    );
  };

  const handleDelete = async (stockCode: string) => {
    setLoading(true);
    setError("");
    try {
      const res = await fetch(`/api/dividend/${encodeURIComponent(stockCode)}`, {
        method: "DELETE",
      });
      if (!res.ok) throw new Error("削除に失敗しました");
      setDividends((prev) =>
        prev.filter((item) => item.stockCode !== stockCode)
      );
    } catch (e: any) {
      setError(e.message);
    }
    setLoading(false);
  };

  // ユーティリティ: 各種計算
  const getEvaluationAmount = (item: DividendResult) =>
    item.holdingCount && item.previousClose
      ? item.holdingCount * item.previousClose
      : 0;

  const getForecastDividend = (item: DividendResult) =>
    item.holdingCount && item.previousClose && item.dividendYield
      ? item.holdingCount * item.previousClose * item.dividendYield
      : 0;

  // 合計値
  const totalEvaluation = dividends.reduce(
    (sum, item) => sum + getEvaluationAmount(item),
    0
  );
  const totalForecast = dividends.reduce(
    (sum, item) => sum + getForecastDividend(item),
    0
  );
  const totalYield =
    totalEvaluation > 0 ? (totalForecast / totalEvaluation) * 100 : 0;

  return (
    <div className="container">
      <h1 className="title">配当金情報一覧</h1>
      <form
        className="search-bar neumorphism"
        onSubmit={(e) => {
          e.preventDefault();
          handleSearch();
        }}
      >
        <input
          type="text"
          placeholder="銘柄コードを入力"
          value={searchCode}
          onChange={(e) => setSearchCode(e.target.value)}
          className="input-code"
          disabled={loading}
        />
        <input
          type="number"
          min={1}
          placeholder="株数"
          value={holdingCount}
          onChange={(e) => setHoldingCount(Number(e.target.value))}
          className="input-shares"
          disabled={loading}
        />
        <button type="submit" className="search-btn" disabled={!searchCode || loading}>
          検索
        </button>
      </form>
      {error && <div className="error">{error}</div>}
      <div className="table-wrapper neumorphism">
        <table className="dividend-table">
          <thead>
            <tr>
              <th>銘柄コード</th>
              <th>銘柄名</th>
              <th>保有株数</th>
              <th>前営業日終値</th>
              <th>評価額</th>
              <th>予想配当利回り</th>
              <th>予想配当金額</th>
              <th>更新日時</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            {dividends.length === 0 ? (
              <tr>
                <td colSpan={9} style={{ textAlign: "center", color: "#888" }}>
                  データがありません
                </td>
              </tr>
            ) : (
              dividends.map((item) => (
                <tr key={item.stockCode}>
                  <td>{item.stockCode}</td>
                  <td>{item.stockName}</td>
                  <td>
                    <input
                      type="number"
                      min={1}
                      value={item.holdingCount || 0}
                      onChange={(e) =>
                        handleHoldingCountChange(item.stockCode, Number(e.target.value))
                      }
                      className="input-shares-row"
                      disabled={loading}
                    />
                  </td>
                  <td>
                    {item.previousClose !== null && item.previousClose !== undefined
                      ? item.previousClose.toLocaleString()
                      : "―"}
                  </td>
                  <td>
                    {getEvaluationAmount(item)
                      ? getEvaluationAmount(item).toLocaleString()
                      : "―"}
                  </td>
                  <td>
                    {item.dividendYield !== null && item.dividendYield !== undefined
                      ? (item.dividendYield * 100).toFixed(2) + "%"
                      : "―"}
                  </td>
                  <td>
                    {getForecastDividend(item)
                      ? Math.round(getForecastDividend(item)).toLocaleString()
                      : "―"}
                  </td>
                  <td>
                    {item.updatedAt
                      ? new Date(item.updatedAt).toLocaleString()
                      : "―"}
                  </td>
                  <td>
                    <button
                      className="delete-btn"
                      onClick={() => handleDelete(item.stockCode)}
                      disabled={loading}
                    >
                      削除
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
        <div className="total-row">
          <div>
            <span>合計評価額：</span>
            <strong>{Math.round(totalEvaluation).toLocaleString()} 円</strong>
          </div>
          <div>
            <span>合計予想配当金額：</span>
            <strong>{Math.round(totalForecast).toLocaleString()} 円</strong>
          </div>
          <div>
            <span>合計配当利回り：</span>
            <strong>{totalYield.toFixed(2)} %</strong>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
