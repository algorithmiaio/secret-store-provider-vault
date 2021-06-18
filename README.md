secret-store-provider-vault
==============================

This module is the example of a Hashicorp Vault based secret provider implementation used by the
Algorithmia platform.

THIS PLUGIN IN UNSUPPORTED BY ALGORITHMIA AND INTENDED AS AN EXAMPLE ONLY

## Getting started

Secret provider modules can be added using the admin functionality for managing secret providers. 

## Building

`sbt assembly`

This will produce a JAR file at:
target/secret-store-provider-vault-assembly-<GIT_SHA>.jar

This can be uploaded to the Algorithmia Admin console as a secret provider.

## Configuration

This plugin requires the follow configuration settings:

* vault_addr - URL to the Vault host or cluster
* vault_token - Token that this plugin should use to access Vault
* vault_secret_path - Path prefix that this plugin should use to store tokens in Vault

## Testing

### Integration Testing

To do integration testing, first run:
`./integration-setup.sh`

This creates a dev vault with docker for the integration tests to run against.

Then:
`sbt compile test it:test`


To remove the container:
`./integration-teardown.sh`

To inspect the vault table:
`integration-vault-cli.sh`

Example:
`./integration-vault-cli.sh kv list /secret/algorithmia`



