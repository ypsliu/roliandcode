/*
 * Copyright 2016 leon chen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.roiland.platform.redis.replicator;

import com.roiland.platform.redis.replicator.cmd.*;
import com.roiland.platform.redis.replicator.io.RedisInputStream;
import com.roiland.platform.redis.replicator.io.RedisOutputStream;
import com.roiland.platform.redis.replicator.rdb.RdbParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.roiland.platform.redis.replicator.Constants.DOLLAR;
import static com.roiland.platform.redis.replicator.Constants.STAR;

/**
 * Created by leon on 8/9/16.
 */
public class RedisSocketReplicator extends AbstractReplicator {

    private static final Log logger = LogFactory.getLog(RedisSocketReplicator.class);

    private final String host;
    private final int port;
    private final Configuration configuration;
    private RedisOutputStream outputStream;
    private Socket socket;
    private ReplyParser replyParser;

    private final AtomicBoolean connected = new AtomicBoolean(false);

    public RedisSocketReplicator(String host, int port, Configuration configuration) throws IOException {
        this.host = host;
        this.port = port;
        this.configuration = configuration;
        if (configuration.getAuthPassword() != null) auth(configuration.getAuthPassword());
        buildInCommandParserRegister();
    }

    private void connect() throws IOException {
        if (!connected.compareAndSet(false, true)) return;

        socket = new Socket();
        socket.setReuseAddress(true);
        socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);
        socket.setSoLinger(true, 0);
        if (configuration.getReadTimeout() > 0) {
            socket.setSoTimeout(configuration.getReadTimeout());
        }
        if (configuration.getReceiveBufferSize() > 0) {
            socket.setReceiveBufferSize(configuration.getReceiveBufferSize());
        }
        if (configuration.getSendBufferSize() > 0) {
            socket.setSendBufferSize(configuration.getSendBufferSize());
        }
        socket.connect(new InetSocketAddress(host, port), configuration.getConnectionTimeout());
        outputStream = new RedisOutputStream(socket.getOutputStream());
        inputStream = new RedisInputStream(socket.getInputStream(), configuration.getBufferSize(), configuration.getRetries());
        replyParser = new ReplyParser(inputStream);
    }

    @Override
    public void open() throws IOException {
        send("SYNC".getBytes());
        final Replicator replicator = this;
        //sync dump
        String reply = (String) replyParser.parse(new BulkReplyHandler() {
            @Override
            public String handle(long len, RedisInputStream in) throws IOException {
                if (logger.isDebugEnabled()) logger.debug("RDB dump file size:" + len);
                if (configuration.isDiscardRdbParser()) {
                    logger.info("Discard " + len + " bytes");
                    in.skip(len);
                } else {
                    RdbParser parser = new RdbParser(in, replicator);
                    parser.parse();
                }
                return "OK";
            }
        });
        //sync command
        if (!reply.equals("OK")) throw new AssertionError("SYNC failed." + reply);
        while (connected.get()) {
            Object obj = replyParser.parse();
            //command
            if (obj instanceof Object[]) {
                if (logger.isDebugEnabled()) logger.debug(Arrays.deepToString((Object[]) obj));

                Object[] command = (Object[]) obj;
                CommandName cmdName = CommandName.name((String) command[0]);
                Object[] params = new Object[command.length - 1];
                System.arraycopy(command, 1, params, 0, params.length);

                //no register .ignore
                if (commands.get(cmdName) == null) continue;

                //do command replyParser
                CommandParser<? extends Command> operations = commands.get(cmdName);
                Command parsedCommand = operations.parse(cmdName, params);

                //do command filter
                if (!doCommandFilter(parsedCommand)) continue;

                //do command handler
                doCommandHandler(parsedCommand);
            } else {
                if (logger.isInfoEnabled()) logger.info("Redis reply:" + obj);
            }
        }
    }

    public void auth(String password) throws IOException {
        if (password != null) {
            send("AUTH".getBytes(), password.getBytes());
            String reply = (String) replyParser.parse();
            if (reply.equals("OK")) return;
            throw new AssertionError("AUTH failed." + reply);
        }
    }

    public void send(byte[] command) throws IOException {
        send(command, new byte[0][]);
    }

    public void send(byte[] command, final byte[]... args) throws IOException {
        connect();
        outputStream.write(STAR);
        outputStream.write(String.valueOf(args.length + 1).getBytes());
        outputStream.writeCrLf();
        outputStream.write(DOLLAR);
        outputStream.write(String.valueOf(command.length).getBytes());
        outputStream.writeCrLf();
        outputStream.write(command);
        outputStream.writeCrLf();
        for (final byte[] arg : args) {
            outputStream.write(DOLLAR);
            outputStream.write(String.valueOf(arg.length).getBytes());
            outputStream.writeCrLf();
            outputStream.write(arg);
            outputStream.writeCrLf();
        }
        outputStream.flush();
    }

    public Object reply() throws IOException {
        return replyParser.parse();
    }

    public Object reply(BulkReplyHandler handler) throws IOException {
        return replyParser.parse(handler);
    }

    @Override
    public void close() throws IOException {
        if (!connected.compareAndSet(true, false)) return;
        if (inputStream != null) inputStream.close();
        if (outputStream != null) outputStream.close();
        if (socket != null && !socket.isClosed()) socket.close();
        if (logger.isInfoEnabled()) logger.info("channel closed");
    }
}
