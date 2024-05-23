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

docker run --network one-health-network --name one-health-graphdb -p 7474:7474 -p 7687:7687 -e NEO4J_AUTH=none -v /data/onehealth/data:/data -d neo4j

# Build the server and client images
echo "run-docker.sh: Building images"
docker build -t one-health-server:latest -f server.dockerfile --no-cache .
docker build -t one-health-client:latest -f client.dockerfile --no-cache .

# Run the containers
echo "run-docker.sh: Starting containers"
docker run --network one-health-network --name one-health-server -p 8080:8080 -d one-health-server
docker run --name one-health-client -p 3000:80 -d one-health-client

echo "====================================================================="
echo "run-docker.sh: Finishing script"
echo "====================================================================="