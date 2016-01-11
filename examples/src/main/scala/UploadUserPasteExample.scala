import org.lare96.pastebin.Pastebin

object UploadUserPasteExample extends App {

  val pastebin = new Pastebin("your_developer_key")

  pastebin.login("some_username", "some_password").flatMap(_.upload
  (
    title = "A test paste.",
    content = "object Test { val example = 25 }\nclass Test { def func = println(Test.example) }",
    format = "scala"

  )).foreach(it => println(s"Upload complete, link: $it"))
}
