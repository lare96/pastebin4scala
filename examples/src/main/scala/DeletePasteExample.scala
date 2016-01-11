import org.lare96.pastebin.Pastebin

object DeletePasteExample extends App {

  val pastebin = new Pastebin("your_developer_key")

  pastebin.login("some_username", "some_password").flatMap(_.delete("aBcDeF")).foreach(it =>
    println(s"Paste $it successfully deleted."))
}
