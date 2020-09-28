#!/usr/bin/env bash
set -xe

projectRootDir=$PWD
pushd $projectRootDir
    ./gradlew clean build
    java -jar build/libs/book-service-0.0.1-SNAPSHOT.jar
popd
