#!/bin/bash

docker run --rm --name vault.secret_provider.it -d -p 8200:8200 \
  -e VAULT_DEV_ROOT_TOKEN_ID=root-token-integration vault

sleep 2





