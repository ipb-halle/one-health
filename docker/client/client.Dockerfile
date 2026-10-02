# Use a base image with Node.js pre-installed
FROM node:22-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and yarn.lock
COPY client/package.json client/yarn.lock ./

# Install dependencies exactly from the committed Yarn lockfile
RUN yarn install --frozen-lockfile

# Copy the rest of the application code
COPY client/ .

# Build the React app
RUN yarn build

# Use Apache httpd for serving the production build
FROM httpd

# Copy the production build files from the build stage to the nginx web root directory
COPY --from=build /app/build /usr/local/apache2/htdocs
COPY docker/client/httpd-vhost.conf /usr/local/apache2/conf/extra/httpd-vhost.conf

RUN echo "Include conf/extra/httpd-vhost.conf" >> /usr/local/apache2/conf/httpd.conf