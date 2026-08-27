#!/usr/bin/env bash
#
DIR=`dirname $0`

java -jar ${DIR}/target/curator-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
