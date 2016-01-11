import org.lare96.pastebin.Pastebin

object DownloadUserPastesExample extends App {

  val pastebin = new Pastebin("your_developer_key")

  pastebin.login("some_username", "some_password").flatMap(_.pastes()).foreach(
    _.foreach(it => println(s"User pastes retrieved, url: ${it.url}")))
}
