@echo off
echo Setting up Gradle wrapper...

REM Create gradle wrapper if it doesn't exist
if not exist "gradlew.bat" (
    echo Creating Gradle wrapper...
    gradle wrapper --gradle-version 8.4
    echo Gradle wrapper created successfully!
) else (
    echo Gradle wrapper already exists.
)

echo Setup complete!
pause
