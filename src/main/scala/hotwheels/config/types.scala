package hotwheels.config

import ciris._
import com.comcast.ip4s.{Host, Port}

import scala.concurrent.duration._

object types {

  case class AppConfig(
      httpClientConfig: HttpClientConfig,
      postgreSQL: PostgreSQLConfig,
      httpServerConfig: HttpServerConfig
  )

  case class PostgreSQLConfig(
      host: String,
      port: Int,
      user: String,
      password: Secret[String],
      database: String,
      max: Int
  )

  case class HttpServerConfig(
      host: Host,
      port: Port
  )

  case class HttpClientConfig(
      timeout: FiniteDuration,
      idleTimeInPool: FiniteDuration
  )

}
