#!/bin/bash

IMAGE_NAME="oleksiid/hivebuddy-image"
CONTAINER_NAME="hivebuddy-back"
REMOTE_VM="hivebuddy_back"

eval "$(ssh-agent -s)"
ssh-add ~/.ssh/personal_key

if ssh-add -l | grep -q "SHA256"; then
  echo "SSH key added successfully"
else
  echo "Failed to add SSH key"
  exit 1
fi

./gradlew bootJar

docker build -t $IMAGE_NAME .

docker push $IMAGE_NAME

ssh $REMOTE_VM << EOF
    docker pull $IMAGE_NAME
    docker stop $CONTAINER_NAME
    docker rm $CONTAINER_NAME
    docker run -d --name $CONTAINER_NAME -p 80:8080 $IMAGE_NAME
EOF

ssh-agent -k

echo "Deployed to remote server successfully"

