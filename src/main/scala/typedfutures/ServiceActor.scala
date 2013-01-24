package typedfutures

import akka.actor.TypedActor.{dispatcher}
import scala.concurrent.Future

trait Service {
  def square(x: Int): Future[Int]
}

class ServiceActor extends Service with Serializable {

  def process(x: Int) = x*x

  def square(x: Int) = {
    log("Service enter")
    val result = Future.successful(process(x))
    log("Service leave")
    result
  }

}