package ru.albert;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private ExecutorService exec;
    public BufferedWriter writer;
    BufferedReader clientReader = null;
    BufferedWriter clientWriter;
    public BufferedReader reader;
    public void start(int port){
        exec = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(port);
            new Thread(accepter).start();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Runnable accepter = new Runnable() {
        public void run() {
            try {
                while (true){
                    Socket client = serverSocket.accept();
                    exec.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                String request = clientReader.readLine();
                                String[] arr = request.split(" ");
                                if(arr[0].equals("send")){
                                    writer = new BufferedWriter(new FileWriter("file", true));
                                    for(int i = 1; i < arr.length; i++){
                                        writer.write(arr[i] + " ");
                                    }
                                    writer.write("\n");
                                    writer.close();
                                    System.out.println("Success");
                                }
                                else if(arr[0].equals("get")){
                                    clientWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                                    reader = new BufferedReader(new FileReader("file"));
                                    String str = reader.readLine();
                                    while (str != null){
                                        clientWriter.write(str + "\n");
                                        str = reader.readLine();
                                    }
                                    clientWriter.flush();
                                    System.out.println("Success");
                                }
                            } catch (IOException e) {
                                throw new IllegalStateException(e);
                            }
                        }
                    });
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    };
}
