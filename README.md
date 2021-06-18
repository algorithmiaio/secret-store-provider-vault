secret-store-provider-vault
==============================

This module is the example of a Hashicorp Vault based secret provider implementation used by the
Algorithmia platform.

THIS PLUGIN IN UNSUPPORTED BY ALGORITHMIA AND INTENDED AS AN EXAMPLE ONLY

## Getting started

This secret provider modules can be added using the admin functionality for managing secret providers.

[Algorithmia Developers - Algorithm Secrets](https://algorithmia.com/developers/platform/algorithm-secrets)

## Requirements

To build this plugin the following must be installed:
* sbt 1.3.13 or later
* java 1.8 or later

As an example, see: Dockerfile.build

This can be executed as:
`docker build . -f Dockerfile.build -t plugin-dev && docker run -it --rm plugin-dev`

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

## How To Contribute

If you have proposed changes, feel free to open PRs.  However, only submit PRs with
code that can be freely released under the MIT license of this package.

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


