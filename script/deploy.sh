#!/bin/bash

# This script deploys to S3 debian repository.

DEB_CODENAME=$1

gem install deb-s3
./gradlew dist
mv build/dist/homeki_*_all.deb build/dist/homeki.deb
deb-s3 upload --endpoint s3-eu-west-1.amazonaws.com --access-key-id=$S3_ACCESS_KEY --secret-access-key=$S3_ACCESS_SECRET --codename $DEB_CODENAME --bucket repository.homeki.com build/dist/homeki.deb
