package tetris.game

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Alert, Button, ButtonType, SplitPane}
import scalafx.scene.control.Alert._
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint.{LinearGradient, Stops}
import scalafx.scene.text.Text

class ManualPage extends JFXApp {

  stage = new PrimaryStage {
    title = "Tetris Game - Hongyu"
    scene = new Scene {
      fill = Black

      val startButton = new Button(
        text = "Play"
      )
      startButton.resize(500, 50)
      startButton.onAction = { _ =>
        println("Start!!!")
        close()
      }

      content = new VBox {
        padding = Insets(20)
        children = Seq(
          new Text {
            text = "Welcome to "
            style = "-fx-font-size: 28pt"
            fill = new LinearGradient(
              endX = 0,
              stops = Stops(PaleGreen, SeaGreen))
          },
          new Text {
            text = "Tetris Game \uD83C\uDC76"
            style = "-fx-font-size: 48pt"
            fill = new LinearGradient(
              endX = 0,
              stops = Stops(Cyan, DodgerBlue)
            )
            effect = new DropShadow {
              color = DodgerBlue
              radius = 25
              spread = 0.25
            }
          },

          new VBox {
            prefHeight = 25
          },

          new SplitPane(),

          new VBox {
            prefHeight = 25
          },

          new Text {
            text = "Keyboard \n  ♦ Rotate : A, S, \uD83E\uDC11 \n  ♦ Move : \uD83E\uDC10, \uD83E\uDC12, \uD83E\uDC13 \n  ♦ Hard Drop: SPACE\n  ♦ Hold: W  "
            style = "-fx-font-size: 18pt"
            fill = FloralWhite
            effect = new DropShadow {
              color = FloralWhite
              radius = 25
              spread = 0.15
            }
          },
          new VBox {
            prefHeight = 25
          },
          startButton
        )
      }
    }
  }
}

