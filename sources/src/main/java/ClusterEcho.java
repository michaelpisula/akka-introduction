import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.routing.ClusterRouterPool;
import akka.cluster.routing.ClusterRouterPoolSettings;
import akka.routing.BroadcastPool;
import com.google.common.collect.Lists;
import com.typesafe.config.ConfigFactory;

import java.util.List;

public class ClusterEcho extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            System.out.println("Echo: " + message);
        }
    }


    private static String getClusterConfig(int port) {
        return "akka {\n" +
                "  loglevel = ERROR\n" +
                "  actor {\n" +
                "    provider = \"akka.cluster.ClusterActorRefProvider\"\n" +
                "  }\n" +
                "  remote {\n" +
                "    log-remote-lifecycle-events = off\n" +
                "    netty.tcp {\n" +
                "      hostname = \"127.0.0.1\"\n" +
                "      port = " + port + "\n" +
                "    }\n" +
                "  }\n" +
                " \n" +
                "  cluster {\n" +
                "    seed-nodes = [\n" +
                "      \"akka.tcp://ClusterSystem@127.0.0.1:2551\",\n" +
                "      \"akka.tcp://ClusterSystem@127.0.0.1:2552\"]\n" +
                " \n" +
                "    auto-down-unreachable-after = 10s\n" +
                "  }\n" +
                "}";
    }

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("ClusterSystem", ConfigFactory.parseString(getClusterConfig(2551)));

        int maxInstancesPerNode = 1;
        int totalInstances = 5;
        boolean allowLocalRoutees = true;
        ActorRef echoActor = system.actorOf(new ClusterRouterPool(
                new BroadcastPool(totalInstances),
                new ClusterRouterPoolSettings(totalInstances, maxInstancesPerNode, allowLocalRoutees, ""))
                .props(Props.create(ClusterEcho.class)),
                "echoActor");

        echoActor.tell("Hello small World", null);
        Thread.sleep(200);
        System.out.println("Making world bigger...");

        List<ActorSystem> systemList = Lists.newArrayList();
        systemList.add(ActorSystem.create("ClusterSystem", ConfigFactory.parseString(getClusterConfig(2552))));
        system.actorOf(Props.create(SimpleClusterListener.class),
                "clusterListener");
        for (int i = 0; i < 3; i++)
            systemList.add(ActorSystem.create("ClusterSystem", ConfigFactory.parseString(getClusterConfig(0))));


        Thread.sleep(10000);

        echoActor.tell("Hello World", null);
        Thread.sleep(2000);

        System.out.println("Making world smaller...");
        for (ActorSystem actorSystem : systemList)
            actorSystem.shutdown();

        Thread.sleep(2000);

        echoActor.tell("Hello small World", null);

        Thread.sleep(2000);

        system.shutdown();

    }
}
