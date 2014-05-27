import akka.actor.*;
import scala.concurrent.duration.Duration;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


public class StringAdder extends UntypedActor {

    private int sum = 0;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Add) {
            sum += Integer.parseInt(((Add) message).value);
        } else if (message instanceof GetSum) {
            getSender().tell(new Sum(sum), getSelf());
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("AdderSystem");
        ActorRef add = system.actorOf(Props.create(StringAdder.class));
        Inbox inbox = Inbox.create(system);

        inbox.send(add, new Add("1"));
        inbox.send(add, new Add("2"));
        inbox.send(add, new Add("3"));
        inbox.send(add, new Add("four"));
        inbox.send(add, new Add("5"));

        inbox.send(add, new GetSum());

        Sum response = (Sum) inbox.receive(Duration.create(10, TimeUnit.SECONDS));
        System.out.println(response.sum);

        system.shutdown();
    }

    public static class Sum implements Serializable {
        int sum;

        public Sum(int sum) {
            this.sum = sum;
        }
    }

    public static class GetSum implements Serializable {
    }

    public static class Add implements Serializable {
        String value;

        public Add(String value) {
            this.value = value;
        }
    }

}