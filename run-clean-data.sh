docker run -d -v $(pwd)/data:/data --name one-health-cleanup ubuntu:latest tail -f /dev/null
docker exec one-health-cleanup rm -rf /data/neo4j_data
docker exec one-health-cleanup rm -rf /data/postgres_data
docker rm -f one-health-cleanup
