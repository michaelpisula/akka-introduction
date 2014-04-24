import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AdderTest {

    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        system.shutdown();
        system.awaitTermination();
    }

    @Test
    public void add2to1() {
        ActorRef adder = system.actorOf(Props.create(Adder.class));
        JavaTestKit probe = new JavaTestKit(system);
        adder.tell(new Adder.Add(1), probe.getRef());
        adder.tell(new Adder.Add(2), probe.getRef());
        adder.tell(new Adder.GetSum(), probe.getRef());

        probe.expectMsgEquals(new Adder.Sum(3));
    }

}
