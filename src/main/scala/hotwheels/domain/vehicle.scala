package hotwheels.domain

import derevo.cats.{eqv, show}
import derevo.circe.{decoder, encoder}
import derevo.derive
import hotwheels.domain.user.UserId
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

  @derive(decoder, encoder)
  case class VehicleRequest(name: String,
                            vehicleType: VehicleTypeId,
                            date: LocalDateTime,
                            color: String,
                            user: UserId)


  implicit val localDateTimeEncoder: Encoder[LocalDateTime] =
    Encoder.encodeString.contramap[LocalDateTime](_.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))

  implicit val localDateTimeDecoder: Decoder[LocalDateTime] =
    Decoder.decodeString.map(str => LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME))

}