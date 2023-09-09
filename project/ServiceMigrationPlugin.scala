package sbttalon

import sbt._
import Keys._
import scalafix.sbt.ScalafixPlugin

/**
 * Migrates service code and configuration
 */
object ServiceMigrationPlugin extends AutoPlugin {
  override def trigger: PluginTrigger = allRequirements
  override def requires: Plugins = ScalafixPlugin

    object autoImport {
      val migrate: InputKey[Unit] = {
        inputKey[Unit]("Scalafix rule(s) to run in this project.")
      }
    }

  import autoImport._
  import ScalafixPlugin.autoImport._

  /**
   * these settings get included in the annotate project scope
   */
  override def projectSettings: Seq[Def.Setting[_]] = Seq(
    ThisBuild / semanticdbEnabled := true,
    ThisBuild / semanticdbVersion := scalafixSemanticdb.revision,
    ThisBuild / scalafixResolvers ++= Seq(
      coursierapi.MavenRepository.of("https://artifactory.corp.creditkarma.com:8443/artifactory/talon")
    ),
    migrate := Def.taskDyn {
      println("run a bunch of other things ")
      scalafix
        .toTask(" --triggered")
    }.value
    //    commands ++= Seq(scalafixCommand)
  )

  //  private val scalafixCommand = Command.args("migrate", "<Scalafix Rule(s)>") { (st: State, args: Seq[String]) => {
  //    println(args)
  //    if (args.nonEmpty)
  //      s"scalafix AkkaImporterRewrite" :: st
  //    else
  //      s"scalafix AkkaImporterRewrite" :: st
  //  }}
}
