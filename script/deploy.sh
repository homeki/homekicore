#!/bin/bash

# This script deploys to S3 debian repository.

set -e

DEB_CODENAME=$1

echo $GPG_PRIVATE_KEY | base64 --decode > homeki-private-gpg.asc
set +e
gpg --import homeki-private-gpg.asc
rm homeki-private-gpg.asc
set -e
DEB_FILENAME=`ls build/dist`
DEB_FULL_FILENAME=${DEB_FILENAME/.deb/_$DEB_CODENAME.deb}
mv build/dist/$DEB_FILENAME build/dist/$DEB_FULL_FILENAME
pushd script > /dev/null
bundle exec deb-s3 upload \
  --endpoint s3-eu-west-1.amazonaws.com \
  --sign $GPG_KEY_ID \
  --access-key-id $S3_ACCESS_KEY \
  --secret-access-key $S3_ACCESS_SECRET \
  --codename $DEB_CODENAME \
  --prefix packages \
  --origin homeki.com \
  --bucket repository.homeki.com \
  --suite $DEB_CODENAME \
  ../build/dist/$DEB_FULL_FILENAME
popd > /dev/null
