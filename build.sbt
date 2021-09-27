lazy val root = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "Neptune",
    scalaVersion := "2.13.6",
    // This is an application with a main method
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.2.0",
      "com.github.japgolly.scalacss" %%% "core" % "0.7.0"
    )
  )

