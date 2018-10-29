package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import payloads.Payload;

public class RemoteActor extends AbstractActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    Cluster cluster = Cluster.get(getContext().system());

    static public Props props() {
        return Props.create(RemoteActor.class, () -> new RemoteActor());
    }

    // subscribe to cluster changes
    @Override
    public void preStart() {
        cluster.subscribe(self(), ClusterEvent.initialStateAsEvents(), MemberEvent.class, UnreachableMember.class);
    }

    // re-subscribe when restart
    @Override
    public void postStop() {
        cluster.unsubscribe(self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(MemberUp.class, mUp -> {
            log.info("Member is Up: {}", mUp.member());
        }).match(UnreachableMember.class, mUnreachable -> {
            log.info("Member detected as unreachable: {}", mUnreachable.member());
        }).match(MemberRemoved.class, mRemoved -> {
            log.info("Member is Removed: {}", mRemoved.member());
        }).match(MemberEvent.class, message -> {
            // ignore
        }).match(String.class, message -> {
            Long payload = Long.valueOf(message);
            payload = payload + 1;
            log.info("I am " + getSelf() + ", To " + getSender() + ", Payload: " + payload);
            getSender().tell(payload, getSelf());
        }).match(Payload.class, payload -> {
            Long p = Long.valueOf(payload.getMsg());
            p = p + 1;
            log.info("I am " + getSelf() + ", To " + getSender() + ", Payload: " + payload.getMsg());
            payload.setMsg(String.valueOf(p));
            getSender().tell(payload, getSelf());

        }).build();
    }
}
