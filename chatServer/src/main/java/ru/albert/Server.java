package ru.albert;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.LinkedList;
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
        public LinkedList<Socket> clients = new LinkedList<>();
        public void run() {
            try {
                while (true){
                    Socket client = serverSocket.accept();
                    clients.add(client);
                    exec.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                clientWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
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
                                    synchronized (clients){
                                        for(Socket socket: clients){
                                            try {
                                                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                                                for(int i = 1; i < arr.length; i++){
                                                    wr.write(arr[i] + "\n");
                                                }
                                                wr.write("\f");
                                                wr.flush();
                                                wr.close();
                                            }
                                            catch (Exception e){
                                                //clients.remove(socket);
                                            }
                                        }
                                    }
                                    System.out.println("Success");
                                }
                                else if(arr[0].equals("get")){
                                    reader = new BufferedReader(new FileReader("file"));
                                    String str = reader.readLine();
                                    while (str != null){
                                        clientWriter.write(str + "\n");
                                        str = reader.readLine();
                                    }
                                    clientWriter.write("\f\n");
                                    clientWriter.flush();
                                    System.out.println("Success");
                                }
                            } catch (IOException e) {
                            }
                        }
                    });
                }
            } catch (IOException e) {
            }
        }
    };
}
