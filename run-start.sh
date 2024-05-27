echo "run-docker.sh: Starting containers"
docker run --network one-health-network --name one-health-graphdb -e NEO4J_AUTH=none -v /data/onehealth/neo4j_data:/data -d neo4j
docker run --network one-health-network --name one-health-postgredb -e  POSTGRES_USER=one_healt_huser -e  POSTGRES_PASSWORD=one_health_password -e  POSTGRES_DB=one_health -v /data/onehealth/postgre_data:/var/lib/postgresql/data -d rdkit-postgre
docker run --network one-health-network --name one-health-server -p 8080:8080 -d one-health-server
docker run --name one-health-client -p 3000:80 -d one-health-client
