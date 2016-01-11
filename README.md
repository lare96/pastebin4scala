Pastebin.com 4 Scala
===
Pastebin4scala is a complete Pastebin.com API wrapper written in Scala. You'll need a developer key in order to use the Pastebin API. You can find yours [here](http://pastebin.com/api#1) afer creating a Pastebin account.

<br>

Overview
-------
All user-relevant methods are contained in `Pastebin` and `PastebinAccount` classes. `Pastebin`, for "anonymous" API features and `PastebinAccount` for features that require authentication.

All methods within those classes return a result wrapped within a `Try` instance. This allows for clean and functional handling of successful and unsuccessful operations. Take for example, this block of code that uploads an authenticated paste

```scala
val pastebin = new Pastebin("developer_key")

pastebin.login("username", "password").flatMap(_.upload
(
  title = "A test paste.",
  content = "object Test { val example = 25 }",
  format = "scala"

)).foreach(it => println(s"Upload complete, link: $it"))
```

And the same code, using pattern matching to handle any exceptions that might have been thrown during the chain of operations

```scala
val pastebin = new Pastebin("developer_key")

pastebin.login("username", "password").flatMap(_.upload
(
  title = "A test paste.",
  content = "object Test { val example = 25 }",
  format = "scala"

)) match {
  case Success(it) => println(s"Upload complete, link: $it")
  
  case Failure(it) => handleErrorSomehow(it)
}
```

Deleting a paste and printing off an indication of that in just a few lines...

```scala
val pastebin = new Pastebin("your_developer_key")

pastebin.login("some_username", "some_password").flatMap(_.delete("aBcDeF")).foreach(it =>
    println(s"Paste $it successfully deleted."))
```

More examples of anonymous and authenticated Pastebin API features can be found [here](https://github.com/lare96/pastebin4scala/tree/master/examples/src/main/scala).


Issues
-------
If you encounter any issues with pastebin4scala please open a new [issue](https://github.com/lare96/pastebin4scala/issues/new) describing the problem or a [pull request](https://github.com/lare96/pastebin4scala/compare) with a potential fix to the problem.
