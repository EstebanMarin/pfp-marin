package shop.services

import shop.domain.ID
import shop.domain.brand._
import shop.domain.effects.GenUUID

import cats.effect.kernel._
import cats.syntax.all._
import skunk._
import skunk.codec.all._
import skunk.implicits._

trait Brands[F[_]] {
  def findAll: F[List[Brand]]
  def create(name: BrandName): F[BrandId]
}

object Brands {
  def make[F[_]: MonadCancelThrow: GenUUID](
      postgres: Resource[F, Session[F]]
  ): Brands[F] = {
    new Brands[F] {
      import BrandSQL._
      override def findAll: F[List[Brand]] =
        postgres.use(_.execute(selectAll))
      override def create(name: BrandName): F[BrandId] =
        postgres.use { session: Session[F] =>
          session.prepare(insertBrand).use { cmd =>
            ID.make[F, BrandId].flatMap { id =>
              cmd.execute(Brand(id, name)).as(id)
            }
          }

        }
    }
  }

  private object BrandSQL {
    val brandId: Codec[BrandId]     = uuid.imap[BrandId](BrandId(_))(_.value)
    val brandName: Codec[BrandName] = varchar.imap[BrandName](BrandName(_))(_.value)
    val codec: Codec[Brand] =
      (brandId ~ brandName).imap {
        case i ~ n => Brand(i, n)
      }(b => b.uuid ~ b.name)

    val selectAll: Query[Void, Brand] =
      sql"""
        SELECT * FROM brands
       """.query(codec)

    val insertBrand: Command[Brand] =
      sql"""
        INSERT INTO brands
        VALUES ($codec)
        """.command

  }

}
