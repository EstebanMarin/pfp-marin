package shop

import shop.modules._
import shop.resources.{ AppResources, MkHttpServer }
import cats.effect._
import org.http4s.HttpApp
import org.typelevel.log4cats.slf4j.Slf4jLogger
import dev.profunktor.redis4cats.effect.Log.NoOp._
import org.typelevel.log4cats.SelfAwareStructuredLogger

object Main extends IOApp.Simple {
  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]
  override def run: IO[Unit] = {
    AppResources
      .make[IO]
      .evalMap { (appRes: AppResources[IO]) =>
        val services: Services[IO] = Services.make[IO](appRes.postgres)
        val httpApp: HttpApp[IO]   = HttpApi.make[IO](services).httpApp
        IO(httpApp)
      }
      .flatMap { case (app) => MkHttpServer[IO].newEmber(app) }
      .useForever
  }

}
