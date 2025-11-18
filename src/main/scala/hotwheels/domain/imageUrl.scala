package hotwheels.domain

import derevo.cats.{eqv, show}
import derevo.circe.{decoder, encoder}
import derevo.derive
import hotwheels.domain.vehicle.VehicleId
import io.estatico.newtype.macros.newtype

import java.util.UUID

object imageUrl {

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class ImageUrlId(value: UUID)

  @derive(decoder, encoder, eqv, show)
  case class ImageUrl(id: ImageUrlId, objectKey: String, url: String, vehicleId: VehicleId)
}
