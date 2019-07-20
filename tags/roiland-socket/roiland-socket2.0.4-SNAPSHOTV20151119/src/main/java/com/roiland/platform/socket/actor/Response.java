package com.roiland.platform.socket.actor;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.BoundedMessageQueueSemantics;
import akka.dispatch.RequiresMessageQueue;
import com.roiland.platform.socket.bean.DownStreamBean;
import com.roiland.platform.socket.bean.ProtocolBean;
import io.netty.channel.Channel;

public class Response extends UntypedActor implements RequiresMessageQueue<BoundedMessageQueueSemantics> {

    private Channel channel;
    public Response(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void onReceive(Object o) throws Exception {
        try {
            if (o instanceof ProtocolBean) {
                final ProtocolBean protocol = (ProtocolBean)o;
                final byte[] body = protocol.getBody();
                if (body[0] == 0x09 && body[1] == 0x00 && body[2] == 0x01) {
                    channel.writeAndFlush(new DownStreamBean(protocol.getTarget(), protocol.getSource(), 0, body[3]));
                }
            } else {
                unhandled(o);
            }
        } finally {
            getContext().stop(getSelf());
        }
    }
}
