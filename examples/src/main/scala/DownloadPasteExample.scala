import org.lare96.pastebin.Pastebin

object DownloadPasteExample extends App {

  val pastebin = new Pastebin("your_developer_key")

  pastebin.download("aBcDeF").foreach(it => println(s"Downloaded paste content:\n\n$it"))
}
