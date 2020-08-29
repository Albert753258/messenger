package ru.albert.easychat.glassfish;

import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.http.server.AddOn;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.HttpServerFilter;
import org.glassfish.grizzly.http.server.NetworkListener;

/**
 * WebSockets {@link AddOn} for the {@link HttpServer}.
 * 
 * @author Alexey Stashok
 */
class WebSocketAddOn implements AddOn {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setup(NetworkListener networkListener, FilterChainBuilder builder) {
        // Get the index of HttpServerFilter in the HttpServer filter chain
        final int httpServerFilterIdx = builder.indexOfType(HttpServerFilter.class);

        if (httpServerFilterIdx >= 0) {
            // Insert the WebSocketFilter right before HttpServerFilter
            builder.add(httpServerFilterIdx, new WebSocketFilter());
        }
    }
}
