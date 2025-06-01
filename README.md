# 配当金情報表示Webシステム

<div>
  <video controls src="https://github.com/user-attachments/assets/5bc25adf-d963-488e-bbb2-b62cced32d33" muted="false" style="max-width: 100%;"></video>
</div>

## 概要
- 学習目的で開発しています。
- 日本の上場株式の配当金情報（実績・予想）を表示するフルスタックWebアプリです。
- Spring Boot（Java）、FastAPI（Python）、React（TypeScript）をDocker Composeで統合しています。

## ディレクトリ構成
- `/backend` ... Spring Boot（APIゲートウェイ・ビジネスロジック）
- `/dividend-python-service` ... FastAPI（yfinanceで配当金データ取得）
- `/frontend` ... React（SPAフロントエンド）

## 起動方法

```
docker-compose build
docker-compose up
```

- React: http://localhost:3000
- Spring Boot: http://localhost:8080
- Python API: http://localhost:8000

## 開発技術
- Java 21, Spring Boot, Spring Cloud OpenFeign
- Python 3, FastAPI, yfinance
- React, TypeScript
- Docker, Docker Compose

## ライセンス
MIT License
