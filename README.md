[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=TesseractFR_TesseractCore&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=TesseractFR_TesseractCore)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=TesseractFR_TesseractCore&metric=bugs)](https://sonarcloud.io/summary/new_code?id=TesseractFR_TesseractCore)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=TesseractFR_TesseractCore&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=TesseractFR_TesseractCore)

# TesseractCore

TesseractCore is a plugin installed on all Tesseract servers to share common logic and data, like premium shop and
voting system.

## Available Services

Services are provided via TesseractLib's Service Container.

| Service          | Service class          | Description                                        |
|------------------|------------------------|----------------------------------------------------|
| Achievements     | AchievementService     | Give achievements to players                       |
| Boutique         | BoutiqueService        | Give payable items to players                      |
| Daily Connection | DailyConnectionService | Get information on a player's last connection date |
