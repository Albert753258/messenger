package ru.albert;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(1111);
//        Socket client = new Socket("localhost", 1111);
//        PrintWriter toServer = new PrintWriter(client.getOutputStream(), true);
//        toServer.println("send wfwefwr");
    }
}