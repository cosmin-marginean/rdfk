#!/usr/bin/env bash

set -e

VERSION=`cat version.properties | grep "version" | awk -F' *= *' '{print $2}'`
echo "Version is $VERSION"

rm -rf docs/dokka
export COVERALLS_REPO_TOKEN="${COVERALLS_REPO_TOKEN_RDF4K}"
./gradlew fullBuild dokkaHtml publish

git add --all
git commit -am "Release $VERSION"
git push
git tag "${VERSION}"
git push --tags --force

gh release create "${VERSION}" --verify-tag --title "RDF4K ${VERSION}" --notes "Version ${VERSION}"

echo "Finished building version $VERSION"
