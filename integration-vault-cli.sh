#!/bin/bash


docker run -it --rm -e VAULT_ADDR='http://0.0.0.0:8200' -e VAULT_TOKEN='root-token-integration' --network host --entrypoint vault vault $*




