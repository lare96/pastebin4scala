import org.lare96.pastebin.Pastebin

object DownloadUserInfoExample extends App {

  val pastebin = new Pastebin("your_developer_key")

  pastebin.login("some_username", "some_password").flatMap(_.info).foreach(it =>
    println(s"User information retrieved, account type: ${it.accountType}"))
}
