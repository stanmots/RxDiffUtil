#!/bin/bash
# This script will publish the project to Bintray via Travis C.

if [[ "$TRAVIS_TEST_RESULT" -eq 0 ]] && [[ -n "$TRAVIS_TAG" ]] && [[ "$TRAVIS_BRANCH" == 'master' && "$TRAVIS_PULL_REQUEST" == 'false' ]]; then
    echo "Pushing build to Bintray"
    ./gradlew bintrayUpload
fi