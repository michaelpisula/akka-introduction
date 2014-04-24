import akka.actor.*;
import akka.persistence.Channel;
import akka.persistence.ConfirmablePersistent;
import akka.persistence.Deliver;
import akka.persistence.Persistent;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;


public class GuaranteedEcho extends UntypedActor {
    int counter = 0;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ConfirmablePersistent) {
            if (++counter > 2) {
                ((ConfirmablePersistent) message).confirm();
                getSender().tell(((ConfirmablePersistent) message).payload(), getSelf());
            } else {
                System.out.println("Ask me again");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create();
        Inbox inbox = Inbox.create(system);

        ActorRef echoActor = system.actorOf(Props.create(GuaranteedEcho.class), "echoActor");
        ActorRef channel = system.actorOf(Channel.props());
        channel.tell(Deliver.create(Persistent.create("Hello Echo!"), echoActor.path()), inbox.getRef());

        Object payload = inbox.receive(Duration.create(30, TimeUnit.SECONDS));
        System.out.println(payload);
        system.shutdown();

    }
}
