# Use a base image with Node.js pre-installed
FROM node:18-alpine as build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json (if exists)
COPY ontology-manager-client/package*.json ./

# Install npm dependencies
RUN npm install

# Copy the rest of the application code
COPY ./ontology-manager-client/ .

COPY ./config/.env.production ./.env.production

# Build the React app
RUN npm run build


# Use Apache httpd as base image for serving the production build
FROM httpd:alpine

# Clean the original htdocs
RUN rm -rf /usr/local/apache2/htdocs/*

# Copy the production build files from the build stage to the nginx web root directory
COPY --from=build /app/build /usr/local/apache2/htdocs/

