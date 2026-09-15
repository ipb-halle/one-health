#
#  CRIMSy Containers
#  Copyright 2021 Leibniz-Institut f. Pflanzenbiochemie
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
#====================================================================
#
# To build the image run "docker build -t pg17_bingo1.46.0 ." in this directory
#
#
# STAGE 0
#
FROM postgres:17-bookworm

# these statements must be on separate lines
ENV INDIGO_RELEASE=indigo-1.46.0
ENV BINGO_PG_VERSION=17
#
# install postgresql source repository
#
RUN set -x  \
 && echo "deb-src http://apt.postgresql.org/pub/repos/apt/ bookworm-pgdg main $PG_MAJOR" > /etc/apt/sources.list.d/pgdg-src.list \
 && apt-get update

#
# install tools (wget, cmake, ...)
# install development tools
# download indigo toolkit
# configure, build and install
# save artifacts in /opt/bingo for use in next stage

#  && apt-get install -y postgresql-plpython-$PG_MAJOR postgresql-plpython3-$PG_MAJOR \
#
RUN set -x \
  && mkdir -p /opt/bingo/setup /opt/bingo/lib \
  && cd /opt/bingo \
  && apt-get install -y --no-install-recommends ca-certificates wget cmake git libfreetype-dev libfontconfig-dev \
  && apt-get install -y --allow-downgrades --no-upgrade postgresql-server-dev-$PG_MAJOR "libpq5=$PG_MAJOR*" "libpq-dev=$PG_MAJOR*" \
  && apt-get build-dep -y --no-upgrade libpq-dev \
  && rm -rf /var/lib/apt/lists/* \
  && (ln -s  /usr/include/postgresql/$PG_MAJOR/server/libpq/md5.h  /usr/include/postgresql/$PG_MAJOR/server/common/md5.h \
    || echo "INFO: failed to create compatibility link - continuing") \
  && echo "INFO: Cloning Indigo toolkit" \
  && wget -O /opt/bingo/$INDIGO_RELEASE.tar.gz https://github.com/epam/Indigo/archive/refs/tags/$INDIGO_RELEASE.tar.gz \
  && tar -xzf $INDIGO_RELEASE.tar.gz

COPY bingo-pg-install.sh /opt/bingo/setup/

RUN set -x \
  && cd /opt/bingo/Indigo-$INDIGO_RELEASE \
  && if [ -z "$NPROC" ] ; then export NPROC=1 ; fi \
  && if [ -z "$BINGO_SCHEMA" ] ; then export BINGO_SCHEMA=bingo ; fi \
  && mkdir build \
  && cd build \
  && cmake .. -DBUILD_INDIGO=OFF -DBUILD_INDIGO_WRAPPERS=OFF -DBUILD_INDIGO_UTILS=OFF -DBUILD_BINGO=ON -DBUILD_BINGO_POSTGRES=ON -DBUILD_BINGO_ELASTIC=OFF \
  && cmake --build . --config Release --target all \
  && cp bin/libbingo-postgres.so /opt/bingo/lib \
  && cp -r ../bingo/postgres/sql /opt/bingo \
  && cd /opt/bingo \
  && /bin/sh ./setup/bingo-pg-install.sh -libdir /lib/postgresql/$PG_MAJOR/lib -y

#
#===================================================================
#
# STAGE 1
#
FROM postgres:17-bookworm
LABEL maintainer="Frank Broda <fbroda@ipb-halle.de>"

RUN mkdir -p /opt/bingo
COPY --from=0 /opt/bingo/bingo_install.sql /opt/bingo/
COPY --from=0 /opt/bingo/bingo_uninstall.sql /opt/bingo/
COPY --from=0 /opt/bingo/lib/libbingo-postgres.so /lib/postgresql/$PG_MAJOR/lib/

#
# Steps to finalize the installation:
#
# su postgres psql %DATABASE% -f /opt/bingo/bingo_install.sql
#
# GRANT USAGE ON SCHEMA bingo TO %ROLE%;
# GRANT SELECT ON bingo.bingo_config TO %ROLE%;
# GRANT SELECT ON bingo.bingo_tau_config TO %ROLE%;
#
#
