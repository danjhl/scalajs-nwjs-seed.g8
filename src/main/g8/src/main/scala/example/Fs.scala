package example

import scalajs.js
import js.annotation._

@js.native
@JSGlobal("fs")
object Fs extends js.Object {
  def readFile(
      file: String,
      encoding: String,
      callBack: js.Function2[String, String, Unit]): Unit = js.native
}