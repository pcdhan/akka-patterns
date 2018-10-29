package apps;
import actors.RemoteActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
public class RemoteApp {
    public static void main(String[] args) {
        Config config = ConfigFactory
                .parseString("akka.remote.netty.tcp.hostname=localhost\n" + "akka.remote.netty.tcp.port=2551\n")
                .withFallback(ConfigFactory.load());
        ActorSystem system = ActorSystem.create("ClusterSystem", config);
        System.out.println("Creating System Actor: " + system);

        System.out.println("Starting Child Actor");
        final ActorRef actorA = system.actorOf(RemoteActor.props(), "ActorA");
        final ActorRef actorB = system.actorOf(RemoteActor.props(), "ActorB");
        System.out.println("Started actors: " + actorA);
        System.out.println("Started actors: " + actorB);
    }
}
