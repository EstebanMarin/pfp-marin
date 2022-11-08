package shop

import cats.effect._

object Main extends IOApp.Simple {
  override def run = IO.println("hello from module")

}
