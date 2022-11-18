package shop.routes

import cats.effect.IO
import cats.implicits._
import io.circe._
import io.circe.syntax.EncoderOps
import org.http4s.Method._
import org.http4s._
import org.http4s.circe._
import org.http4s.client.dsl.io._
import org.http4s.syntax.literals._
import org.scalacheck.Gen
import shop.Generators._
import shop.domain.ID
import shop.domain.brand._
import shop.services.Brands
import weaver.scalacheck.Checkers
import weaver.{Expectations, SimpleIOSuite}

import scala.util.control.NoStackTrace

object BrandRouteSuite extends SimpleIOSuite with Checkers {
  case object DummyError extends NoStackTrace

  def expectHttpBodyAndStatus[A: Encoder](routes: HttpRoutes[IO], req: Request[IO])(
      expectedBody: A,
      expectedStatus: Status
  ): IO[Expectations] =
    routes.run(req).value.flatMap {
      case Some(resp) =>
        resp.asJson.map { json =>
          // Expectations form a multiplicative Monoid but we can also use other combinators like `expect.all`
          expect.same(resp.status, expectedStatus) |+| expect
            .same(json.dropNullValues, expectedBody.asJson.dropNullValues)
        }
      case None => IO.pure(failure("route not found"))
    }

  test("GET brands succeeds") {
    def dataBrands(brands: List[Brand]) = new TestBrands {
      override def findAll: IO[List[Brand]] =
        IO.pure(brands)
    }
    forall(Gen.listOf(brandGen)) { b =>
      val req    = GET(uri"/brands")
      val routes = BrandRoutes[IO](dataBrands(b)).routes
      expectHttpBodyAndStatus(routes, req)(b, Status.Ok)
    }
  }

  def failingBrands(brands: List[Brand]) = new TestBrands {
    override def findAll: IO[List[Brand]] =
      IO.raiseError(DummyError) *> IO.pure(brands)
  }

  def expectHttpFailure(routes: HttpRoutes[IO], req: Request[IO]): IO[Expectations] =
    routes.run(req).value.attempt.map {
      case Left(_) => success
      case Right(_) => failure("expected a failure")
    }

  test("GET brands fails") {
    forall(Gen.listOf(brandGen)) { b =>
      val req = GET(uri"/brands")
      val routes = BrandRoutes[IO](failingBrands(b)).routes
      expectHttpFailure(routes, req)
    }
  }
}

protected class TestBrands extends Brands[IO] {
  def create(name: BrandName): IO[BrandId] = ID.make[IO, BrandId]
  def findAll: IO[List[Brand]]             = IO.pure(List.empty)
}
