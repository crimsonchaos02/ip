#!/usr/bin/env bash


# compile the code into the bin folder, terminates if error occurred
SRC_DIR="../src/main/java/edith"
BIN_DIR="../bin"

rm -rf "$BIN_DIR"/*
mkdir -p "$BIN_DIR"

# create bin directory if it doesn't exist
mkdir -p "$BIN_DIR"

# compile all Java files recursively
find "$SRC_DIR" -name "*.java" > sources.txt
if ! javac -Xlint:none -d "$BIN_DIR" @sources.txt; then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

INPUT_FILE="input.txt"
EXPECTED_FILE="EXPECTED.TXT"
ACTUAL_FILE="ACTUAL.TXT"

# delete previous output
rm -f "$ACTUAL_FILE"

# run the program
java -classpath "$BIN_DIR" edith.Edith < "$INPUT_FILE" > "$ACTUAL_FILE"

# convert to UNIX format (if dos2unix exists)
cp "$EXPECTED_FILE" EXPECTED-UNIX.TXT
if command -v dos2unix >/dev/null 2>&1; then
    dos2unix "$ACTUAL_FILE"
    dos2unix EXPECTED-UNIX.TXT
fi

# compare the output to the expected output
if diff "$ACTUAL_FILE" EXPECTED-UNIX.TXT; then
    echo "Test result: PASSED"
    exit 0
else
    echo "Test result: FAILED"
    exit 1
fi