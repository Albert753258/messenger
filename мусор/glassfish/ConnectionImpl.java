package ru.albert.easychat.glassfish;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

import org.glassfish.tyrus.websockets.Connection;
import org.glassfish.tyrus.websockets.DataFrame;
import org.glassfish.tyrus.websockets.WebSocketResponse;
import org.glassfish.tyrus.websockets.WriteFuture;

import org.glassfish.grizzly.Closeable;
import org.glassfish.grizzly.EmptyCompletionHandler;
import org.glassfish.grizzly.ICloseType;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.http.HttpContent;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.http.HttpResponsePacket;
import org.glassfish.grizzly.http.Protocol;

/**
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
class ConnectionImpl extends Connection {

    private final FilterChainContext ctx;
    private final HttpContent httpContent;
    private final org.glassfish.grizzly.Connection connection;

    public ConnectionImpl(final FilterChainContext ctx, final HttpContent httpContent) {
        this.ctx = ctx;
        this.connection = ctx.getConnection();
        this.httpContent = httpContent;
    }

    public ConnectionImpl(final org.glassfish.grizzly.Connection connection) {
        this.connection = connection;
        this.ctx = null;
        this.httpContent = null;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Future<DataFrame> write(final DataFrame frame, final CompletionHandler completionHandler) {
        if (!connection.isOpen()) {
            return null;
        }

        final WriteFuture<DataFrame> future = new WriteFuture<DataFrame>();
        connection.write(frame, new EmptyCompletionHandler() {

            @Override
            public void completed(Object result) {
                if (completionHandler != null) {
                    completionHandler.completed(frame);
                }

                future.setResult(frame);
            }

            @Override
            public void failed(Throwable throwable) {
                if (completionHandler != null) {
                    completionHandler.failed(throwable);
                }

                future.setFailure(throwable);
            }
        });

        return future;
    }

    @Override
    public void write(WebSocketResponse response) {
        if (ctx == null) {
            throw new UnsupportedOperationException("not supported on client side");
        }

        final HttpResponsePacket responsePacket = ((HttpRequestPacket) httpContent.getHttpHeader()).getResponse();
        responsePacket.setProtocol(Protocol.HTTP_1_1);
        responsePacket.setStatus(response.getStatus());

        for (Map.Entry<String, String> entry : response.getHeaders().entrySet()) {
            responsePacket.setHeader(entry.getKey(), entry.getValue());
        }

        ctx.write(HttpContent.builder(responsePacket).build());
    }

    @Override
    public void addCloseListener(final CloseListener closeListener) {

        final org.glassfish.tyrus.websockets.Connection webSocketConnection = this;

        connection.addCloseListener(new org.glassfish.grizzly.CloseListener() {
            @Override
            public void onClosed(Closeable closeable, ICloseType iCloseType) throws IOException {
                closeListener.onClose(webSocketConnection);
            }
        });
    }

    @Override
    public void closeSilently() {
        connection.closeSilently();
    }

    @Override
    public int hashCode() {
        return connection.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Connection && connection.equals(((Connection) obj).getUnderlyingConnection());
    }

    @Override
    public Object getUnderlyingConnection() {
        return connection;
    }

    public String toString() {
        return this.getClass().getName() + " " + connection.toString() + " " + connection.hashCode();
    }
}