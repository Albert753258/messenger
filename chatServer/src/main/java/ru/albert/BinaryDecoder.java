package ru.albert;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

public class BinaryDecoder implements Decoder.Binary {

    @Override
    public Object decode(ByteBuffer byteBuffer) throws DecodeException {
        try {
            String[] arr = (String[]) byteBufferToObject(byteBuffer);
            return byteBufferToObject(byteBuffer);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean willDecode(ByteBuffer byteBuffer) {
        return false;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
    public static Object byteBufferToObject(ByteBuffer byteBuffer)
            throws Exception {
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        Object object = deSerializer(bytes);
        return object;
    }

    public static Object deSerializer(byte[] bytes) throws IOException,
            ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new ByteArrayInputStream(bytes));
        return objectInputStream.readObject();
    }
}
