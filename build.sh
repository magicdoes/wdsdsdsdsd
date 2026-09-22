#!/usr/bin/env bash
set -euo pipefail
mkdir -p build
mvn -q install:install-file -Dfile=baseline/MagicSMP-current.jar -DgroupId=com.bx -DartifactId=magicsmp-current -Dversion=1.0 -Dpackaging=jar
mvn -q -f guard/pom.xml clean package
mvn -q -f patcher/pom.xml clean package
java -jar patcher/target/patcher.jar baseline/MagicSMP-current.jar guard/target/classes build/MagicSMP.jar
