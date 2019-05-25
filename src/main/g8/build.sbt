// Projects

lazy val root = 
  project
    .in(file("."))
    .settings(appSettings)
    .enablePlugins(ScalaJSBundlerPlugin)

// Settings

lazy val appSettings =
  Seq(
    name                            := "$name$",
    scalaVersion                    := "2.12.8",
    version                         := "0.1.0-SNAPSHOT",
    organization                    := "com.example",
    organizationName                := "example",
    scalacOptions                   += "-P:scalajs:sjsDefinedByDefault",
    scalaJSUseMainModuleInitializer := true,
    version in webpack              := "4.29.6",
    watchSources                    += baseDirectory.value / "app",
    appDeps,
    appNpmDeps)

// Dependencies

lazy val appDeps = 
  libraryDependencies ++=
    Seq(
      Deps.laminar.value,
      Deps.scalaTest.value % Test)

lazy val appNpmDeps = 
  npmDependencies in Compile ++=
    Seq(
      Deps.npm.photonkit)

// Tasks

lazy val pack = taskKey[Unit]("package")
lazy val start = taskKey[Unit]("start app")
lazy val updateApp = taskKey[Unit]("update app")

pack := { Tasks.pack(name.value, scalaVersion.value, true) }
start := { Tasks.startApp(scalaVersion.value) }
updateApp := { Tasks.updateApp(scalaVersion.value) }

// Aliases

addCommandAlias("build", ";fastOptJS::webpack;pack;updateApp")