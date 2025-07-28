@echo off
REM Script de ejecución para la migración de datos
REM SeguraTuAuto - Sistema de Migración

echo === SCRIPT DE MIGRACION DE DATOS - SEGURATUAUTO ===
echo.

REM Verificar que esté en el directorio correcto
if not exist "build.gradle" (
    echo Error: Debe ejecutar este script desde el directorio raiz del proyecto
    exit /b 1
)

REM Compilar el proyecto
echo Compilando el proyecto...
gradlew.bat compileJava

if errorlevel 1 (
    echo Error: No se pudo compilar el proyecto
    exit /b 1
)

echo Compilacion exitosa!
echo.

REM Ejecutar la migración
echo Ejecutando migracion de datos...

REM Construir el classpath
for /f "tokens=*" %%i in ('gradlew.bat -q printClasspath') do set CLASSPATH=%%i
set CLASSPATH=build\classes\java\main;build\resources\main;%CLASSPATH%

java -cp "%CLASSPATH%" com.seguratuauto.migration.main %*

echo.
echo Script de migracion completado.
pause
