package hotwheels

import cats.effect._
import hotwheels.config.Config
import hotwheels.modules.HttpApi
import hotwheels.resources.{AppResources, MkHttpServer, Services}
import org.typelevel.log4cats.{Logger, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple {

  implicit val logger: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  def run: IO[Unit] = {
    Config.load[IO].flatMap { cfg =>
      Logger[IO].info(s"loaded config $cfg") >>
        AppResources.make[IO](cfg)
          .map { res => // res
            // combine and plug in resources, wire up modules...
            val services = Services.make[IO](res.postgres)
            val httpApp = HttpApi.make[IO](services)
            cfg -> httpApp.httpApp
          }
          .flatMap {
            case (cfg, httpApp) =>
              MkHttpServer[IO].newEmber(cfg.httpServerConfig, httpApp)
          }
          .useForever
    }
  }
}
