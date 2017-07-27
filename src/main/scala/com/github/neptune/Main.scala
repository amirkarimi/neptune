package com.github.neptune

import org.scalajs.dom
import dom.{ document, window }
import dom.html._
import dom.raw.MouseEvent
import scala.scalajs.js.annotation.JSExportTopLevel
import scalacss.ProdDefaults._

@JSExportTopLevel("Neptune")
object Neptune {
  case class Action(icon: String, title: String, command: () => Unit)

  object Act {
    def apply(icon: String, title: String)(command: => Unit): Action = {
      Action.apply(icon, title, () => command)
    }
  }

  case class Settings(actions: Seq[Action] = actions, styleWithCss: Boolean = false)

  val actions = Seq(
    Act("<b>B</b>", "Bold") {
      exec("bold")
    },
    Act("<i>I</i>", "Italic") {
      exec("italic")
    },
    Act("<u>U</u>", "Underline") {
      exec("underline")
    },
    Act("<strike>S</strike>", "strikeThrough") {
      exec("strikeThrough")
    },
    Act("<b>H<sub>1</sub></b>", "Heading 1") {
      exec("formatBlock", "<H1>")
    },
    Act("<b>H<sub>2</sub></b>", "Heading 2") {
      exec("formatBlock", "<H2>")
    },
    Act("&#182;", "Paragraph") {
      exec("formatBlock", "<P>")
    },
    Act("&#8220; &#8221;", "Quote") {
      exec("formatBlock", "<BLOCKQUOTE>")
    },
    Act("&#35;", "Ordered List") {
      exec("insertOrderedList")
    },
    Act("&#8226;", "Unordered List") {
      exec("insertUnorderedList")
    },
    Act("&lt;/&gt;", "Code") {
      exec("formatBlock", "<PRE>")
    },
    Act("&#8213;", "Horizontal Line") {
      exec("insertHorizontalRule")
    },
    Act("&#128279;", "Link") {
      val url = window.prompt("Enter the link URL")
      if (url.nonEmpty) exec("createLink", url)
    },
    Act("&#128247;", "Image") {
      val url = window.prompt("Enter the image URL")
      if (url.nonEmpty) exec("insertImage", url)
    }
  )

  private def exec(command: String) = {
    document.execCommand(command, false, null)
  }

  private def exec(command: String, value: scalajs.js.Any) = {
    document.execCommand(command, false, value)
  }

  @JSExportTopLevel("neptune")
  def neptune(element: dom.Element, settings: Settings = Settings()): Html = {
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

    if (settings.styleWithCss) exec("styleWithCSS")

    content
  }

  val preventTab: (dom.KeyboardEvent => Any) = { e => 
    if (e.keyCode == 9) e.preventDefault()
  }
}

object Main {

  def main(args: Array[String]): Unit = {
    NeptuneStyles.addToDocument()
    // TODO
    Neptune.neptune(document.getElementById("my-editor"))
  }
}
