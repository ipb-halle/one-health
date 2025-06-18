docker build -t one-health-server:latest -f server.dockerfile --no-cache .
docker build -t one-health-client:latest -f client.dockerfile --no-cache .

#
# Note to production environment:
# the files generated during client build need to be copied manually to the 
# proxy directory!
#

