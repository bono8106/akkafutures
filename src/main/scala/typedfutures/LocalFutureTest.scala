package typedfutures

object LocalFutureTest extends App {

  val service = namedThreadActorOf(classOf[Service], classOf[ServiceActor], "service")
  val client = namedThreadActorOf(classOf[Client], classOf[ClientActor], "client")

  client.go(service)

}
