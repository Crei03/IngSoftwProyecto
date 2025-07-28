#!/bin/bash

# Script de ejecución para la migración de datos
# SeguraTuAuto - Sistema de Migración

echo "=== SCRIPT DE MIGRACIÓN DE DATOS - SEGURATUAUTO ==="
echo

# Verificar que esté en el directorio correcto
if [ ! -f "build.gradle" ]; then
    echo "Error: Debe ejecutar este script desde el directorio raíz del proyecto"
    exit 1
fi

# Compilar el proyecto
echo "Compilando el proyecto..."
./gradlew compileJava

if [ $? -ne 0 ]; then
    echo "Error: No se pudo compilar el proyecto"
    exit 1
fi

echo "Compilación exitosa!"
echo

# Ejecutar la migración
echo "Ejecutando migración de datos..."
java -cp "build/classes/java/main:build/resources/main:$(./gradlew -q printClasspath)" com.seguratuauto.migration.main $@

echo
echo "Script de migración completado."
