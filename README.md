secret-store-provider-vault
==============================

This module is the example of a Hashicorp Vault-based secret provider implementation used by the Algorithmia platform.

This plugin is unsupported by Algorithmia and intended as an example only.

## Getting started

This secret provider module can be added using Algorithmia's [admin functionality for managing secret providers](https://training.algorithmia.com/exploring-the-admin-panel/842511).

## Requirements

To build this plugin the following must be installed:
* sbt 1.3.13 or later
* Java 1.8 or later

As an example, see: `Dockerfile.build`, which can be executed as:

`docker build . -f Dockerfile.build -t plugin-dev && docker run -it --rm plugin-dev`

## Building

To build, run:
`sbt assembly`

This will produce a JAR file at:
`target/secret-store-provider-vault-assembly-<GIT_SHA>.jar`

This JAR file can then be [uploaded to Algorithmia as a secret provider module](https://training.algorithmia.com/exploring-the-admin-panel/842511) in the admin UI.

Rather than building, files from the releases may also be used:
[Releases](https://github.com/algorithmiaio/secret-store-provider-vault/releases)

## Configuration

This plugin requires the follow configuration settings:

* `vault_addr` - URL to the Vault host or cluster
* `vault_token` - Token that this plugin should use to access Vault
* `vault_secret_path` - Path prefix that this plugin should use to store tokens in Vault

## How to contribute

If you have proposed changes, feel free to open PRs. However, only submit PRs with
code that can be freely released under the MIT License of this package.

## Testing

### Integration testing

To do integration testing, first run:
`./integration-setup.sh`

This creates a dev vault with Docker for the integration tests to run against.

Then:
`sbt compile test it:test`

To remove the container:
`./integration-teardown.sh`

To inspect the vault table:
`integration-vault-cli.sh`

Example:
`./integration-vault-cli.sh kv list /secret/algorithmia`
