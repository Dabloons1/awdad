#!/bin/bash

# Setup script for Gradle wrapper
echo "Setting up Gradle wrapper..."

# Create gradle wrapper if it doesn't exist
if [ ! -f "gradlew" ]; then
    echo "Creating Gradle wrapper..."
    gradle wrapper --gradle-version 8.4
    chmod +x ./gradlew
    echo "Gradle wrapper created successfully!"
else
    echo "Gradle wrapper already exists."
fi

echo "Setup complete!"
