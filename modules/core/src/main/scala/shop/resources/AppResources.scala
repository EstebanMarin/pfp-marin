package shop.resources

sealed abstract class AppResources[F[_]]()

object AppResources {
  def make[F[_]] = {
    new AppResources[F] {}
  }
}
