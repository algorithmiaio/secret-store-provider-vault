secret-store-provider-internal
==============================

This module is the internal secret provider implementation used by the
Algorithmia platform.

## Getting started

Typically, secret provider modules should be added using the admin
functionality for managing secret providers.  Because of special needs
for the internal secret provider module, we will load it as a standard
dependency at build time.

```sbt
libraryDependencies += "com.algorithmia" % "secret-store-provider-internal" % "0.9.0"
```

## Publishing

### Maven
The following command can be used to publish a version based on SHA.

`sbt publish`

To publish a semantically versioned release, please follow the documentation
[here](https://docs.google.com/document/d/12uYiHsXNH8yGbaHF00aUwlTrkIQMh8gdTy-pITLT144/edit#heading=h.ake9tfj07vvi).

### Local
The following command can be used to publish to the local Ivy repository.

`sbt publishLocal`

## Testing

### Integration Testing

To do integration testing, first run:
`./integration-setup.sh`

This creates a mariadb with docker for the integration tests to run against.

Then:
`sbt compile test it:test`


To remove the container:
`./integration-teardown.sh`

To inspect the data table:
`integration-db-client.sh`


