# Use a base image with Node.js pre-installed
FROM node:14-alpine as build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json (if exists)
COPY ontology-manager-client/package*.json ./

# Install npm dependencies
RUN npm install

# Copy the rest of the application code
COPY ./ontology-manager-client/ .

# Build the React app
RUN npm run build


# Use Nginx as the base image for serving the production build
FROM nginx:alpine


# Copy the production build files from the build stage to the nginx web root directory
COPY --from=build /app/build /usr/share/nginx/html

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
