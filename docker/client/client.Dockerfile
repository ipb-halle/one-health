# Use a base image with Node.js pre-installed
FROM node:22-slim AS build
# Set the working directory inside the container

WORKDIR /app

# Copy package.json and package-lock.json (if exists)
COPY client/package*.json ./

# Install npm dependencies
RUN npm install

# Copy the rest of the application code
COPY client/ .

# Build the React app
RUN npm run build


# Use Apache httpd for serving the production build
FROM httpd


# Copy the production build files from the build stage to the nginx web root directory
COPY --from=build /app/build /usr/local/apache2/htdocs
