# homekicore

The core server providing the REST API and communication with various sensors and devices.
Read more on the [wiki](../../wiki).

## Compile and run

Prereqs needed:
 * vagrant, virtualbox
 * java

1. `git clone https://github.com/homeki/homekicore.git`
2. `cd homekicore`
3. `./gradlew jar`
4. `cd vagrant`
5. `vagrant up`
6. `./run-server.sh`

This will start the jar built through gradle on a vm. To debug, add the `debug` argument to `run-server.sh`
(attach a remote debugger to port 5050). The API will be exposed on port 5000 when the jar is running.

## Run the API tests

To run the API test, an instance of Homeki need to be started in test mode. Test mode clears
the database and performs some configuration changes to mock devices and such.

1. `cd homekicore`
2. `cd vagrant`
3. `run-test-server.sh` (`debug` parameter works just as for `run-server.sh`)
4. `cd ..`
5. `./gradlew build -DrunTests` (or run them from inside your editor)

## Build release

Publishing the release requires access to the Homeki debian repository.

1. `cd homekicore`
2. Edit `gradle.properties`, remove the `-SNAPSHOT` from the version.
3. `./gradlew dist`
4. Copy the `build/dist/homeki_*.deb` to the Homeki debian repository.
5. Edit `gradle.properties`, increment the version and re-add the `-SNAPSHOT` label.
6. Commit and push the version changes.

## Upload packages to debian repo

The packages must be signed with the Homeki gpg key when they are uploaded to the
debian repo. To check if the gpg is present, use `gpg --list-secret-keys`, make sure
`Homeki Development Team <contact@homeki.com>` exists.

If it does not exist, use `gpg --import <key>.asc` to import it.

Install [deb-s3](https://github.com/krobertson/deb-s3) and upload debian package with:

```
deb-s3 upload --endpoint s3-eu-west-1.amazonaws.com --access-key-id <id> --secret-access-key <secret> --codename <stable|unstable> --sign <gpg_key_id> --bucket repository.homeki.com --prefix packages <debfile>
```
