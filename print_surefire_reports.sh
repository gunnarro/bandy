#!/usr/bin/env sh
echo "Current directory is $(pwd)"
echo "\n=== SUREFIRE REPORTS ===\n"

for testreport in target/surefire-reports/*.txt
do
    cat $testreport
done