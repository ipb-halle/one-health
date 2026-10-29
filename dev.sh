#!/usr/bin/env bash
#
# Copyright 2026 Leibniz-Institut f. Pflanzenbiochemie
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
#======================================================================
#
# Run Docker Containers in Development Mode
# -----------------------------------------
#
# Use "dev.sh interactive" if you want to run the front end with "yarn start",
# otherwise choose between "dev.sh start" or "dev.sh debug" (or the other
# options). You should have a ".onehealth" file in your home directory, which
# defines at least the following variables:
#
# - ONE_HEALTH_DATA: path to the data directory
# - ONE_HEALTH_REPO: path to the repository root
# - ORCID_CLIENT_ID: ORCID application identifier (for ORCID authentication)
# - ORCID_CLIENT_SECRET: secret for the ORCID authenticator
#
# Further variables may override the default settings from the
# server/main/resources/application-*.properties files:
#
# - ORCID_REDIRECT_URI
# - FRONTEND_URL
#
p=`dirname $0`
cd "$p"

if [ -e ~/.onehealth ] ; then
    . ~/.onehealth
else
    echo 'Configuration file "~/.onehealth" is missing'
    echo 'Aborting ...'
    exit 1
fi

echo "Note: running in development mode with ports exposed!"
export SPRING_PROFILES_ACTIVE=dev

case $1 in
    interactive)
        docker compose -f docker-compose.yml -f compose-dev.yml up postgredb graphdb server
        ;;
    debug)
        docker compose -f docker-compose.yml -f compose-dev.yml up
        ;;
    server)
        docker compose -f docker-compose.yml -f compose-dev.yml up -d server
        ;;
    client)
        docker compose -f docker-compose.yml -f compose-dev.yml up -d client
        ;;
    start)
        docker compose -f docker-compose.yml -f compose-dev.yml up -d
        ;;
    stop)
        docker compose -f docker-compose.yml -f compose-dev.yml down
        ;;
    *)
        echo "Usage: dev.sh [start|stop|interactive|debug|server|client]"
        ;;
esac

