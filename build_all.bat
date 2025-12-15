@echo off
echo ==================================================
echo      Building Online Shopping Microservices
echo ==================================================

echo Building entire project (Multi-module)...
call mvn clean install

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Build Failed! Please check the logs above.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ==================================================
echo           Build Successful!
echo ==================================================
echo.
echo You can now run the application using: docker-compose up --build
pause
