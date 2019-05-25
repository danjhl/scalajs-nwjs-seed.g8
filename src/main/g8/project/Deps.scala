import sbt._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Deps {
  lazy val scalaTest = Def.setting("org.scalatest" %%% "scalatest" % "3.0.5")
  lazy val laminar   = Def.setting("com.raquo"     %%% "laminar"   % "0.7")

  object npm {
    lazy val photonkit = "photonkit" -> "0.1.2"
  }

  val fileCopy = Seq(
    "photonkit/dist/css/photon.css"      -> "photon.css",
    "photonkit/fonts/photon-entypo.woff" -> "photon-entypo.woff",
  )
}