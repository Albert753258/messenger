package ru.albert.easychat.glassfish;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.DeploymentException;

import org.glassfish.tyrus.core.TyrusEndpoint;
import org.glassfish.tyrus.spi.SPIEndpoint;
import org.glassfish.tyrus.spi.SPIHandshakeListener;
import org.glassfish.tyrus.spi.SPIRegisteredEndpoint;
import org.glassfish.tyrus.spi.TyrusClientSocket;
import org.glassfish.tyrus.spi.TyrusContainer;
import org.glassfish.tyrus.spi.TyrusServer;
import org.glassfish.tyrus.websockets.WebSocketEngine;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;

/**
 * @author Danny Coward (danny.coward at oracle.com)
 */
public class GrizzlyEngine implements TyrusContainer {

    public static final String SSL_ENGINE_CONFIGURATOR = "org.glassfish.tyrus.client.sslEngineConfigurator";

    //The same value Grizzly is using for socket timeout.
    private static final long CLIENT_SOCKET_TIMEOUT = 30000;
    private final WebSocketEngine engine;

    /**
     * Creates Grizzly engine.
     */
    public GrizzlyEngine() {
        engine = WebSocketEngine.getEngine();
    }

    @Override
    public TyrusServer createServer(String rootPath, int port) {
        final HttpServer server = HttpServer.createSimpleServer(rootPath, port);
        server.getListener("grizzly").registerAddOn(new WebSocketAddOn());
        return new TyrusServer() {
            @Override
            public void start() throws IOException {
                server.start();
            }

            @Override
            public void stop() {
                server.stop();
            }

            @Override
            public SPIRegisteredEndpoint register(SPIEndpoint endpoint) throws DeploymentException {
                TyrusEndpoint ge = new TyrusEndpoint(endpoint);
                engine.register(ge);
                return ge;
            }

            @Override
            public void unregister(SPIRegisteredEndpoint ge) {
                engine.unregister((TyrusEndpoint) ge);
            }
        };
    }

    @Override
    public TyrusClientSocket openClientSocket(String url, ClientEndpointConfig cec, SPIEndpoint endpoint,
                                              SPIHandshakeListener listener, Map<String, Object> properties) {
        URI uri;

        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            // TODO - report error
            return null;
        }

        GrizzlyClientSocket clientSocket = new GrizzlyClientSocket(endpoint, uri, cec, CLIENT_SOCKET_TIMEOUT, listener,
                properties == null ? null : (SSLEngineConfigurator) properties.get(SSL_ENGINE_CONFIGURATOR),
                properties == null ? null : (String) properties.get(GrizzlyClientSocket.PROXY_URI),
                properties == null ? null : (ThreadPoolConfig) properties.get(GrizzlyClientSocket.WORKER_THREAD_POOL_CONFIG),
                properties == null ? null : (ThreadPoolConfig) properties.get(GrizzlyClientSocket.SELECTOR_THREAD_POOL_CONFIG));
        clientSocket.connect();
        return clientSocket;
    }
}