#!/bin/bash

cd ./application-log-api/

./mvnw package

cd ..

cd customer-api/

./mvnw package

cd ..

cd score-api/

./mvnw package

cd ..

cd authorization-server/

./mvnw package

cd ..
