echo "run-docker.sh: Stopping all the containers"
docker stop one-health-server
docker stop one-health-client
docker stop one-health-graphdb

# Remove all the containers
echo "run-docker.sh: Removing all the containers"
docker rm one-health-server
docker rm one-health-client
docker rm one-health-graphdb