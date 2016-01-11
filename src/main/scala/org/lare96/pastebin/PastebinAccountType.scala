package org.lare96.pastebin

import enumeratum.{EnumEntry, Enum}

/** A sealed class holding data for the `PastebinAccountType` constants.
  *
  * @param value The value of the account type.
  * @author lare96 <http://github.org/lare96>
  */
sealed class PastebinAccountType(val value: Int) extends EnumEntry

/** A collection of case objects acting as constants that represent all of the possible Pastebin.com
  * account types.
  *
  * @author lare96 <http://github.org/lare96>
  */
object PastebinAccountType extends Enum[PastebinAccountType] {

  val values = findValues

  def withId(id: Int) = values.find(_.value == id)

  case object Normal extends PastebinAccountType(0)
  case object Pro extends PastebinAccountType(1)
}
