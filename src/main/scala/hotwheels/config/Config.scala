package hotwheels.config

import cats.effect.Async
import ciris._
import com.comcast.ip4s._
import hotwheels.config.AppEnvironment.{Prod, Test}
import hotwheels.config.types.{AppConfig, HttpClientConfig, HttpServerConfig, PostgreSQLConfig}

import scala.concurrent.duration._

object Config {

  def load[F[_] : Async]: F[AppConfig] =
    env("SC_APP_ENV")
      .as[AppEnvironment]
      .flatMap {
        case Test =>
          default[F](
          )
        case Prod =>
          default[F](
          )
      }
      .load[F]

  private def default[F[_]](): ConfigValue[F, AppConfig] = {
    env("SC_POSTGRES_PASSWORD").secret
      .map { postgresPassword =>
        AppConfig(
          HttpClientConfig(
            timeout = 60.seconds,
            idleTimeInPool = 30.seconds
          ),
          PostgreSQLConfig(
            host = "localhost",
            port = 5432,
            user = "postgres",
            password = postgresPassword,
            database = "store",
            max = 10
          ),
          HttpServerConfig(
            host = host"0.0.0.0",
            port = port"8080"
          )
        )
      }
  }

}
