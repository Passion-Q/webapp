@echo off
chcp 65001 >nul
echo ================================================
echo    公文管理系统 - 启动脚本
echo ================================================
echo.
echo 数据库配置 (默认值):
echo   - 地址: localhost:3306
echo   - 数据库: document_db
echo   - 用户: root
echo   - 密码: 102331
echo.
echo 如需修改配置，请编辑 application.yml 文件
echo.
echo 启动中...
echo ================================================
echo.

cd /d "%~dp0"
.\mvnw.cmd clean spring-boot:run

pause