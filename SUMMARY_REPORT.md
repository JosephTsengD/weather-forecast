# 天氣預報 Android App — 專案摘要報告

> 本報告供面試官快速了解專案架構、實作重點與使用的工具

## 專案概述

一個完整的 Android 天氣預報應用程式，採用 **Clean Architecture + MVVM** 架構，使用 **Kotlin** 與傳統 View 系統開發。

### 核心功能
- 當前天氣顯示（溫度、濕度、風速、氣壓等）
- 7 天天氣預報（由 API 的 3 小時資料聚合為每日預報）
- 城市搜尋與選擇（OpenWeatherMap API）
- 離線支援（當前天氣快取）

## 技術架構

### 架構模式
- **Clean Architecture**
- **MVVM**：ViewModel 管理 UI 狀態，透過 LiveData 響應式更新 UI
- **模組化設計**：8 個獨立模組，職責單一，易於擴展

### 技術棧
- **語言**: Kotlin
- **UI**: View 系統 + XML 佈局、Material Design
- **依賴注入**: Koin 
- **網路**: Retrofit + OkHttp
- **資料庫**: Room
- **異步**: Kotlin Coroutines + Flow
- **版本管理**: Gradle 8.x，版本集中於 `gradle/libs.versions.toml`

### 模組結構
```
app/                      # 應用入口、Koin 配置
core/
├── core-common/         # Domain 層：模型、Repository 介面
├── core-network/        # 網路層：Retrofit、DTO、Mapper
├── core-database/       # 資料庫層：Room、Entity、Mapper
└── core-ui/            # UI 共用工具
data/
└── data-weather/        # Repository 實作（整合網路與資料庫）
feature/
├── feature-weather/    # 天氣功能：ViewModel、Fragment、Adapter
└── feature-city/      # 城市管理：ViewModel、Fragment、Adapter
```

## 核心實作

### 1. 資料流
- ViewModel 併發請求當前天氣與預報，減少總等待時間
- Repository 優先從網路獲取，失敗時回退到快取（當前天氣）
- 使用 `Result<T>` 型別封裝成功/失敗，強制錯誤處理

### 2. 資料聚合邏輯
- 將 API 回傳的 3 小時間隔資料聚合為每日預報
- 計算每天最高/最低溫，選擇中午時間點作為代表性天氣描述

### 3. 快取策略
- **當前天氣**：網路成功後寫入 Room，失敗時回退快取
- **預報資料**：不進行快取，每次從網路獲取最新資料

### 4. 錯誤處理
- 使用 `Result<T>` 型別，而非直接拋出 Exception
- ViewModel 將 `Result` 轉換為 `WeatherUiState`，UI 根據狀態顯示 Loading/Error/Success

## 使用的工具與協助

### AI 工具

#### Claude AI / Cursor AI
**使用場景**：
- **架構設計諮詢**：確認 Clean Architecture 最佳實踐、模組劃分建議
- **程式碼生成**：Repository 實作範例、ViewModel 狀態管理範例
- **文件撰寫**：技術文件與程式碼註解生成

**具體協助範例**：
1. **架構設計**：提供 Clean Architecture 與 MVVM 結合的範例結構
2. **Mapper 實作**：協助設計 DTO → Domain Model 轉換邏輯

**為什麼使用 AI 工具**：
- **加速開發**：減少查詢文件時間，快速獲得最佳實作建議
- **最佳實踐確認**：確保程式碼符合Google Android標準，避免常見錯誤

**使用原則**：
- AI 作為輔助工具，核心架構設計與業務邏輯決策, 實作由自己完成
- 先自行思考問題，再向 AI 確認最佳實踐，而非完全依賴，確保符合專案需求

### APK 位置
- `app/build/outputs/apk/debug/app-debug.apk`

## 成果總結
符合專題需求標準
✅ **完整架構實作**：Clean Architecture + MVVM + 模組化  
✅ **離線優先體驗**：當前天氣快取與優雅降級  
✅ **資料處理能力**：3 小時資料聚合為每日預報  
✅ **可維護性**：清晰的模組劃分，易於擴展
