package typedfutures

import akka.actor.TypedActor
import akka.dispatch.Future

trait Service {
  def square(x: Int): Future[Int]
}

class ServiceActor extends TypedActor with Service with Serializable {

  def process(x: Int) = x*x

  def square(x: Int) = {
    log("Service enter")
    val result = future(process(x))
    log("Service leave")
    result
  }

}