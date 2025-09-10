@echo off
setlocal enabledelayedexpansion

:: Define paths
set SRC_DIR=..\src\main\java
set BIN_DIR=..\bin
set INPUT_FILE=input.txt
set EXPECTED_FILE=EXPECTED.TXT
set ACTUAL_FILE=ACTUAL.TXT

:: Create bin directory if it doesn't exist
if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

:: Compile all Java files recursively
set SOURCES=
for /r "%SRC_DIR%" %%f in (*.java) do (
    set SOURCES=!SOURCES! "%%f"
)
if not defined SOURCES (
    echo No Java source files found!
    exit /b 1
)
javac -d "%BIN_DIR%" !SOURCES!
if %errorlevel% neq 0 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)

:: Delete previous output
if exist "%ACTUAL_FILE%" del "%ACTUAL_FILE%"

:: Run the program
java -classpath "%BIN_DIR%" Duke < "%INPUT_FILE%" > "%ACTUAL_FILE%"

:: Compare the output to the expected output
fc "%ACTUAL_FILE%" "%EXPECTED_FILE%" > nul
if %errorlevel%==0 (
    echo Test result: PASSED
    exit /b 0
) else (
    echo Test result: FAILED
    exit /b 1
)