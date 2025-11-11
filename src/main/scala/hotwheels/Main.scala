package hotwheels

import cats.effect.{IO, IOApp}
import hotwheels.config.Config
import hotwheels.resources.{AppResources, MkHttpServer}
import org.http4s.HttpRoutes
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple {

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  def run: IO[Unit] = {
    Config.load[IO].flatMap { cfg =>
      Logger[IO].info(s"loaded config $cfg") >>
        AppResources.make[IO](cfg)
          .map { _ => // res
            // combine and plug in resources, wire up modules...
            val httpApp = HttpRoutes.empty[IO].orNotFound
            cfg -> httpApp
          }
          .flatMap {
            case (cfg, httpApp) =>
              MkHttpServer[IO].newEmber(cfg.httpServerConfig, httpApp)
          }
          .useForever
    }
  }
}
