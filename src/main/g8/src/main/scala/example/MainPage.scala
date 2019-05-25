package example

import com.raquo.laminar.api.L._
import com.raquo.laminar.setters.ChildrenCommandSetter.ChildrenCommand
import com.raquo.laminar.collection.CollectionCommand.Append
import org.scalajs.dom
import org.scalajs.dom.document

object MainPage {
  private val taskDiffBus = new EventBus[ChildrenCommand]

  def create(): HtmlElement = {
    div(cls("window"),
      div(cls("window-content"),
        div(cls("pane-group"),
          div(cls("pane pane-sm sidebar"),

            nav(cls("nav-group"),
              h5(cls("nav-group-title"), "Actions"),
              span(cls("nav-group-item"), span(cls("icon icon-window")), 
                "Hello scalajs",
                onClick.map(_ => Append(div("Hello scalajs"))) --> taskDiffBus),

              span(cls("nav-group-item"), span(cls("icon icon-drive")), 
                "Hello nodejs",
                onClick --> (readInfo _))
            ),

            nav(cls("nav-group"),
              h5(cls("nav-group-title"), "Favorites"),
              a(cls("nav-group-item active"), span(cls("icon icon-home")),
                "Home"),
              span(cls("nav-group-item"), span(cls("icon icon-download")), 
                "Downloads"),
              span(cls("nav-group-item"), span(cls("icon icon-signal")), 
                "Documents"),
              span(cls("nav-group-item"), span(cls("icon icon-cloud")), 
                "Applications")
            )
          ),
          div(cls("pane"),
            div(cls("padded-more"),
              div("Content"),
              children.command <-- taskDiffBus.events
            )
          )
        )
      )
    )
  }

  def readInfo(evt: dom.MouseEvent) = {
    Fs.readFile("info.txt", "utf8", { (error, data) => 
      taskDiffBus.writer.onNext(Append(div(data)))
    })
  }
}