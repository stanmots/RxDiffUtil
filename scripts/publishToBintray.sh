#!/bin/bash
# This script will publish the project to Bintray via Travis CI.

echo "Checking whether the artifacts should be published to Bintray"
echo "TRAVIS_TEST_RESULT is $TRAVIS_TEST_RESULT"
echo "TRAVIS_TAG is $TRAVIS_TAG"
echo "TRAVIS_BRANCH is $TRAVIS_BRANCH"
echo "TRAVIS_PULL_REQUEST is $TRAVIS_PULL_REQUEST"

# Do not check TRAVIS_BRANCH because of the bug in Travis
# see https://github.com/travis-ci/travis-ci/issues/4745
if [[ "$TRAVIS_TEST_RESULT" -eq 0 ]] && [[ -n "$TRAVIS_TAG" ]] && [[ "$TRAVIS_PULL_REQUEST" == 'false' ]]; then
    echo "Pushing to Bintray"
    ./gradlew bintrayUpload
else
echo "The artifacts won't be published"
fi