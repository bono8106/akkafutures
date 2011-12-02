package typedfutures

import akka.actor.TypedActor.{dispatcher, timeout}
import akka.dispatch.Future
import akka.dispatch.KeptPromise

trait Service {
  def square(x: Int): Future[Int]
}

class ServiceActor extends Service with Serializable {

  def process(x: Int) = x*x

  def square(x: Int) = {
    log("Service enter")
    val result = new KeptPromise(Right(process(x)))
    log("Service leave")
    result
  }

}