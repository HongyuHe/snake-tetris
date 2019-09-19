package snake.components

import javafx.collections.ObservableList
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, CheckBox, Label, RadioButton, Slider, SplitPane, ToggleGroup}
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

class StartPage extends JFXApp with Setting {
  var gameLevel: Int = 0
  var nrApples: Int = 1
  var nrBombs: Int = 0
  var twoPlayerMode: Boolean = false

  stage = new PrimaryStage {
    title = "Snake Game - Hongyu"
    scene = new Scene(400, 300) {

      val levelLabel = new Label("Set level:")
      val easyButton = new RadioButton("Easy")
      val normalButton = new RadioButton("Normal")
      val hellButton = new RadioButton("Hell")

      val levelGroup = new ToggleGroup
      levelGroup.toggles = List(easyButton, normalButton, hellButton)
      //      levelLabel.layoutX = 20
      //      levelLabel.layoutY = 50

      val twoPlayerBox = new CheckBox("Two player mode")

      val appleLabel = new Label("Number of apples:")
      val appleSlider = new Slider(1, 5, 1)
      val bombLabel = new Label("Number of Bombs:")
      val bombSlider = new Slider(0, 10, 0)
      val speedLabel = new Label("Set snake speed:")
      val speedSlider = new Slider(1, 10, 5)


      val startButton = new Button("Start")

      //-------------------------------- Action --------------------------------------//
      easyButton.onAction = { even =>
        if (easyButton.selected.apply()) gameLevel = 1
      }
      normalButton.onAction = { even =>
        if (normalButton.selected.apply()) gameLevel = 3
      }
      hellButton.onAction = { even =>
        if (hellButton.selected.apply()) gameLevel = 5
      }

      twoPlayerBox.onAction = { even =>
        twoPlayerMode = twoPlayerBox.selected.apply()
      }

      startButton.onAction = { even =>
        gameSpeed = speedSlider.getValue.toInt
        nrApples = appleSlider.getValue.toInt
        nrBombs = bombSlider.getValue.toInt
        println("Start!!!")
        close()
      }
      //-------------------------------- Content --------------------------------------//
      content = new VBox {
        padding = Insets(25)
        children = Seq(
          levelLabel,
          new HBox {
            padding = Insets(10)
            children = Seq(
              new HBox {
                prefWidth = 5
              },
              easyButton,
              normalButton,
              hellButton,
            )
          },

          new SplitPane(),
          new VBox {
            prefHeight = 75
            padding = Insets(10)
            children = Seq(
              appleLabel,
              appleSlider,
              bombLabel,
              bombSlider,
              speedLabel,
              speedSlider,
            )
          },
          twoPlayerBox,
          new VBox {
            prefHeight = 15
          },
          new SplitPane(),
          new VBox {
            prefHeight = 20
          },
          startButton
        )
      }
    }
  }
}