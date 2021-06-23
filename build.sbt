
name := "secret-store-provider-vault"

organization := "com.algorithmia"

description := "Algorithmia vault secret store module"

libraryDependencies += "com.algorithmia" % "plugin-sdk" % "1.0.0"
libraryDependencies += "com.bettercloud" % "vault-java-driver" % "5.1.0";

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies += "junit" % "junit" % "4.13.1" % "it,test",
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "it,test"
  )

// Do not append Scala versions to generated artifacts
crossPaths := false

// Forbid including Scala related libraries
autoScalaLibrary := false

// version artifacts using Git
enablePlugins(GitVersioning)
