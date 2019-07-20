package com.roiland.platform.socket.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.BoundedMessageQueueSemantics;
import akka.dispatch.RequiresMessageQueue;
import com.roiland.platform.socket.bean.DownStreamBean;
import com.roiland.platform.socket.bean.ProtocolBean;
import io.netty.channel.Channel;

public class Dispatcher extends UntypedActor implements RequiresMessageQueue<BoundedMessageQueueSemantics> {

    private Channel channel = null;
    public Dispatcher(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof ProtocolBean) {
            getContext().actorOf(Props.create(Response.class, channel)).tell(o, getSelf());
        } else {
            unhandled(o);
        }
    }
}
