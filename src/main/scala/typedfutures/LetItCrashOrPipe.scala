package typedfutures

import akka.actor._
import scala.concurrent.Future

object LetItCrashOrPipe extends App {

  val actorSystem = ActorSystem()
  val typedActors = TypedActor(actorSystem)

  implicit def executionContext = actorSystem.dispatcher

  trait PipeOrCrash {
    def pipe: Future[Any]
    def crash: Unit

    def go: Unit
  }

  class PipeOrCrashActor extends PipeOrCrash {
    def pipe: Future[Any] = { println("this="+this); throw new Exception("Exception piped through future") }
    def crash: Unit = { println("this="+this); throw new Exception("Exception piped to supervisor (let-it-crash)") }
    def go {
      val self = TypedActor.self[PipeOrCrash]
      self.pipe onComplete { result => println("Future completed with " + result) }
      self.crash
    }

  }

  val actor: PipeOrCrash = typedActors.typedActorOf(TypedProps[PipeOrCrashActor]())

  actor.go

}