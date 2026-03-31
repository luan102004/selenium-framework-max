# selenium-framework-max (Lab 11)

[![Test Status](https://github.com/{YOUR_USERNAME}/selenium-framework-max/actions/workflows/full.yml/badge.svg)](https://github.com/{YOUR_USERNAME}/selenium-framework-max/actions)
[![Allure Report](https://img.shields.io/badge/Allure-Report-orange)](https://{YOUR_USERNAME}.github.io/selenium-framework-max/)

> **Thay `{YOUR_USERNAME}` bằng GitHub username của bạn sau khi push.**

## Chạy local
```bash
# Smoke test Chrome
mvn clean test -Dbrowser=chrome -Denv=dev -DsuiteXmlFile=src/test/resources/testng-smoke.xml

# Smoke test Firefox
mvn clean test -Dbrowser=firefox -Denv=dev -DsuiteXmlFile=src/test/resources/testng-smoke.xml

# Allure report
mvn allure:serve
```

## Bài 4: Selenium Grid
```bash
docker-compose up -d
mvn test -Dgrid.url=http://localhost:4444 -DsuiteXmlFile=src/test/resources/testng-grid.xml
docker-compose down
```

## Bài 3: Kiểm tra không hardcode password
```bash
grep -r 'secret_sauce' src/    # Kết quả phải RỖNG
grep -r 'standard_user' src/main/   # Kết quả phải RỖNG
```

## GitHub Secrets cần tạo
| Secret | Giá trị |
|--------|---------|
| `SAUCEDEMO_USERNAME` | `standard_user` |
| `SAUCEDEMO_PASSWORD` | `secret_sauce` |
