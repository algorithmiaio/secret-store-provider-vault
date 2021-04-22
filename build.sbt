
name := "secret-store-provider-vault"

organization := "com.algorithmia"

description := "Algorithmia vault secret store module"


val algorithmiaS3Maven = "s3://s3-us-west-2.amazonaws.com/algorithmia-maven/repo"

libraryDependencies += "com.algorithmia" % "plugin-sdk" % "4b49076258af71767f502f1849839b60879ef8f5"
libraryDependencies += "com.bettercloud" % "vault-java-driver" % "5.1.0";


resolvers += "Algorithmia Maven" at algorithmiaS3Maven

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies += "junit" % "junit" % "4.13.1" % "it,test",
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "it,test"
  )
  


publishMavenStyle := true
publishTo := Some("Algorithmia Maven" at algorithmiaS3Maven)

// Do not append Scala versions to generated artifacts
crossPaths := false

// Forbid including Scala related libraries
autoScalaLibrary := false

// version artifacts using Git
enablePlugins(GitVersioning)
