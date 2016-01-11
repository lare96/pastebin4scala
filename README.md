Pastebin.com 4 Scala
===
Pastebin4scala is a complete Pastebin.com API wrapper written in Scala.

<br>

Overview
-------
All user-relevant methods are contained in `Pastebin` and `PastebinAccount` classes. `Pastebin`, for "anonymous" API features and `PastebinAccount` for features that require authentication.

All methods within those classes return a result wrapped within a `Try` instance. This allows for clean and functional handling of successful and unsuccessful operations. Take for example, this block of code that deletes an authenticated paste

```scala
val pastebin = new Pastebin("your_developer_key")

pastebin.login("some_username", "some_password").map(_.delete("aBcDeF")).foreach(it =>
    println(s"Paste $it successfully deleted."))
```

And the same code, using pattern matching to handle any exceptions that might be thrown during the chain of operations

```scala
val pastebin = new Pastebin("your_developer_key")

pastebin.login("some_username", "some_password").map(_.delete("aBcDeF")) match {

  case Success(it) => println(s"Paste $it successfully deleted.")
  
  case Failure(it) => it.printStackTrace()
}
```

Examples of all anonymous and authenticated Pastebin API features can be found here.


Issues
-------
 If you encounter any issues with pastebin4scala please open a new issue or a pull request with the fix.
