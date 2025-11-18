package hotwheels.domain

import derevo.cats.{eqv, show}
import derevo.circe.{decoder, encoder}
import derevo.derive
import io.estatico.newtype.macros.newtype

import java.util.UUID

object user {

  @derive(decoder, encoder, eqv, show)
  @newtype
  case class UserId(value: UUID)

  @derive(decoder, encoder, eqv, show)
  case class User(id: UserId, name: String, email: String, sub: String)
}
