#!/bin/bash

export $(grep -v '^#' .env | xargs)

mvn clean verify sonar:sonar -U \
  -Dsonar.projectKey=DietiEstates \
  -Dsonar.projectName='DietiEstates' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=$SONAR_TOKEN
