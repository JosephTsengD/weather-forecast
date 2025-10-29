# Weather Forecast App

一個現代化的 Android 天氣預報應用程式，使用 Kotlin、Clean Architecture 和傳統 View 系統開發。

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
│   └── core-ui/                   # UI 共用組件
├── feature/
│   ├── feature-weather/           # 天氣預報功能
│   └── feature-city/              # 城市管理功能
└── data/
    └── data-weather/              # 天氣數據層
```

更多細節請見：
- `ARCHITECTURE.md`（架構與資料流）
- `PRODUCT_OVERVIEW.md`（產品邏輯與使用情境）
- `DEVELOPMENT_GUIDE.md`（開發、測試、故障排除）

## 開始使用

1. 克隆專案
2. 在 `local.properties` 中添加您的 OpenWeatherMap API Key：
   ```
   WEATHER_API_KEY=your_api_key_here
   ```
3. 建置並執行專案

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

## 貢獻指南

1. Fork 專案
2. 創建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交變更 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 開啟 Pull Request

## 授權

MIT License

## 聯絡資訊

如有問題或建議，請透過以下方式聯絡：
- 建立 Issue
- 發送 Pull Request
- 發送 Email

---

**開發者**: [Your Name]  
**版本**: 1.0.0  
**最後更新**: 2025-10-29

## 進一步閱讀

- `API_USAGE_GUIDE.md` - API 使用指南
- `DEBUG_GUIDE.md` - 調試指南
- `TEST_INSTRUCTIONS.md` - 完整測試指南
- `PROJECT_COMPLETION_REPORT.md` - 完成報告
- `AI_TOOLS_USAGE.md` - 本專案 AI 工具使用說明
# weather-forecast
