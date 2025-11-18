package hotwheels.domain

import derevo.cats.{eqv, show}
import derevo.circe.{decoder, encoder}
import derevo.derive
import hotwheels.domain.user.UserId
import io.estatico.newtype.macros.newtype

import java.time.LocalDateTime
import java.util.UUID

object vehicle {

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class VehicleId(value: UUID)

  @derive(decoder, encoder)
  case class Vehicle(id: VehicleId,
                     name: String,
                     vehicleType: VehicleTypeId,
                     date: LocalDateTime,
                     color: String,
                     user: UserId)

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class VehicleTypeId(value: UUID)

  @derive(decoder, encoder, eqv, show)
  case class VehicleType(id: VehicleTypeId, name: String)
}

