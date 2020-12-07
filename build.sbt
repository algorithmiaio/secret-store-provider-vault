
name := "secret-store-provider-internal"

organization := "com.algorithmia"

description := "Algorithmia internal secret store module"

val algorithmiaS3Maven = "s3://s3-us-west-2.amazonaws.com/algorithmia-maven/repo"

libraryDependencies += "com.algorithmia" % "plugin-sdk" % "d7cd0f9fce4527673e6133b8d873a14134d2de95"

resolvers += "Algorithmia Maven" at algorithmiaS3Maven

publishMavenStyle := true
publishTo := Some("Algorithmia Maven" at algorithmiaS3Maven)

// Do not append Scala versions to generated artifacts
crossPaths := false

// Forbid including Scala related libraries
autoScalaLibrary := false

// version artifacts using Git
enablePlugins(GitVersioning)
