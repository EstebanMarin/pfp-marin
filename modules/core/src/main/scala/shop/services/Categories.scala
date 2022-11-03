package shop.services

import shop.domain.category._

trait Categories[F[_]] {
  def getAll: F[List[Category]]
  def post(c: Category): F[CategoryID]
}
