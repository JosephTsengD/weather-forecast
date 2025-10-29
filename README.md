# Weather Forecast App

一個現代化的 Android 天氣預報應用程式，使用 Kotlin、Clean Architecture 和View 系統開發。

<p align="center">
  <img src="https://github.com/user-attachments/assets/c4a771a6-948a-42ed-981d-05a96330b39b" height="420" />
  <img src="https://github.com/user-attachments/assets/3c9c5e58-5a58-4b08-ad56-535b5c70e8ed" height="420" />
</p>

## 功能特色

- 當前天氣顯示
- 7 天天氣預報
- 城市選擇和管理
- 離線支援與緩存
- Material Design 3 UI
- 城市搜尋功能
- 下拉刷新

## 技術棧

- **語言**: Kotlin
- **UI**: 傳統 View 系統 + XML 佈局
- **架構**: Clean Architecture + MVVM
- **依賴注入**: Koin
- **網路**: Retrofit2 + OkHttp3
- **數據庫**: Room
- **圖片載入**: Glide
- **異步**: Coroutines + Flow
 - **版本管理**: Gradle 8.x、版本由 `gradle/libs.versions.toml` 管理

## 專案結構

```
weather-forecast/
├── app/                           # 主應用模組
├── core/
│   ├── core-common/               # 共用工具類
│   ├── core-network/              # 網路層
│   ├── core-database/             # 數據庫層
│   └── core-utils/                   # 共用組件
├── feature/
│   ├── feature-weather/           # 天氣預報功能
│   └── feature-city/              # 城市管理功能
└── data/
    └── data-weather/              # 天氣數據層
```

更多細節請見：
- `SUMMARY_REPORT.md`（專案摘要報告）

## API 配置

本專案使用 OpenWeatherMap API 作為天氣數據來源：

1. 前往 [OpenWeatherMap](https://openweathermap.org/api) 註冊帳號
2. 取得免費 API Key
3. 將 API Key 配置到 `local.properties` 文件中

## 架構說明

### Clean Architecture
- **Presentation Layer**: Fragment + ViewModel + XML Layouts
- **Domain Layer**: Business Logic + Repository Interfaces
- **Data Layer**: Repository Implementation + API + Database

### 模組化設計
- 每個功能模組獨立開發和測試
- Core 模組提供共用功能
- Feature 模組實現具體業務邏輯

### 依賴注入
使用 Koin 進行依賴注入，簡化代碼結構：
- Network Module: Retrofit + OkHttp
- Database Module: Room Database
- Repository Module: Data Layer Implementation
- ViewModel Module: Presentation Layer

AI 工具使用

#### Claude AI (Anthropic) / Cursor AI
**使用場景**：
- **架構設計諮詢**：Clean Architecture 的最佳實踐、模組劃分建議
- **程式碼生成**：Repository 實作範例、ViewModel 狀態管理範例
- **錯誤診斷**：API Key 傳遞問題、Koin 依賴問題的除錯協助
- **文件撰寫**：技術文件與程式碼註解生成

## 測試

專案包含完整的測試覆蓋：
- 單元測試: ViewModel、Repository、Service
- 整合測試: API 和 Database
- UI 測試: Fragment 和 Activity

執行測試：
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## 建置

### 環境需求
- Android Studio Hedgehog (2023.1.1) 或更新版本
- JDK 17
- Android SDK 34
- Kotlin 1.9.24（以 `libs.versions.toml` 為準）

### 建置命令
```bash
# 建置 Debug 版本
./gradlew assembleDebug

# 建置 Release 版本
./gradlew assembleRelease

# 執行測試
./gradlew test

# 清理專案
./gradlew clean
```

## 開發指南

### 新增功能模組
1. 在 `feature/` 目錄下創建新模組
2. 配置 `build.gradle.kts`
3. 在 `settings.gradle.kts` 中添加模組
4. 創建對應的 Koin 模組

### 新增 API 端點
1. 在 `core-network` 模組中添加 API 介面
2. 定義對應的 DTO 類別
3. 實作數據映射函數
4. 在 Repository 中使用

### 新增數據庫表
1. 在 `core-database` 模組中定義 Entity
2. 創建對應的 DAO 介面
3. 更新 Database 類別
4. 實作數據映射函數

---

**開發者**: Joseph Tseng 曾少甫
**版本**: 1.0.0  
**最後更新**: 2025-10-29

# weather-forecast
