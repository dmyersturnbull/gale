name := "gale"

description := "Gale language"
organization := "com.github.kokellab"
organizationHomepage := Some(url("https://github.com/kokellab"))
version := "0.1.0-SNAPSHOT"
isSnapshot := true
scalaVersion := "2.12.8"
publishMavenStyle := true
publishTo :=
	Some(if (isSnapshot.value)
		"Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
	else "Sonatype Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
	)
publishArtifact in Test := false
pomIncludeRepository := { _ => false }
javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:all")
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:postfixOps")
testOptions in Test += Tests.Argument("-oF")
homepage := Some(url("https://github.com/kokellab/gale"))
licenses := Seq("Apache Software License, Version 2.0"  -> url("https://www.apache.org/licenses/LICENSE-2.0"))
developers := List(Developer("dmyersturnbull", "Douglas Myers-Turnbull", "dmyersturnbull@kokellab.com", url("https://github.com/dmyersturnbull")))
startYear := Some(2016)
scmInfo := Some(ScmInfo(url("https://github.com/kokellab/gale"), "https://github.com/kokellab/gale.git"))
libraryDependencies ++= Seq(
	"com.typesafe" % "config" % "1.3.3",
	"com.google.guava" % "guava" % "27.1-jre",
	"org.slf4j" % "slf4j-api" % "1.8.0-beta4",
	"com.github.scopt" %% "scopt" % "4.0.0-RC2",
	"com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
	"org.scalanlp" %% "breeze" % "1.0-RC2",
	"org.parboiled" %% "parboiled" % "2.1.5",
	"org.typelevel"  %% "squants"  % "1.3.0",
"org.scalatest" %% "scalatest" % "3.1.0-RC1" % "test",
"org.scalactic" %% "scalactic" % "3.1.0-RC1" % "test",
"org.scalacheck" %% "scalacheck" % "1.14.0" % "test"
)
pomExtra :=
	<issueManagement>
		<system>Github</system>
		<url>https://github.com/kokellab/skale/issues</url>
	</issueManagement>
