import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.ConfigFactory;

public class Echo extends UntypedActor {

    private static String configRemote = "akka {\n" +
            "  loglevel = ERROR\n" +
            "  actor {\n" +
            "    provider = \"akka.remote.RemoteActorRefProvider\"\n" +
            "  }\n" +
            "  remote {\n" +
            "    enabled-transports = [\"akka.remote.netty.tcp\"]\n" +
            "    netty.tcp {\n" +
            "      hostname = \"127.0.0.1\"\n" +
            "      port = 2553\n" +
            "    }\n" +
            " }\n" +
            "}";
    private static String configRemoting = "akka {\n" +
            "  loglevel = ERROR\n" +
            "  actor {\n" +
            "    provider = \"akka.remote.RemoteActorRefProvider\"\n" +
            "    deployment {\n" +
            "      /echoActor {\n" +
            "        remote = \"akka.tcp://remoteActorSystem@127.0.0.1:2553\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "  remote {\n" +
            "    enabled-transports = [\"akka.remote.netty.tcp\"]\n" +
            "    netty.tcp {\n" +
            "      hostname = \"127.0.0.1\"\n" +
            "      port = 2552\n" +
            "    }\n" +
            " }\n" +
            "}";

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            System.out.println(getContext().system().name() + " Echo: " + message);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("remotingActorSystem", ConfigFactory.parseString(configRemoting));
        ActorSystem remotingSystem = ActorSystem.create("remoteActorSystem", ConfigFactory.parseString(configRemote));

        ActorRef echoActor = system.actorOf(Props.create(Echo.class), "echoActor");
        echoActor.tell("Hello World", null);

        Thread.sleep(1000);

        system.shutdown();
        remotingSystem.shutdown();

    }
}
