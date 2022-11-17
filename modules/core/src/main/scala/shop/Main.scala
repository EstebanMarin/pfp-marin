package shop

import shop.modules._
import shop.resources.MkHttpServer

import cats.effect._
import cats.implicits._
import org.http4s.HttpApp
import org.http4s.server.Server
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import shop.resources.AppResources
import skunk.Session

object Main extends IOApp.Simple {
  implicit val logger = Slf4jLogger.getLogger[IO]

  val services: Services[IO] = Services.make[IO]
  val http: HttpApp[IO]      = HttpApi.make[IO].httpApp
  // val appResources = AppResources.make[IO].postgres.evalMap { _: Session[IO] =>
  //   IO.println("TESTING")
  // }

  val serverResource: Resource[IO, Server] = MkHttpServer[IO].newEmber(http)

  val composeResoureces = for {
    server: Server      <- MkHttpServer[IO].newEmber(http)
    // appRes: Session[IO] <- AppResources.make[IO].postgres
  // } yield (server, appRes)
  } yield (server)

  override def run: IO[Unit] = {
    serverResource.use {
      case _: Server =>
        Logger[IO].info(s"SERVER STARTED") *> IO.never
    }
  }

}
