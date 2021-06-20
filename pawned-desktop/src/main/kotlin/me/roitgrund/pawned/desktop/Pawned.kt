package me.roitgrund.pawned.desktop

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.*
import me.roitgrund.pawned.Coord
import me.roitgrund.pawned.Game
import me.roitgrund.pawned.api.Color
import me.roitgrund.pawned.api.PieceType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main() {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
  SwingUtilities.invokeLater(::createAndShowGUI)
}

private fun createAndShowGUI() {
  val game = Game()
  val frame = JFrame("Pawned")
  val panel = PawnedPanel(game)

  PawnedController.start(panel, game)

  frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
  frame.contentPane = panel
  frame.pack()
  frame.isVisible = true
}

private class PawnedController private constructor(val panel: JPanel, val game: Game) {
  var from: Coord? = null

  companion object {
    fun start(panel: JPanel, game: Game) {
      val controller = PawnedController(panel, game)
      controller.init()
    }
    val log: Logger = LoggerFactory.getLogger(PawnedController::class.java)
  }

  private fun init() {
    panel.addMouseListener(
        object : MouseListener {
          override fun mouseClicked(e: MouseEvent) {
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

          override fun mousePressed(e: MouseEvent?) {}

          override fun mouseReleased(e: MouseEvent?) {}

          override fun mouseEntered(e: MouseEvent?) {}

          override fun mouseExited(e: MouseEvent?) {}
        })
  }

  fun attemptMove(from: Coord, to: Coord) {
    (object : SwingWorker<Boolean, Void>() {
          override fun doInBackground(): Boolean {
            return game.playMove(from, to)
          }

          override fun done() {
            if (get()) {
              panel.repaint()
            }
          }
        })
        .execute()
  }
}

private class PawnedPanel(val game: Game) : JPanel(BorderLayout()) {
  val imageLoader = run {
    val panel = this
    object : SwingWorker<Map<String, BufferedImage>, Void>() {
      override fun doInBackground(): Map<String, BufferedImage> {
        return (PieceType.values().asSequence().map { fileName(it, Color.WHITE) } +
                PieceType.values().asSequence().map { fileName(it, Color.BLACK) } +
                sequenceOf("dark-square.png", "light-square.png"))
            .map { it to ImageIO.read(object {}::class.java.getResource("/$it")) }
            .toMap()
      }

      override fun done() {
        panel.repaint()
      }
    }
  }

  init {
    border = BorderFactory.createEmptyBorder(500, 500, 500, 500)
    imageLoader.execute()
  }

  override fun getPreferredSize() = Dimension(1024, 1024)

  override fun paintComponent(g: Graphics) {
    super.paintComponent(g)

    if (!imageLoader.isDone) {
      return
    }

    val images = imageLoader.get()

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
