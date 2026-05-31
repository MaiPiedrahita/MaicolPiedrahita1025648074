# ──────────────────────────────────────────────────────────────
# Script de conveniencia para ejecutar las pruebas en Windows
# con Java 25 y certificados SSL del sistema operativo.
#
# Uso:  .\run-tests.ps1
# ──────────────────────────────────────────────────────────────

$ErrorActionPreference = "Stop"

# Ruta al JDK 25 (ajustar si está instalado en otra ubicación)
$java25 = "C:\Program Files\Java\jdk-25.0.2"
if (-not (Test-Path "$java25\bin\java.exe")) {
    Write-Warning "JDK 25 no encontrado en $java25. Ajusta la variable `$java25 en este script."
    exit 1
}

$env:JAVA_HOME = $java25
$env:MAVEN_OPTS = "-Djavax.net.ssl.trustStoreType=Windows-ROOT"

Write-Host ""
Write-Host "=== Ejecutando pruebas con Java 25 ===" -ForegroundColor Cyan
Write-Host "JAVA_HOME = $env:JAVA_HOME"
Write-Host ""

mvn clean test --no-transfer-progress
