import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.persistence.Persistent;
import akka.persistence.UntypedProcessor;
import scala.Option;
import scala.concurrent.duration.Duration;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class PersistingAdder extends UntypedProcessor {
    private int sum = 0;
    private long seqNr = 0;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Persistent) {
            seqNr = ((Persistent) message).sequenceNr();
            Object payload = ((Persistent) message).payload();
            if (payload instanceof Add) {
                sum += Integer.parseInt(((Add) payload).value);
            }
        } else if (message instanceof GetSum) {
            getSender().tell(new Sum(sum), getSelf());
            sum = 0;
            deleteMessages(seqNr);
        }
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) {
        if (message.isDefined() && message.get() instanceof Persistent) {
            deleteMessage(((Persistent) message.get()).sequenceNr());
        }
        super.preRestart(reason, message);
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("AdderSystem");
        ActorRef add = system.actorOf(Props.create(PersistingAdder.class));
        Inbox inbox = Inbox.create(system);

        inbox.send(add, Persistent.create(new Add("1")));
        inbox.send(add, Persistent.create(new Add("2")));
        inbox.send(add, Persistent.create(new Add("3")));
        inbox.send(add, Persistent.create(new Add("four")));
        inbox.send(add, Persistent.create(new Add("5")));

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
