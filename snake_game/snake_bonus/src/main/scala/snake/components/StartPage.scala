package snake.components

import javafx.collections.ObservableList
import scalafx.application.{JFXApp, Platform}
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.application.JFXApp
import scalafx.application.JFXApp._
import scalafx.beans.Observable
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.effect.DropShadow
import scalafx.scene.paint.Color._
import scalafx.scene.paint.{LinearGradient, Stops}
import scalafx.scene.text.Text
import snake.game.Setting

class StartPage extends JFXApp with Setting {
//  new Alert(AlertType.Information, "Hello Dialogs!!!").showAndWait()

  var gameLevel: Int = 0
  var nrApples: Int = 1
  var nrBombs: Int = 0
  var twoPlayerMode: Boolean = false
  var battleWithAI: Boolean = false
  var startFlag: Boolean = false

  stage = new PrimaryStage {
    title = "Snake Game - Hongyu"
    scene = new Scene(500, 500) {

      val levelLabel = new Label("Set level:")
      val easyButton = new RadioButton("Easy")
      val normalButton = new RadioButton("Normal")
      val hellButton = new RadioButton("Hell")

      val levelGroup = new ToggleGroup
      levelGroup.toggles = List(easyButton, normalButton, hellButton)
      //      levelLabel.layoutX = 20
      //      levelLabel.layoutY = 50

      val twoPlayerBox = new CheckBox("Two-player mode")
      val battleWithAiBox = new CheckBox("Battle with AI")

//      val modeGroup = new ToggleGroup
//      modeGroup.toggles = List(twoPlayerMode, aiBox)

      val appleLabel = new Label("Number of apples:")
      val appleNum = new Label("1")
      val appleSlider = new Slider(1, 10, 1)

      val bombLabel = new Label("Number of Bombs:")
      val bombNum = new Label("0")
      val bombSlider = new Slider(0, 10, 0)

      val speedLabel = new Label("Set snake speed:")
      val speedValue = new Label("5")
      val speedSlider = new Slider(1, 10, 5)


      val startButton = new Button("Start")

      //-------------------------------- Action --------------------------------------//
      easyButton.onAction = { _ =>
        if (easyButton.selected.apply()) gameLevel = 1
      }
      normalButton.onAction = { _ =>
        if (normalButton.selected.apply()) gameLevel = 3
      }
      hellButton.onAction = { _ =>
        if (hellButton.selected.apply()) gameLevel = 5
      }

      appleSlider.onMouseDragged = { _ =>
//        println("Dragged")
        appleNum.setText(appleSlider.getValue.toInt.toString)
      }
      appleSlider.onMouseClicked = { _ =>
//        println("Click")
        appleNum.setText(appleSlider.getValue.toInt.toString)
      }
      bombSlider.onMouseDragged = { _ =>
        bombNum.setText(bombSlider.getValue.toInt.toString)
      }
      bombSlider.onMouseClicked = { _ =>
        bombNum.setText(bombSlider.getValue.toInt.toString)
      }
      speedSlider.onMouseDragged = { _ =>
        speedValue.setText(speedSlider.getValue.toInt.toString)
      }
      speedSlider.onMouseClicked = { _ =>
        speedValue.setText(speedSlider.getValue.toInt.toString)
      }

      twoPlayerBox.onAction = { _ =>
        twoPlayerMode = twoPlayerBox.selected.apply()
        if (!twoPlayerMode) battleWithAiBox.selected = false
//        battleWithAiBox.selected = !twoPlayerMode
//        battleWithAI  = !twoPlayerMode
      }
      battleWithAiBox.onAction = { _ =>
        if (!twoPlayerMode) {
          twoPlayerBox.selected = true
          twoPlayerMode = true
        }
        battleWithAI = battleWithAiBox.selected.apply()
//        else battleWithAI = battleWithAiBox.selected.apply()

//        twoPlayerBox.selected = false
//        twoPlayerMode = !battleWithAI
      }

      startButton.onAction = { _ =>
        gameSpeed = speedSlider.getValue.toInt
        nrApples = appleSlider.getValue.toInt
        nrBombs = bombSlider.getValue.toInt
        startFlag = true
        println("Start!!!")
        close()
      }
      //-------------------------------- Content --------------------------------------//
      content = new VBox {
        padding = Insets(50)
        children = Seq(
          levelLabel,
          new HBox {
            padding = Insets(20)
            children = Seq(
              new HBox {
                prefWidth = 5
              },
              easyButton,
              new HBox { prefWidth = 5 },
              normalButton,
              new HBox { prefWidth = 5 },
              hellButton,
            )
          },

          new SplitPane(),
          new VBox {
            prefHeight = 70
            padding = Insets(10)
            children = Seq(

              new HBox {
                padding = Insets(5)
                children = Seq(
                  appleLabel,
                  new HBox { prefWidth = 10 },
                  appleNum
                )
              },
              appleSlider,

              new HBox {
                padding = Insets(5)
                children = Seq(
                  bombLabel,
                  new HBox { prefWidth = 10 },
                  bombNum
                )
              },
              bombSlider,

              new HBox {
                padding = Insets(5)
                children = Seq(
                  speedLabel,
                  new HBox { prefWidth = 10 },
                  speedValue
                )
              },
              speedSlider,
            )
          },
          new HBox {
            padding = Insets(30)
            children = Seq(
              new HBox { prefWidth = 10 },
              twoPlayerBox,
              new HBox { prefWidth = 20 },
              battleWithAiBox,
            )
          },
          new VBox {
            prefHeight = 25
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