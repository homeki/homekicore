#!/bin/bash

PASSWORD=homeki

echo mysql-server mysql-server/root_password password $PASSWORD | sudo debconf-set-selections
echo mysql-server mysql-server/root_password_again password $PASSWORD | sudo debconf-set-selections