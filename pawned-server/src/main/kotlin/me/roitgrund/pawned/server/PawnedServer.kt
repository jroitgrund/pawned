package me.roitgrund.pawned.server

import io.grpc.ServerBuilder
import java.io.InputStream
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import kotlin.random.Random
import me.roitgrund.pawned.Coord
import me.roitgrund.pawned.Game
import me.roitgrund.pawned.api.*

fun startServer(port: Int, certificate: InputStream, signingKey: InputStream) {
  val waitingPlayer = AtomicReference<String>()
  val allPlayers = ConcurrentHashMap.newKeySet<String>()
  val whitePlayers = ConcurrentHashMap<String, Game>()
  val blackPlayers = ConcurrentHashMap<String, Game>()

  ServerBuilder.forPort(port)
      .useTransportSecurity(certificate, signingKey)
      .addService(
          object : PawnedServiceGrpcKt.PawnedServiceCoroutineImplBase() {
            override suspend fun newGame(request: NewGameRequest): NewGameResponse {
              val id = UUID.randomUUID().toString()
              allPlayers.add(id)
              val waitingId =
                  waitingPlayer.getAndUpdate {
                    if (it == null) {
                      id
                    } else {
                      null
                    }
                  }

              if (waitingId != null) {
                val game = Game()
                if (Random.nextBoolean()) {
                  whitePlayers[waitingId] = game
                  blackPlayers[id] = game
                } else {
                  whitePlayers[id] = game
                  blackPlayers[waitingId] = game
                }
              }

              return NewGameResponse.newBuilder().setId(id).build()
            }

            override suspend fun getGame(request: GetGameRequest): GameResponse {
              if (!allPlayers.contains(request.id)) {
                throw IllegalArgumentException()
              }

              val whiteGame = whitePlayers[request.id]
              val blackGame = blackPlayers[request.id]

              if (whiteGame == null && blackGame == null) {
                return GameResponse.newBuilder()
                    .setWaitingForGame(WaitingForGame.getDefaultInstance())
                    .build()
              }

              val (game, color) = getGameAndColor(whiteGame, blackGame)

              return GameResponse.newBuilder().setGameInfo(gameInfo(game, color)).build()
            }

            override suspend fun move(request: MoveRequest): GameResponse {
              if (!allPlayers.contains(request.id)) {
                throw IllegalArgumentException()
              }

              val (game, color) =
                  getGameAndColor(whitePlayers[request.id], blackPlayers[request.id])

              game.playMove(Coord(request.fromCoord), Coord(request.toCoord))

              return GameResponse.newBuilder().setGameInfo(gameInfo(game, color)).build()
            }
          })
      .build()
      .start()
}

private fun gameInfo(game: Game, color: Color) =
    GameInfo.newBuilder()
        .setNextTurnState(game.nextTurnState)
        .setPlayerColor(color)
        .addAllWhitePieces(
            game.whitePieces
                .map { (coord, pieceType) ->
                  Piece.newBuilder().setCoord(coord.toString()).setPieceType(pieceType).build()
                }
                .asIterable())
        .addAllWhitePieces(
            game.blackPieces
                .map { (coord, pieceType) ->
                  Piece.newBuilder().setCoord(coord.toString()).setPieceType(pieceType).build()
                }
                .asIterable())
        .build()

private fun getGameAndColor(whiteGame: Game?, blackGame: Game?) =
    when {
      whiteGame != null -> (whiteGame to Color.WHITE)
      blackGame != null -> (blackGame to Color.BLACK)
      else -> throw IllegalStateException()
    }
