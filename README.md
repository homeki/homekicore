homekicore
==========
The core server providing the REST API and communication with various sensors and devices.
Read more on the [wiki](../../wiki).

Compile and run
---------------
Prereqs needed:
 * vagrant and virtualbox
 * java

1. `git clone https://github.com/homeki/homekicore.git`
2. `cd homekicore`
3. `./gradlew jar`
4. `cd vagrant`
5. `vagrant up`
6. `./run.sh`

This will start the jar built through gradle on a vm. To debug, use `run-debug.sh` instead of `run.sh`
(attach a remote debugger to port 5050). The API will be exposed on port 5000 when the jar is running.

