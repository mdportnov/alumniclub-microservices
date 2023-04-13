#!/bin/sh
docker ps -aq --filter name=alumni* | xargs docker stop