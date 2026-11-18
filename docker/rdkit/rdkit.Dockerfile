# Stage 1: Build RDKit with PostgeSQL
ARG postgres_image_version=15
FROM docker.io/postgres:${postgres_image_version} AS builder

ARG postgres_version=15
ARG rdkit_git_url=https://github.com/rdkit/rdkit.git
ARG rdkit_git_ref=Release_2024_03_6

# GPG verification for postgresql-apt-repository
RUN apt-get update \
    && apt-get install -yq --no-install-recommends \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    && curl -fsSL https://www.postgresql.org/media/keys/ACCC4CF8.asc -o /etc/apt/trusted.gpg.d/postgresql.asc \    
    && chmod 644 /etc/apt/trusted.gpg.d/postgresql.asc \
    && echo "deb http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list

# Install build dependencies (unversioned boost)
RUN apt-get update \
    && apt-get install -yq --no-install-recommends \
    build-essential \
    cmake \
    git \
    libboost-iostreams-dev \
    libboost-program-options-dev \
    libboost-regex-dev \
    libboost-serialization-dev \
    libboost-system-dev \
    libeigen3-dev \
    libfreetype6-dev \
    postgresql-server-dev-${postgres_version}=$(postgres -V | awk '{print $3}')\* \        
    zlib1g-dev \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /opt/RDKit-build && chown postgres:postgres /opt/RDKit-build

USER postgres
WORKDIR /opt/RDKit-build

# Clone RDKit
RUN git clone ${rdkit_git_url}
WORKDIR /opt/RDKit-build/rdkit
RUN git checkout ${rdkit_git_ref}

RUN cmake \
    -D RDK_BUILD_CAIRO_SUPPORT=OFF \
    -D RDK_BUILD_INCHI_SUPPORT=ON \
    -D RDK_BUILD_AVALON_SUPPORT=ON \
    -D RDK_BUILD_PYTHON_WRAPPERS=OFF \
    -D RDK_BUILD_DESCRIPTORS3D=OFF \
    -D RDK_BUILD_FREESASA_SUPPORT=OFF \
    -D RDK_BUILD_COORDGEN_SUPPORT=ON \
    -D RDK_BUILD_MOLINTERCHANGE_SUPPORT=OFF \
    -D RDK_BUILD_YAEHMOP_SUPPORT=OFF \
    -D RDK_BUILD_STRUCTCHECKER_SUPPORT=OFF \
    -D RDK_USE_URF=OFF \
    -D RDK_BUILD_PGSQL=ON \
    -D RDK_PGSQL_STATIC=ON \
    -D PostgreSQL_CONFIG=pg_config \
    -D PostgreSQL_INCLUDE_DIR=`pg_config --includedir` \
    -D PostgreSQL_TYPE_INCLUDE_DIR=`pg_config --includedir-server` \
    -D PostgreSQL_LIBRARY_DIR=`pg_config --libdir` \
    -D RDK_INSTALL_INTREE=OFF \
    -D CMAKE_INSTALL_PREFIX=/opt/RDKit \
    -D CMAKE_BUILD_TYPE=Release \
    . 

RUN make -j4

USER root
WORKDIR /opt/RDKit-build/rdkit

RUN make install

RUN /bin/bash /opt/RDKit-build/rdkit/Code/PgSQL/rdkit/pgsql_install.sh

# Tests
USER postgres
WORKDIR /opt/RDKit-build/rdkit

RUN set -e; \
    export RDBASE="$PWD"; \
    export LD_LIBRARY_PATH="/opt/RDKit/lib:$PWD/lib"; \
    initdb -D /opt/RDKit-build/pgdata; \
    pg_ctl -D /opt/RDKit-build/pgdata -l /opt/RDKit-build/pgdata/log.txt start; \  
    trap 'pg_ctl -D /opt/RDKit-build/pgdata stop' EXIT; \
    psql -X -v ON_ERROR_STOP=1 -d postgres -c 'CREATE EXTENSION rdkit;'; \
    ctest -j4 --output-on-failure

# Stage 2: Final Image
ARG postgres_image_version=15
FROM docker.io/postgres:${postgres_image_version}

ARG postgres_version=15

# Install runtime dependencies (unversioned Boost)
RUN apt-get update \
    && apt-get install -yq --no-install-recommends \
    libboost-serialization1.83.0 \
    libfreetype6 \
    zlib1g \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*


# Copy built RDKit PostgreSQL extension files
COPY --from=builder /usr/share/postgresql/${postgres_version}/extension/*rdkit* /usr/share/postgresql/${postgres_version}/extension/
COPY --from=builder /usr/lib/postgresql/${postgres_version}/lib/rdkit.so /usr/lib/postgresql/${postgres_version}/lib/rdkit.so