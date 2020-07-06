package ru.albert;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start(1111);
    }
}