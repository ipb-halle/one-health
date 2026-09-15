#!/usr/bin/env bash
#
p=`realpath $0`
DIR=`dirname "$p"`
DIR=`realpath "$DIR/.."`

. "$DIR/etc/config.sh"
export NPROCS=`lscpu -J | jq '."lscpu"[] | select(."field" == "CPU(s):") | .data | tonumber'`
pushd $DIR/docker >/dev/null
docker build -f Dockerfile --build-arg=NPROCS=$NPROCS -t $BINGO_TAG .
popd >/dev/null
