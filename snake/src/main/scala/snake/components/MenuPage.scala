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

class MenuPage extends JFXApp with Setting {

  var gameLevel: Int = 0
  var nrApples: Int = 1
  var nrBombs: Int = 0
  timer = 16
  var twoPlayerMode: Boolean = false
  var battleWithAI: Boolean = false
  var startFlag: Boolean = false

  stage = new PrimaryStage {
    title = "Snake Game - Hongyu"
    scene = new Scene(510, 650) {

      fill = YellowGreen

      val levelLabel = new Label(" Choose level:")
      val easyButton = new RadioButton("")
      val normalButton = new RadioButton("")
      val hellButton = new RadioButton("")

      val levelGroup = new ToggleGroup
      levelGroup.toggles = List(easyButton, normalButton, hellButton)
      //      levelLabel.layoutX = 20
      //      levelLabel.layoutY = 50

      val twoPlayerBox = new CheckBox("")
      val battleWithAiBox = new CheckBox("")

//      val modeGroup = new ToggleGroup
//      modeGroup.toggles = List(twoPlayerMode, aiBox)

      val appleLabel = new Label("Number of apples:")
      val appleNum = new Label("1")
      val appleSlider = new Slider(1, 5, 1)

      val bombLabel = new Label("Number of Bombs:")
      val bombNum = new Label("3")
      val bombSlider = new Slider(0, 20, 3)

      val speedLabel = new Label("Set snake speed:")
      val speedValue = new Label("5")
      val speedSlider = new Slider(1, 10, 5)

      val timerValue = new Label("16")
      val timerSlider = new Slider(16, 60, 16)

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
      timerSlider.onMouseDragged = { _ =>
        timerValue.setText(timerSlider.getValue.toInt.toString)
      }
      timerSlider.onMouseClicked = { _ =>
        timerValue.setText(timerSlider.getValue.toInt.toString)
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
        timer = timerSlider.getValue.toInt
        startFlag = true
        println("Start!!!")
        close()
      }
      //-------------------------------- Content --------------------------------------//
      content = new VBox {
        padding = Insets(30)
        children = Seq(
          new Text {
            text = "Welcome to "
            style = "-fx-font-size: 28pt"
            fill = new LinearGradient(
              endX = 0,
            stops = Stops(Coral, Chocolate))
          },
          new Text {
            text = "Snake Game \uD83D\uDC0D"
            style = "-fx-font-size: 48pt"
            fill = new LinearGradient(
              endX = 0,
              stops = Stops(Brown, SaddleBrown)

            )
            effect = new DropShadow {
              color = WhiteSmoke
              radius = 25
              spread = 0.25
            }
          },
          new VBox {
            prefHeight = 25
          },
          new Text {
            text = " ▁ ▂ ▃ ▄ ▅ ▆ ▇ █ Choose level █ ▇ ▆ ▅ ▄ ▃ ▂ ▁   "
            style = "-fx-font-size: 14pt"
            fill = Black
          },
          new HBox {
            padding = Insets(20)
            children = Seq(
              new HBox {
                prefWidth = 5
              },
              new HBox { prefWidth = 10 },
              new Text {
                text = "\uD83D\uDE46 Easy "
                style = "-fx-font-size: 14pt"
                fill = Black
              },
              easyButton,
              new HBox { prefWidth = 5 },
              new HBox { prefWidth = 10 },
              new Text {
                text = "\uD83D\uDE47 Nomal "
                style = "-fx-font-size: 14pt"
                fill = Black
              },
              normalButton,
              new HBox { prefWidth = 5 },
              new HBox { prefWidth = 10 },
              new Text {
                text = "\uD83D\uDC80 Hell "
                style = "-fx-font-size: 14pt"
                fill = Black
              },
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
                  new Text {
                    text = "\uD83C\uDF4E Number of apples:"
                    style = "-fx-font-size: 14pt"
                    fill = Black
                  },
                  new HBox { prefWidth = 10 },
                  appleNum
                )
              },
              appleSlider,

              new HBox {
                padding = Insets(5)
                children = Seq(
                  new Text {
                    text = "\uD83D\uDCA3 Number of bombs: "
                    style = "-fx-font-size: 14pt"
                    fill = Black
                  },
                  new HBox { prefWidth = 10 },
                  bombNum
                )
              },
              bombSlider,

              new HBox {
                padding = Insets(5)
                children = Seq(
                  new Text {
                    text = "\uD83D\uDD50 Timer (second):"
                    style = "-fx-font-size: 14pt"
                    fill = Black
                  },
                  new HBox { prefWidth = 10 },
                  timerValue
                )
              },
              timerSlider,

              new HBox {
                padding = Insets(5)
                children = Seq(
                  new Text {
                    text = "\uD83D\uDE80 Snake speed:"
                    style = "-fx-font-size: 14pt"
                    fill = Black
                  },
                  new HBox { prefWidth = 10 },
                  speedValue
                )
              },
              speedSlider,
            )
          },
          new HBox {
            padding = Insets(5)
            children = Seq(
              new HBox { prefWidth = 10 },
              new Text {
                text = "\uD83D\uDC6B Two player mode "
                style = "-fx-font-size: 14pt"
                fill = Black
              },
              twoPlayerBox,
              new HBox { prefWidth = 20 },
              new Text {
                text = "\uD83E\uDD16 Battle with AI "
                style = "-fx-font-size: 14pt"
                fill = Black
              },
              battleWithAiBox,
            )
          },
          new VBox {
            prefHeight = 10
          },
          new SplitPane(),
          new VBox {
            prefHeight = 25
          },
          startButton
        )
      }
    }
  }
}