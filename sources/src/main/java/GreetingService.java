import java.util.Map;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.camel.CamelMessage;
import akka.camel.javaapi.UntypedConsumerActor;

public class GreetingService extends UntypedConsumerActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof CamelMessage) {
			CamelMessage camelMsg = (CamelMessage) message;
			Map<String,Object> headers = camelMsg.getHeaders();
			getSender().tell("Hello " + headers.get("greetee"), getSelf());
		}
	}

	@Override
	public String getEndpointUri() {
		return "jetty:http://localhost:4242/greet";
	}
	
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("GreetingService");
	    system.actorOf(Props.create(GreetingService.class));
	}

}
