package com.github.neptune

import org.scalajs.dom
import dom.document
import org.scalajs.dom.html.Html
import scala.scalajs.js.annotation.JSExportTopLevel

case class Action(icon: String, title: String, command: () => Unit)

object Action {
  def apply(icon: String, title: String)(command: => Unit) = {
    apply(icon, title, () => command)
  }
}

object Main {

  val actions = Seq(
    Action("<b>B</b>", "Bold") {
      exec("bold")
    },
    Action("<i>I</i>", "Italic") {
      exec("italic")
    },
    Action("<u>U</u>", "Underline") {
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
    val content = document.createElement("div").asInstanceOf[Html]
    content.contentEditable = "true"
    content.className = NeptuneStyles.neptuneContent.htmlClass
    content.onkeydown = preventTab
    element.appendChild(content)


    settings.actions.forEach(action => {
      const button = document.createElement('button')
      button.className = settings.classes.button
      button.innerHTML = action.icon
      button.title = action.title
      button.onclick = action.result
      actionbar.appendChild(button)
    })
  }

  val preventTab: (dom.KeyboardEvent => Any) = { e => 
    if (e.keyCode == 9) e.preventDefault()
  }

  def main(args: Array[String]): Unit = {
    neptune(document.getElementById("my-editor"))
  }
}
