package org.lare96.pastebin

import java.net.URL

import org.lare96.pastebin.PastebinHttpUtils._

import scala.io.Source
import scala.util.Try

/** One of the main classes of pastebin4scala. All anonymous Pastebin.com API features can be accessed and utilized from
  * methods contained inside `Pastebin` instances.
  *
  * @param devKey The developer API key. One is assigned to every Pastebin account and is required in order
  *               to use the Pastebin API.
  * @author lare96 <http://github.org/lare96>
  */
final class Pastebin(val devKey: String) {

  /** Authenticates the user with `username` and `password`.
    *
    * @param username The username to authenticate.
    * @param password The password to authenticate.
    * @return A `PastebinAccount` instance wrapped within `Try` containing the user key amongst more methods.
    */
  def login(username: String, password: String) = post[PastebinAccount](
    it => new PastebinAccount(devKey, it),

    ("api_dev_key", devKey),
    ("api_user_name", username),
    ("api_user_password", password)
  )

  /** Uploads an '''anonymous''' paste to Pastebin. To upload a paste associated with a specific user, use the `login`
    * method that returns a `PastebinAccount` instance and use its `upload` method instead.
    *
    * @param title The title of the paste.
    * @param content The content within the paste.
    * @param format The syntax highlighting for the paste.
    * @param expire The expiration date of the paste.
    * @param exposure The exposure level of the paste.
    * @return A `String` wrapped within `Try` containing the url to the uploaded paste.
    */
  def upload(title: String = "",
             content: String,
             format: String = "",
             expire: PastebinExpiration = PastebinExpiration.Never,
             exposure: PastebinExposure = PastebinExposure.Public) = post[String](
    it => it,

    ("api_dev_key", devKey),
    ("api_option", "paste"),
    ("api_paste_code", content),
    ("api_paste_private", exposure.value.toString),
    ("api_paste_expire_date", expire.value),
    ("api_paste_name", title),
    (if (format.isEmpty) "" else "api_paste_format", format)
  )

  /** Retrieves the `18` currently trending pastes.
    *
    * @return The `PastebinContentResult` instances wrapped within `Try`.
    */
  def trending = post[Seq[PastebinContentResult]](
    it => decodePastes(it),

    ("api_dev_key", devKey),
    ("api_option", "trends")
  )

  /** Downloads the contents of a paste.
    *
    * @param pasteKey The key of the paste to delete. The key of the paste is at the end of a paste link; for
    *                 example, in the link http://www.pastebin.com/aBcDeF the key would be `aBcDeF`.
    * @return A `String` wrapped within `Try` containing the contents of the downloaded paste.
    */
  def download(pasteKey: String) = Try[String] {
    Source.fromInputStream(new URL(s"http://www.pastebin.com/raw/$pasteKey")
      .openConnection.getInputStream).mkString
  }
}

/** Another one of the main classes of pastebin4scala. All Pastebin.com API features that require user authentication
  * can be accessed and utilized from methods contained inside `PastebinAccount` instances. This class should typically
  * '''not''' be instantiated except through the `login` method inside `Pastebin`.
  *
  * @param devKey The developer API key. Taken from `Pastebin` on authentication.
  * @param userKey The user key. Received from Pastebin.com on authentication.
  * @author lare96 <http://github.org/lare96>
  */
final class PastebinAccount(val devKey: String, val userKey: String) {

  /** Uploads an '''authenticated''' paste to Pastebin. To upload an anonymous paste, use the `upload` method within
    * the `Pastebin` class instead.
    *
    * @param title The title of the paste.
    * @param content The content within the paste.
    * @param format The syntax highlighting for the paste.
    * @param expire The expiration date of the paste.
    * @param exposure The exposure level of the paste.
    * @return A `String` wrapped within `Try` containing the url to the uploaded paste.
    */
  def upload(title: String = "",
             content: String,
             format: String = "",
             expire: PastebinExpiration = PastebinExpiration.Never,
             exposure: PastebinExposure = PastebinExposure.Public) = post[String](
    it => it,

    ("api_dev_key", devKey),
    ("api_user_key", userKey),
    ("api_option", "paste"),
    ("api_paste_code", content),
    ("api_paste_private", exposure.value.toString),
    ("api_paste_expire_date", expire.value),
    ("api_paste_name", title),
    (if (format.isEmpty) "" else "api_paste_format", format)
  )

  /** Deletes an authenticated paste.
    *
    * @param pasteKey The key of the paste to delete. The key of the paste is at the end of a paste link; for
    *                 example, in the link http://www.pastebin.com/aBcDeF the key would be `aBcDeF`.
    * @return A `String` wrapped within `Try` containing the paste key of the deleted paste.
    */
  def delete(pasteKey: String) = post[String](
    it => pasteKey,

    ("api_dev_key", devKey),
    ("api_user_key", userKey),
    ("api_option", "delete"),
    ("api_paste_key", pasteKey)
  )

  /** Retrieves all of the info for this authenticated user.
    *
    * @return A `PastebinAccountInfoResult` instance wrapped within `Try`.
    */
  def info = post[PastebinAccountInfoResult](
    it => decodeAccountInfo(it),

    ("api_dev_key", devKey),
    ("api_user_key", userKey),
    ("api_option", "userdetails")
  )

  /** Retrieves a variable amount of pastes by an authenticated user. The default amount of pastes
    * retrieved (no parameter specified) is `50`, the minimum amount is `1`, and the maximum amount is `1000`.
    *
    * @param amount The amount of pastes to retrieve (`50` default, `1` min, `1000` max).
    * @return The `PastebinContentResult` instances wrapped within `Try`.
    */
  def pastes(amount: Int = 0) = post[Seq[PastebinContentResult]](
    it => if (it == "No pastes found.") Seq.empty else decodePastes(it),

    ("api_dev_key", devKey),
    ("api_user_key", userKey),
    ("api_option", "list"),
    (if (amount == 0) "" else "api_results_limit", amount.toString)
  )
}