lazy val root = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "Neptune",
    scalaVersion := "2.11.11",
    // This is an application with a main method
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1",
      "com.github.japgolly.scalacss" %%% "core" % "0.5.3"
    )
  )

