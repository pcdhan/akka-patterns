package apps;

import java.util.concurrent.TimeUnit;

import actors.LocalActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Identify;
import akka.japi.Function;
import akka.pattern.Patterns;
import akka.util.Timeout;
import payloads.Payload;
import scala.concurrent.Await;
import scala.concurrent.Future;
public class LocalApp {
    public static void main(String[] args)  throws Exception{
        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.hostname=localhost\n")
                .withFallback(ConfigFactory.load());
        ActorSystem system = ActorSystem.create("ClusterSystem", config);
        System.out.println("Creating System Actor: " + system);

        //Local Actor
        final ActorRef localA  = system.actorOf(LocalActor.props(), "localA");

        //Get A Remote Actor
        Timeout timeout = new Timeout(20000, TimeUnit.MILLISECONDS);
        ActorSelection actorSelection = system.actorSelection("akka.tcp://ClusterSystem@localhost:2551/user/ActorA");
        Future<Object> future = Patterns.ask(actorSelection, new Identify(""), timeout);
        ActorIdentity reply = (ActorIdentity) Await.result(future, timeout.duration());
        ActorRef actorRef = reply.ref().get();
        Boolean exits = (actorRef == null) ? false : true;
        System.out.println("Does Remote Actor Exist: " + exits);

        //Example: Ask.
        Future<Object> ask = Patterns.ask(actorRef, "1", timeout);
        Long response = (Long) Await.result(ask, timeout.duration());
        System.out.println("Response from Remote Actor String: "+response);

        //Example:askWithReplyTo
        //If we want to include sender reference in the payload
        //Need help !!  How do I pass a sender ActorRef Eg. localA in the payload
        Payload payload = new Payload();
        payload.setMsg("0");
        Future<Object> askWithSenderRef = Patterns.askWithReplyTo(actorRef,payload,20000L);
        Payload responsePayload = (Payload) Await.result(askWithSenderRef, timeout.duration());
        System.out.println("Response from Remote Actor Payload: "+responsePayload.getMsg());

    }
}
