package org.lare96.pastebin

import java.net.URL
import java.nio.charset.StandardCharsets

import scala.io.Source
import scala.util.Try
import scala.xml.XML

/** A singleton object containing methods related to encoding and decoding HTTP POST requests. Methods
  * within this object should be statically imported.
  *
  * @author lare96 <http://github.org/lare96>
  */
object PastebinHttpUtils {

  /** The link where login HTTP POST requests will be sent. */
  private final val ApiLogin = "http://pastebin.com/api/api_login.php"

  /** The link where all non-login HTTP POST requests will sent. */
  private final val ApiPost = "http://pastebin.com/api/api_post.php"

  /** Encodes and sends a HTTP POST request to either `ApiLogin` or `ApiPost`, and then reads and decodes
    * the response.
    *
    * @param result The function that will decode the response, into `T`.
    * @param request The parameters for the HTTP POST request.
    * @tparam T The type that the response will be decoded into.
    * @return A `Try` instance holding either the response to the HTTP POST request or an `Exception`.
    */
  def post[T](result: (String => T), request: (String, String)*) = Try[T] {

    val connection = new URL(if (request.exists(_._1 == "api_user_name")) ApiLogin else ApiPost).openConnection

    connection.setDoInput(true)
    connection.setDoOutput(true)


    val output = (for ((k, v) <- request if !k.isEmpty) yield k + '=' + v + '&').mkString.dropRight(1)

    connection.getOutputStream.write(output.getBytes(StandardCharsets.UTF_8))


    val response = Source.fromInputStream(connection.getInputStream).mkString

    if (response.startsWith("Bad API request")) {
      throw new PastebinResponseException(response)
    } else {
      result(response)
    }
  }

  /** Returns a `Seq` of `PastebinContentResult` instances decoded from `resp`.
    *
    * @param resp The response that will be decoded.
    */
  def decodePastes(resp: String) = {
    val xml = XML.loadString(s"<pastes>\n$resp</pastes>") \ "paste"

    for (it <- xml) yield {
      def get(element: String) = (it \ element).text

      PastebinContentResult(get("paste_key"),
        get("paste_date").toLong,
        get("paste_title"),
        get("paste_size").toInt,
        get("paste_expire_date").toLong,
        PastebinExposure.withId(get("paste_private").toInt).get,
        get("paste_format_long"),
        get("paste_format_short"),
        get("paste_url"),
        get("paste_hits").toLong)
    }
  }

  /** Returns a `PastebinAccountInfoResult` instance decoded from `resp`.
    *
    * @param resp The response that will be decoded.
    */
  def decodeAccountInfo(resp: String) = {
    val xml = XML.loadString(resp)

    def get(element: String) = (xml \ element).text

    PastebinAccountInfoResult(get("user_name"),
      get("user_format_short"),
      PastebinExpiration.withNameValue(get("user_expiration")).get,
      get("user_avatar_url"),
      PastebinExposure.withId(get("user_private").toInt).get,
      get("user_website"),
      get("user_email"),
      get("user_location"),
      PastebinAccountType.withId(get("user_account_type").toInt).get)
  }
}

/** An `Exception` implementation thrown when an invalid response to a HTTP POST request is received. These
  * responses '''always''' start with "Bad API request."
  *
  * @param response The invalid response that was received.
  * @author lare96 <http://github.org/lare96>
  */
final class PastebinResponseException(response: String) extends Exception(response)

/** A decoded Pastebin.com XML response representing a paste.
  *
  * @param pasteKey The key of the paste to delete. The key of the paste is at the end of a paste link; for
  *                 example, in the link http://www.pastebin.com/aBcDeF the key would be `aBcDeF`.
  * @param date The date that the paste was created on, in epoch seconds. All date-time class instances can be retrieved
  *             through this value with `Instant.ofEpochSecond(date)`.
  * @param title The title of the paste.
  * @param size The size of the paste, in `bytes`.
  * @param expireDate The date that the paste will expire on, in epoch seconds. All date-time class instances can be
  *                   retrieved through this value with `Instant.ofEpochSecond(date)`.
  * @param pastebinExposure The exposure value of the paste.
  * @param formatLong The 'proper' name of the syntax highlighting in the paste.
  * @param formatShort The 'code' name of the syntax highlighting in the paste.
  * @param url The url of the paste.
  * @param hits The unique views the paste has accumulated.
  * @author lare96 <http://github.org/lare96>
  */
final case class PastebinContentResult(pasteKey: String,
                                       date: Long,
                                       title: String,
                                       size: Int,
                                       expireDate: Long,
                                       pastebinExposure: PastebinExposure,
                                       formatLong: String,
                                       formatShort: String,
                                       url: String,
                                       hits: Long)

/** A decoded Pastebin.com XML response representing account information.
  *
  * @param username The username of the account.
  * @param formatShort The default syntax highlighting listed for the account.
  * @param expiration The default paste expiration time listed for the account.
  * @param avatarUrl The url of the avatar on the account.
  * @param exposure The default exposure level listed for the account.
  * @param websiteUrl The url of the website listed on the account.
  * @param email The email listed on the account.
  * @param location The location of the user that created the account.
  * @param accountType The account type, related to payment.
  * @author lare96 <http://github.org/lare96>
  */
final case class PastebinAccountInfoResult(username: String,
                                           formatShort: String,
                                           expiration: PastebinExpiration,
                                           avatarUrl: String,
                                           exposure: PastebinExposure,
                                           websiteUrl: String,
                                           email: String,
                                           location: String,
                                           accountType: PastebinAccountType)