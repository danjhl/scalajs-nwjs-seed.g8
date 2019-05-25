package example

import com.raquo.laminar.api.L._
import org.scalajs.dom.document

object Main {
  def main(args: Array[String]): Unit = {
    documentEvents.onDomContentLoaded.foreach { _ =>

      render(document.getElementById("app-container"), MainPage.create())

    }(unsafeWindowOwner)
  }
}