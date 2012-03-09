package typedfutures

import akka.actor._
import akka.dispatch.Future

object LetItCrashOrPipe extends App {

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

  val actorSystem = ActorSystem()
  val typedActors = TypedActor(actorSystem)

  val actor: PipeOrCrash = typedActors.typedActorOf(TypedProps[PipeOrCrashActor]())

  actor.go

}