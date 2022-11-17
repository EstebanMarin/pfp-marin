package shop

import shop.modules._
import shop.resources.MkHttpServer

import cats.effect._
import org.http4s.HttpApp
import org.http4s.server.Server
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import shop.resources.AppResources

object Main extends IOApp.Simple {
  implicit val logger = Slf4jLogger.getLogger[IO]

  val services: Services[IO] = Services.make[IO]
  val http: HttpApp[IO]      = HttpApi.make[IO].httpApp

  val serverResource: Resource[IO, Server] = MkHttpServer[IO].newEmber(http)

  val appResources: Resource[IO, AppResources[IO]] = AppResources.make[IO]

  // TODO this can be done in parallel
  val composeResources: Resource[IO, (Server, AppResources[IO])] = for {
    server: Server           <- MkHttpServer[IO].newEmber(http)
    appRes: AppResources[IO] <- appResources
  } yield (server, appRes)

  override def run: IO[Unit] = {
    composeResources.use {
      case (_, _) => {
        Logger[IO].info(s"SERVER STARTED") *> IO.never
      }
    }
  }

}
