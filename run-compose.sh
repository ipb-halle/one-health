echo "====================================================================="
echo "run-compose.sh: Starting script"
echo "====================================================================="

echo "run-compose.sh: Running update script"
chmod +x run-update.sh
./run-update.sh

echo "run-compose.sh: Removing all the containers"
docker compose rm -s -f

echo "run-compose.sh: Building the server and client images"
docker compose build --no-cache

echo "run-compose.sh: Runing the containers"
docker compose up

echo "====================================================================="
echo "run-compose.sh: Finishing script"
echo "====================================================================="