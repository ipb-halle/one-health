# Use a base image with Node.js pre-installed
FROM node:14-alpine

# Set the working directory inside the container
WORKDIR /usr/src/app

# Copy package.json and package-lock.json (if exists)
COPY ontology-manager-client/package*.json ./

# Install npm dependencies
RUN npm install

# Copy the rest of the application code
COPY ./ontology-manager-client/ .


# Command to run your application
CMD ["npm", "start"]
