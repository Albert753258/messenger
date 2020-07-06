package ru.albert;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainTest {
    public static void main(String[] args) throws IOException {
        Socket client = new Socket("localhost", 1111);
        PrintWriter toServer = new PrintWriter(client.getOutputStream(), true);
        toServer.println("get");
    }
}
