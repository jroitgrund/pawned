package me.roitgrund.pawned.server

import io.grpc.netty.GrpcSslContexts
import io.grpc.netty.NettyChannelBuilder
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import me.roitgrund.pawned.api.*

internal class PawnedServerTest {
  @Test
  fun test_server() {
    runBlocking {
      val port = 5678

      startServer(
          port,
          object {}::class.java.getResource("/localhost.pem").openStream(),
          object {}::class.java.getResource("/localhost-key-pkcs8.pem").openStream())

      val pawnedService =
          PawnedServiceGrpcKt.PawnedServiceCoroutineStub(
              NettyChannelBuilder.forAddress("localhost.roitgrund.me", port)
                  .sslContext(
                      GrpcSslContexts.forClient()
                          .trustManager(object {}::class.java.getResource("/ca.pem").openStream())
                          .build())
                  .build())

      val id1 = pawnedService.newGame(NewGameRequest.getDefaultInstance()).id
      assertTrue(
          pawnedService.getGame(GetGameRequest.newBuilder().setId(id1).build()).hasWaitingForGame())
      val id2 = pawnedService.newGame(NewGameRequest.getDefaultInstance()).id

      val id1Game = pawnedService.getGame(GetGameRequest.newBuilder().setId(id1).build())
      assertTrue(id1Game.hasGameInfo())
      assertTrue(
          pawnedService.getGame(GetGameRequest.newBuilder().setId(id2).build()).hasGameInfo())

      val (white, black) =
          if (id1Game.gameInfo.playerColor == Color.WHITE) {
            (id1 to id2)
          } else {
            (id2 to id1)
          }

      pawnedService.move(
          MoveRequest.newBuilder().setId(white).setFromCoord("e2").setToCoord("e4").build())
      pawnedService.move(
          MoveRequest.newBuilder().setId(black).setFromCoord("c7").setToCoord("c5").build())
      pawnedService.move(
          MoveRequest.newBuilder().setId(white).setFromCoord("f1").setToCoord("c4").build())
      pawnedService.move(
          MoveRequest.newBuilder().setId(black).setFromCoord("b8").setToCoord("c6").build())
      pawnedService.move(
          MoveRequest.newBuilder().setId(white).setFromCoord("d1").setToCoord("h5").build())
      pawnedService.move(
          MoveRequest.newBuilder().setId(black).setFromCoord("g8").setToCoord("f6").build())
      pawnedService.move(
          MoveRequest.newBuilder().setId(white).setFromCoord("h5").setToCoord("f7").build())

      assertEquals(
          NextTurnState.WHITE_WON,
          pawnedService.getGame(GetGameRequest.newBuilder().setId(id1).build())
              .gameInfo
              .nextTurnState)
      assertEquals(
          NextTurnState.WHITE_WON,
          pawnedService.getGame(GetGameRequest.newBuilder().setId(id2).build())
              .gameInfo
              .nextTurnState)
    }
  }
}
