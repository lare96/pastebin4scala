package org.lare96.pastebin

import enumeratum.{Enum, EnumEntry}

/** A sealed class holding data for the `PastebinExpiration` constants.
  *
  * @param value The value of the expiration constant.
  * @author lare96 <http://github.org/lare96>
  */
sealed class PastebinExpiration(val value: String) extends EnumEntry

/** A collection of case objects acting as constants that represent all of the possible Pastebin.com
  * expiration time options.
  *
  * @author lare96 <http://github.org/lare96>
  */
object PastebinExpiration extends Enum[PastebinExpiration] {

  val values = findValues
  def withNameValue(name: String) = values.find(_.value == name)

  case object Never extends PastebinExpiration("N")
  case object TenMinutes extends PastebinExpiration("10M")
  case object OneHour extends PastebinExpiration("1H")
  case object OneDay extends PastebinExpiration("1D")
  case object OneWeek extends PastebinExpiration("1W")
  case object TwoWeeks extends PastebinExpiration("2W")
  case object OneMonth extends PastebinExpiration("1M")
}