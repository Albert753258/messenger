package ru.albert;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainTest {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1111);
        Socket client = serverSocket.accept();
        PrintWriter toServer = new PrintWriter(client.getOutputStream(), true);
        toServer.println("sefse");
        toServer.flush();
    }
}
