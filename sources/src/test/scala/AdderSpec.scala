import Adder.{Sum, Add, GetSum}
import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, ImplicitSender, TestKit}
import org.scalatest.matchers.Matchers
import org.scalatest.{WordSpec, BeforeAndAfterAll}

class AdderSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender with WordSpec with Matchers with BeforeAndAfterAll {
  def this() = this(ActorSystem("MySpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An Adder actor" must {

    "return 3 as sum of 1 and 2" in {
      val greeter = system.actorOf(Props[Adder])
      greeter ! new Add(1)
      greeter ! new Add(2)
      greeter ! new GetSum()
      expectMsg(new Sum(3))
    }

    "know the Answer to the Ultimate Question of Life, the Universe, and Everything" in {
      val greeter = TestActorRef[Adder]
      greeter.underlyingActor.sum = 42
      greeter ! new GetSum()
      expectMsg(new Sum(42))
    }
  }

}
