package com.github.neptune

import org.scalajs.dom
import dom.document
import org.scalajs.dom.html._
import org.scalajs.dom.raw.MouseEvent
import scala.scalajs.js.annotation.JSExportTopLevel
import scalacss.ProdDefaults._

case class Action(icon: String, title: String, command: () => Unit)

object Act {
  def apply(icon: String, title: String)(command: => Unit): Action = {
    Action.apply(icon, title, () => command)
  }
}

object Main {

  val actions = Seq(
    Act("<b>B</b>", "Bold") {
      exec("bold")
    },
    Act("<i>I</i>", "Italic") {
      exec("italic")
    },
    Act("<u>U</u>", "Underline") {
      exec("underline")
    }
  )

  private def exec(command: String) = {
    document.execCommand(command, false, null)
  }

  private def exec(command: String, value: scalajs.js.Any) = {
    document.execCommand(command, false, value)
  }


  @JSExportTopLevel("neptune")
  def neptune(element: dom.Element): Unit = {
    element match {
      case element: dom.html.Element => element.className = NeptuneStyles.neptune.htmlClass
      case other => // Don't know how to settup className for non-html element
    }

    val actionbar = document.createElement("div").asInstanceOf[Div]
    actionbar.className = NeptuneStyles.neptuneActionbar.htmlClass // TODO: settings.classes.actionbar
    element.appendChild(actionbar)

    val content = document.createElement("div").asInstanceOf[Html]
    content.contentEditable = "true"
    content.className = NeptuneStyles.neptuneContent.htmlClass
    content.onkeydown = preventTab
    element.appendChild(content)

    actions.foreach { action =>
      val button = document.createElement("button").asInstanceOf[Button]
      button.className = NeptuneStyles.neptuneButton.htmlClass // TODO: settings.classes.button
      button.innerHTML = action.icon
      button.title = action.title
      button.onclick = { (e: MouseEvent) => action.command() }
      actionbar.appendChild(button)
    }
  }

  val preventTab: (dom.KeyboardEvent => Any) = { e => 
    if (e.keyCode == 9) e.preventDefault()
  }

  def main(args: Array[String]): Unit = {
    NeptuneStyles.addToDocument()
    // TODO
    neptune(document.getElementById("my-editor"))
  }
}
