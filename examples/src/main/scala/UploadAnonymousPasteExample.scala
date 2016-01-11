import org.lare96.pastebin.{Pastebin, PastebinExpiration}

object UploadAnonymousPasteExample extends App {

  val pastebin = new Pastebin("your_developer_key")

  pastebin.upload(
    title = "A test paste.",
    content = "object Test { val example = 25 }\nclass Test { def func = println(Test.example) }",
    format = "scala",
    expire = PastebinExpiration.OneHour

  ).foreach(it => println(s"Upload complete, link: $it"))
}
