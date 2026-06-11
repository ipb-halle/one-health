#!/usr/bin/env bash
#
# Copyright 2025 Leibniz-Institut f. Pflanzenbiochemie 
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
#
if [ -e ~/.onehealth ] ; then
    . ~/.onehealth
else
    echo 'Configuration file "~/.onehealth" is missing'
    echo 'Aborting ...'
    exit 1
fi

cd "$ONE_HEALTH_REPO"

case $1 in
    start)
        docker compose -f docker-compose.yml -f "$ONE_HEALTH_DATA/compose.override.yaml" up -d
        ;;
    stop)
        docker compose down --remove-orphans
        ;;
    restart)
        docker compose restart
        ;;
    *)
        echo "Usage: n1h.sh [start|stop|restart]"
        ;;
esac

