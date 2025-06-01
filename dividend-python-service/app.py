from fastapi import FastAPI
import yfinance as yf
import traceback

app = FastAPI()

@app.get("/dividend/{code}")
def get_dividend_data(code: str):
    print(f"[INFO] リクエスト受信: code={code}")

    ticker_symbol = f"{code}.T"
    print(f"[INFO] yfinanceでティッカー取得: {ticker_symbol}")

    try:
        ticker = yf.Ticker(ticker_symbol)
        info = ticker.info
        print(f"[DEBUG] info取得: {info}")

        stock_name = info.get("shortName") or info.get("longName") or ""
        print(f"[DEBUG] 銘柄名: {stock_name}")

        previous_close = info.get("previousClose")
        print(f"[DEBUG] 前営業日終値: {previous_close}")

        dividend_yield = info.get("dividendYield")
        print(f"[DEBUG] 予想配当利回り（％表記）: {dividend_yield}")

        current_price = info.get("currentPrice")
        print(f"[DEBUG] 現在株価: {current_price}")

        # 実績配当金（過去1年分の合計）
        dividends = ticker.dividends
        print(f"[DEBUG] 配当履歴: {dividends}")

        actual_dividend = None
        if dividends is not None and not dividends.empty:
            actual_dividend = float(dividends[-365:].sum())
        print(f"[DEBUG] 実績配当金（直近1年合計）: {actual_dividend}")

        # 予想配当金（現株価×予想配当利回り）
        forecast_dividend = None
        if current_price is not None and dividend_yield is not None:
            forecast_dividend = float(current_price * dividend_yield)
        print(f"[DEBUG] 予想配当金: {forecast_dividend}")

        # 予想配当利回りを100で割って返す
        dividend_yield_fraction = None
        if dividend_yield is not None:
            dividend_yield_fraction = dividend_yield / 100 if dividend_yield > 1 else dividend_yield
        print(f"[DEBUG] 予想配当利回り（小数表記）: {dividend_yield_fraction}")

        result = {
            "stockCode": code,
            "stockName": stock_name,
            "actualDividend": actual_dividend,
            "forecastDividend": forecast_dividend,
            "previousClose": previous_close,
            "dividendYield": dividend_yield_fraction
        }
        print(f"[INFO] レスポンス: {result}")
        return result

    except Exception as e:
        print(f"[ERROR] データ取得中にエラー発生: {e}")
        traceback.print_exc()
        return {
            "stockCode": code,
            "stockName": "",
            "actualDividend": None,
            "forecastDividend": None,
            "previousClose": None,
            "dividendYield": None,
            "error": str(e)
        }
