package hotwheels.resources

import cats.effect.{Resource, Sync}
import hotwheels.service.Vehicles
import skunk.Session

object Services {
  def make[F[_]: Sync](
      postgres: Resource[F, Session[F]],
  ): Services[F] = {
    new Services[F](
      vehicles = Vehicles.make[F](postgres)
    ) {}
  }
}

sealed abstract class Services[F[_]] private (
  val vehicles: Vehicles[F]
)