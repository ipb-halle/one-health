echo "====================================================================="
echo "run-docker.sh: Starting script"
echo "====================================================================="

# Stop all the containers 
echo "run-docker.sh: Stopping all the containers"
docker stop one-health-server
docker stop one-health-client
docker stop one-health-graphdb

# Remove all the containers
echo "run-docker.sh: Removing all the containers"
docker rm one-health-server
docker rm one-health-client
docker rm one-health-graphdb

# Build the server and client images
echo "run-docker.sh: Building images"
docker build -t rdkit-postgre:latest -f rdkit.dockerfile .
docker build -t one-health-server:latest -f server.dockerfile --no-cache .
docker build -t one-health-client:latest -f client.dockerfile --no-cache .

# Run the containers
echo "run-docker.sh: Starting containers"
docker run --network one-health-network --name one-health-graphdb -e NEO4J_AUTH=none -v /data/onehealth/neo4j_data:/data -d neo4j
docker run --network one-health-network --name one-health-postgredb -e  POSTGRES_USER=one_health_user -e  POSTGRES_PASSWORD=one_health_password -e  POSTGRES_DB: one_health -v /data/onehealth/postgre_data:/var/lib/postgresql/data -d rdkit-postgre
docker run --network one-health-network --name one-health-server -p 8080:8080 -d one-health-server
docker run --name one-health-client -p 3000:80 -d one-health-client

echo "====================================================================="
echo "run-docker.sh: Finishing script"
echo "====================================================================="