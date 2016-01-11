package org.lare96.pastebin

import enumeratum.{Enum, EnumEntry}

/** A sealed class holding data for the `PastebinExposure` constants.
  *
  * @param value The value of the exposure constant.
  * @author lare96 <http://github.org/lare96>
  */
sealed class PastebinExposure(val value: Int) extends EnumEntry

/** A collection of case objects acting as constants that represent all of the possible Pastebin.com
  * exposure options.
  *
  * @author lare96 <http://github.org/lare96>
  */
object PastebinExposure extends Enum[PastebinExposure] {

  val values = findValues

  def withId(id: Int) = values.find(_.value == id)

  case object Public extends PastebinExposure(0)
  case object Unlisted extends PastebinExposure(1)
  case object Private extends PastebinExposure(2)
}
