import akka.actor.*;
import scala.concurrent.duration.Duration;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class Adder extends UntypedActor {

    int sum = 0;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Add) {
            sum += ((Add) message).value;
        } else if (message instanceof GetSum) {
            getSender().tell(new Sum(sum), getSelf());
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("AdderSystem");
        ActorRef add = system.actorOf(Props.create(Adder.class));
        Inbox inbox = Inbox.create(system);

        add.tell(new Add(1), inbox.getRef());
        add.tell(new Add(2), inbox.getRef());
        add.tell(new Add(3), inbox.getRef());

        add.tell(new GetSum(), inbox.getRef());

        Sum response = (Sum) inbox.receive(Duration.create(10, TimeUnit.SECONDS));
        System.out.println(((Sum) response).sum);

        system.shutdown();
    }

    public static class Sum implements Serializable {
        int sum;

        public Sum(int sum) {
            this.sum = sum;
        }

        @Override
        public int hashCode() {
            return Objects.hash(sum);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Sum other = (Sum) obj;
            return Objects.equals(this.sum, other.sum);
        }
    }

    public static class GetSum implements Serializable {
    }

    public static class Add implements Serializable {
        int value;

        public Add(int value) {
            this.value = value;
        }
    }

}
