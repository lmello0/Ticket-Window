#!/bin/bash

# package images
mvn clean package -Dmaven.test.skip

# build frontdoor
docker build -t lmello/ticket-window-frontdoor:latest ./frontdoor

# build attendant
docker build -t lmello/ticket-window-attendant:latest ./attendant

# build dashboard
docker build -t lmello/ticket-window-dashboard:latest ./dashboard
