name := "riscv"

version := "0.1_snapshot_2"

scalaVersion := "2.12.6"

//crossScalaVersions := Seq( "2.11.11" )

scalacOptions ++= Seq( "-deprecation", "-feature", "-unchecked", "-language:postfixOps", "-language:implicitConversions", "-language:existentials" )

organization := "xyz.hyperreal"

//resolvers += Resolver.sonatypeRepo( "snapshots" )

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Hyperreal Repository" at "https://dl.bintray.com/edadma/maven"

libraryDependencies ++= Seq(
	"org.scalatest" %% "scalatest" % "3.0.0" % "test",
	"org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)

libraryDependencies ++= Seq(
//	"org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6",
//	"org.scala-lang.modules" %% "scala-xml" % "1.0.6"
	"org.scala-lang.modules" %% "scala-swing" % "2.0.2"
)

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.1"
)

libraryDependencies ++= Seq(
  "jline" % "jline" % "2.14.4"
)

coverageExcludedPackages := ".*Tests;.*CPU;.*IllegalCSR;.*Instruction;.*Compressed;.*package;Example;.*BDF;.*Emulator;.*StdIOChar;.*StdIOInt;.*StdIOHex;.*JLineInt;.*JLineHex;.*RNG;.*WriteOnlyDevice;.*ReadOnlyDevice;.*Device;.*RAM;.*ROM;.*Memory;.*Addressable;.*Video;.*Options;.*Main;.*Disk;.*Hexdump"

mainClass in (Compile, run) := Some( "xyz.hyperreal." + name.value.replace('-', '_') + ".Main" )

mainClass in assembly := Some( "xyz.hyperreal." + name.value.replace('-', '_') + ".Main" )

assemblyJarName in assembly := name.value + "-" + version.value + ".jar"

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

licenses := Seq("ISC" -> url("https://opensource.org/licenses/ISC"))

homepage := Some(url("https://github.com/edadma/" + name.value))

pomExtra :=
  <scm>
    <url>git@github.com:edadma/{name.value}.git</url>
    <connection>scm:git:git@github.com:edadma/{name.value}.git</connection>
  </scm>
  <developers>
    <developer>
      <id>edadma</id>
      <name>Edward A. Maxedon, Sr.</name>
      <url>https://github.com/edadma</url>
    </developer>
  </developers>
