#!/bin/bash

# This script deploys to S3 debian repository.

set -e

DEB_CODENAME=$1

./gradlew dist

echo $GPG_PRIVATE_KEY | base64 --decode > homeki-private-gpg.asc
set +e
gpg --import homeki-private-gpg.asc
rm homeki-private-gpg.asc
set -e
mv build/dist/homeki_*_all.deb build/dist/homeki.deb
$BUILD_META_HOME/bundle/deb-s3 upload --endpoint s3-eu-west-1.amazonaws.com --sign $GPG_KEY_ID --access-key-id=$S3_ACCESS_KEY --secret-access-key=$S3_ACCESS_SECRET --codename $DEB_CODENAME --prefix packages --bucket repository.homeki.com build/dist/homeki.deb
