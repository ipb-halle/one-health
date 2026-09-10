#!/usr/bin/env bash
#
# Deploy the database schema to a container
#
p=`dirname $0`
DIR=`realpath $p/..`
CONTAINER=db

pushd $DIR 2>&1 >/dev/null
cat <<EOF | docker exec -i $CONTAINER su - postgres
cat << DATA | base64 -d | tar -xvf -
`tar -cf - schema/ | base64`
DATA
cat schema/0000_schema.sql | psql template1
EOF
popd 2>&1 >/dev/null
