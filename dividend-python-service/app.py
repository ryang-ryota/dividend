from fastapi import FastAPI
import yfinance as yf

app = FastAPI()

@app.get("/dividend/{code}")
def get_dividend_data(code: str):
    ticker = yf.Ticker(f"{code}.T")
    # 配当履歴（pandas.Series: index=日付, value=配当額）
    dividends = ticker.dividends
    # infoから現在株価と予想配当利回り
    info = ticker.info
    current_price = info.get("currentPrice")
    dividend_yield = info.get("dividendYield")
    # pandas.Seriesはそのまま返せないので、日付と値をlist化
    dividend_list = []
    if dividends is not None:
        dividend_list = [
            {"date": str(idx.date()), "amount": float(val)}
            for idx, val in dividends.items()
        ]
    return {
        "dividends": dividend_list,  # [{date: "YYYY-MM-DD", amount: float}, ...]
        "currentPrice": current_price,
        "dividendYield": dividend_yield
    }
