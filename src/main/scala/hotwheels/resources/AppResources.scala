package hotwheels.resources

import cats.effect.std.Console
import cats.effect.{Resource, Temporal}
import cats.syntax.all._
import fs2.io.net.Network
import hotwheels.config.types.{AppConfig, PostgreSQLConfig}
import natchez.Trace.Implicits.noop
import org.http4s.client.Client
import org.typelevel.log4cats.Logger
import skunk._
import skunk.codec.text._
import skunk.implicits._

sealed abstract class AppResources[F[_]](
    val client: Client[F],
    val postgres: Resource[F, Session[F]],
)

object AppResources {

  def make[F[_]: Temporal: Logger: MkHttpClient: Network: Console](
      cfg: AppConfig
  ): Resource[F, AppResources[F]] = {

    def checkPostgresConnection(
        postgres: Resource[F, Session[F]]
    ): F[Unit] =
      postgres.use { session =>
        session.unique(sql"select version();".query(text)).flatMap { v =>
          Logger[F].info(s"Connected to Postgres $v")
        }
      }

    def mkPostgreSqlResource(c: PostgreSQLConfig): SessionPool[F] =
      Session
        .pooled[F](
          host = c.host,
          port = c.port,
          user = c.user,
          password = Some(c.password.value),
          database = c.database,
          max = c.max
        )
        .evalTap(checkPostgresConnection)

    (
      MkHttpClient[F].newEmber(cfg.httpClientConfig),
      mkPostgreSqlResource(cfg.postgreSQL),
    ).parMapN(new AppResources[F](_,  _) {})

  }

}
