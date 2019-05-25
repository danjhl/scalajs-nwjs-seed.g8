import sbt._
import scala.sys.process._
import java.nio.file.{Files, StandardOpenOption, Paths}

object Tasks {
  val sdk = true
  val platform = "linux-x64"
  val version = "0.38.3"

  val name = if (sdk) "nwjs-sdk" else "nwjs"
  val nwjsDir = s"\${name}-v\${version}-\${platform}"
  val nwjsZip = s"\${nwjsDir}.tar.gz"
  val downloadUrl = s"https://dl.nwjs.io/v\${version}/\${nwjsZip}"

  val outDir = "app"
  
  def pack(project: String, scalaVersion: String, debug: Boolean) {
    val scalaOut = scalaTarget(scalaVersion)
    val dest = scalaTarget(scalaVersion) + outDir
    val zipFile = dest + "/" + nwjsZip
    val bundler = s"\${scalaOut}/scalajs-bundler/main"
    
    if (!file(dest + "/" + nwjsZip).exists) {
      println("downloading nwjs...")
      (s"mkdir -p \${dest}" !)
      (url(downloadUrl) #> file(zipFile) !)
      (s"tar zxf \${zipFile} -C \${dest}" !)
    }
    else {
      println("already downloaded")
    }

    val files = (s"ls app").!!.split("\n")
    files.foreach { f => (s"cp -f app/\${f} \${dest}/\${nwjsDir}/\${f}" !) }

    (s"cp \${bundler}/\${project}-fastopt-bundle.js \${dest}/\${nwjsDir}/app.js" !)

    if (debug) {
      println("add reload script...")
      val script = reloadScript()
      Files.write(
        Paths.get(s"\${dest}/\${nwjsDir}/node.js"), 
        script.getBytes(), 
        StandardOpenOption.APPEND)
    }

    Deps.fileCopy.foreach { item =>
      (s"cp \${bundler}/node_modules/\${item._1} \${dest}/\${nwjsDir}/\${item._2}" !)
    }
  }

  def startApp(scalaVersion: String) {
    val scalaOut = scalaTarget(scalaVersion)
    val dest = scalaTarget(scalaVersion) + outDir
    Process(s"\${dest}/\${nwjsDir}/nw").run(ProcessLogger(_ => ()))
  }

  def updateApp(scalaVersion: String) {
    val scalaOut = scalaTarget(scalaVersion)
    val dest = scalaTarget(scalaVersion) + outDir
    (s"rm -f \${dest}/\${nwjsDir}/update" !)
    (s"touch \${dest}/\${nwjsDir}/update" !)
  }

  private def scalaTarget(scalaVersion: String) = {
    "target/scala-" + scalaVersion.split('.').take(2).mkString(".") + "/"
  }

  private def reloadScript() = {
    """
      |var fs = require('fs')
      |var path = require('path')
      |var filedir = path.dirname('.')
      |var filename = path.basename('update')
      |
      |fs.watch(filedir, function(evt, who) {
      |  if (evt === 'rename' && who === filename) {
      |      location.reload()
      |  }
      |})""".stripMargin
  }
}