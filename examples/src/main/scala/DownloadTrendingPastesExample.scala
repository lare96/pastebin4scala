import org.lare96.pastebin.Pastebin

object DownloadTrendingPastesExample extends App {

  val pastebin = new Pastebin("your_developer_key")

  pastebin.trending.foreach(_.foreach(it => println(s"Trending paste url: ${it.url}")))
}
