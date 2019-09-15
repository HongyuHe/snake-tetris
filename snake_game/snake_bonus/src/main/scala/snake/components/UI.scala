package snake.components

import javafx.collections.ObservableList
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, CheckBox, Label, RadioButton, ToggleGroup}
import scalafx.scene.layout.{BorderPane, HBox, VBox}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.Observable
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.paint.Color._
import scalafx.scene.paint.{LinearGradient, Stops}
import scalafx.scene.text.Text
import snake.game.Setting

class UI extends JFXApp with Setting {
  var level: Int = 0
  var twoPlayerMode: Boolean = false
  var speed: Double = 0

  stage = new PrimaryStage {
    title = "Snake Game - Hongyu"
    scene = new Scene(300, 300) {
      //      root = new BorderPane {
      //        padding = Insets(25)
      //        center = new Label("Hello There!")
      //      }
      val levelLabel = new Label("Set level:")
      val easyButton = new RadioButton("Easy")
      val normalButton = new RadioButton("Normal")
      val hellButton = new RadioButton("Hell")

      val levelGroup = new ToggleGroup
      levelGroup.toggles = List(easyButton, normalButton, hellButton)
//      levelLabel.layoutX = 20
//      levelLabel.layoutY = 50

      val twoPlayerBox = new CheckBox("Two player mode")

      val startButton = new Button("Start")



      twoPlayerBox.onAction = { even =>
        twoPlayerMode = twoPlayerBox.selected.apply()
      }
      startButton.onAction = { even =>
        println("Click!!!")
      }


//      startButton.layoutX = 500
//      startButton.layoutY = 250

      content = new VBox {
        padding = Insets(25)
        children = Seq(
          levelLabel,
          new HBox(
            easyButton,
            normalButton,
            hellButton,
          ),
          new VBox{prefHeight = 150},
          twoPlayerBox,
          new VBox{prefHeight = 30},
          startButton
        )
      }
      //      content = List(levelLabel, levelGroup)
    }
  }
}