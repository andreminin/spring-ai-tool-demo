#!/bin/bash


docker run --name pg-test \
  -e POSTGRES_DB=test \
  -e POSTGRES_USER=user \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d postgres:16

