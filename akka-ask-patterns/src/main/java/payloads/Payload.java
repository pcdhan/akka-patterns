package payloads;

import java.io.Serializable;

import akka.actor.ActorRef;
import akka.japi.Function;

public class Payload implements Function<ActorRef, Object>, Serializable {
    private static final long serialVersionUID = 1L;

    String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public Object apply(ActorRef param) throws Exception {
        return this;
    }

}
