package me.roitgrund.pawned.desktop

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.awt.image.BufferedImage
import java.util.concurrent.atomic.AtomicReference
import javax.imageio.ImageIO
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.UIManager
import kotlinx.coroutines.*
import me.roitgrund.pawned.Coord
import me.roitgrund.pawned.Game
import me.roitgrund.pawned.api.Color
import me.roitgrund.pawned.api.Pawned
import me.roitgrund.pawned.api.PieceType
import org.slf4j.LoggerFactory

fun main() {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
  createAndShowGUI()
}

private fun createAndShowGUI() {
  val game = Game()
  val frame = JFrame("Pawned")

  val scope =
      MainScope() +
          CoroutineExceptionHandler { _, e ->
            LoggerFactory.getLogger(Pawned::class.java).error("Error", e)
            frame.dispose()
          }

  val panel = PawnedPanel.start(scope, game)

  PawnedController.start(scope, panel, game)

  frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
  frame.contentPane = panel
  frame.pack()
  frame.isVisible = true
  frame.addWindowListener(
      object : WindowListener {
        override fun windowOpened(e: WindowEvent?) {}

        override fun windowClosing(e: WindowEvent?) {}

        override fun windowClosed(e: WindowEvent?) {
          scope.cancel()
        }

        override fun windowIconified(e: WindowEvent?) {}

        override fun windowDeiconified(e: WindowEvent?) {}

        override fun windowActivated(e: WindowEvent?) {}

        override fun windowDeactivated(e: WindowEvent?) {}
      })
}

private class PawnedController
private constructor(val scope: CoroutineScope, val panel: JPanel, val game: Game) {
  var from: Coord? = null

  companion object {
    fun start(scope: CoroutineScope, panel: JPanel, game: Game): PawnedController {
      val controller = PawnedController(scope, panel, game)
      controller.init()
      return controller
    }
  }

  private fun init() {
    panel.addMouseListener(
        object : MouseListener {
          override fun mouseClicked(e: MouseEvent) {
            scope.launch(Dispatchers.IO) {
              val file = 'a' + e.x / 128
              val rank = 8 - e.y / 128
              val fromCoord = from
              from = null
              if (fromCoord == null) {
                from = Coord(file, rank)
              } else {
                attemptMove(fromCoord, Coord(file, rank))
              }
            }
          }

          override fun mousePressed(e: MouseEvent?) {}

          override fun mouseReleased(e: MouseEvent?) {}

          override fun mouseEntered(e: MouseEvent?) {}

          override fun mouseExited(e: MouseEvent?) {}
        })
  }

  suspend fun attemptMove(from: Coord, to: Coord) {
    withContext(Dispatchers.IO) { game.playMove(from, to) }
    withContext(Dispatchers.Main) { panel.repaint() }
  }
}

private class PawnedPanel private constructor(val game: Game) : JPanel(BorderLayout()) {
  val imageLoader: AtomicReference<Map<String, BufferedImage>> = AtomicReference()

  init {
    border = BorderFactory.createEmptyBorder(500, 500, 500, 500)
  }

  companion object {
    fun start(scope: CoroutineScope, game: Game): PawnedPanel {
      val panel = PawnedPanel(game)
      scope.launch { panel.init() }
      return panel
    }
  }

  private suspend fun init() {
    withContext(Dispatchers.IO) {
      val values = PieceType.values().toSet() - PieceType.UNRECOGNIZED
      imageLoader.set(
          (values.asSequence().map { fileName(it, Color.WHITE) } +
                  values.asSequence().map { fileName(it, Color.BLACK) } +
                  sequenceOf("dark-square.png", "light-square.png"))
              .map { it to ImageIO.read(object {}::class.java.getResource("/$it")) }
              .toMap())
    }
    withContext(Dispatchers.Main) { repaint() }
  }

  override fun getPreferredSize() = Dimension(1024, 1024)

  override fun paintComponent(g: Graphics) {
    super.paintComponent(g)

    val images = imageLoader.get() ?: return

    for (file in 'a'..'h') {
      for (rank in 1..8) {
        val (xOffset, yOffset) = toOffset(Coord(file, rank))
        val (startX, startY) = toPixelCoord(Coord(file, rank))
        val image =
            when ((xOffset + yOffset) % 2) {
              0 -> images["dark-square.png"]
              1 -> images["light-square.png"]
              else -> throw IllegalStateException()
            }
        g.drawImage(image, startX, startY, null)
      }
    }

    sequenceOf((game.whitePieces to Color.WHITE), (game.blackPieces to Color.BLACK)).forEach {
        (pieces, color) ->
      pieces.forEach {
        val (coord, pieceType) = it
        val (startX, startY) = toPixelCoord(coord)
        g.drawImage(images[fileName(pieceType, color)], startX, startY, null)
      }
    }
  }
}

private fun toOffset(coord: Coord): Pair<Int, Int> {
  val (file, rank) = coord
  val xOffset = file.code - 'a'.code
  val yOffset = 8 - rank
  return (xOffset to yOffset)
}

private fun toPixelCoord(coord: Coord): Pair<Int, Int> {
  val (file, rank) = coord
  val xOffset = file.code - 'a'.code
  val yOffset = 8 - rank
  val startX = xOffset * 128
  val startY = yOffset * 128
  return (startX to startY)
}

private fun fileName(pieceType: PieceType, color: Color) =
    "${if(color == Color.BLACK) { "black"} else {"white"}}-${pieceType.name.lowercase()}.png"
