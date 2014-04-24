import akka.actor.*;
import akka.actor.SupervisorStrategy.Directive;
import akka.japi.Function;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class Supervisor extends UntypedActor {

  private SupervisorStrategy strategy = new OneForOneStrategy( 10,
                                                               Duration.create( 5, TimeUnit.SECONDS ),
                                                               new Function<Throwable, SupervisorStrategy.Directive>() {

                                                                 @Override
                                                                 public Directive apply( Throwable t ) throws Exception {
                                                                   System.out.println( getSender().path() + " threw " + "an exception" );
                                                                   if ( t instanceof NumberFormatException ) {
                                                                     return SupervisorStrategy.resume();
                                                                   }

                                                                   return SupervisorStrategy.restart();
                                                                 }
                                                               }
  );

  @Override
  public SupervisorStrategy supervisorStrategy() {
    return strategy;
  }

  public Supervisor() {
    getContext().actorOf( Props.create( StringAdder.class ), "Adder" );

  }

  @Override
  public void onReceive( Object message ) throws Exception {
    unhandled( message );
  }

  public static void main( String[] args ) throws Exception {
    ActorSystem system = ActorSystem.create( "SupervisorSystem" );
    ActorRef supervisor = system.actorOf( Props.create( Supervisor.class ), "Supervisor" );

    Future<ActorRef> actorFuture = system.actorSelection( supervisor.path()
                                                                    .child( "Adder" ) ) //"/user/Supervisor/Adder"
        .resolveOne( Duration.create( 5, TimeUnit.SECONDS ) );

    ActorRef add = Await.result( actorFuture, Duration.create( 5, TimeUnit.SECONDS ) );

    Inbox inbox = Inbox.create( system );

    inbox.send( add, new StringAdder.Add( "1" ) );
    inbox.send( add, new StringAdder.Add( "2" ) );
    inbox.send( add, new StringAdder.Add( "3" ) );
    inbox.send( add, new StringAdder.Add( "four" ) );
    inbox.send( add, new StringAdder.Add( "5" ) );

    inbox.send( add, new StringAdder.GetSum() );

    StringAdder.Sum response = (StringAdder.Sum) inbox.receive( Duration.create( 10, TimeUnit.SECONDS ) );
    System.out.println( response.sum );

    system.shutdown();
  }

}