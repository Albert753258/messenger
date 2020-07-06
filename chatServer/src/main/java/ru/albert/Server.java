package ru.albert;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    public static LinkedList<Socket> clients = new LinkedList<>();
    private ExecutorService exec;
    BufferedReader reader = null;
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
                    //clients.add(client);
                    exec.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                String request = reader.readLine();
                                String[] arr = request.split(" ");
                                if(arr[0].equals("send")){
                                    Writer out;
                                    BufferedWriter writer = new BufferedWriter(new FileWriter("file", true));
                                    writer.write(arr[1] + "\n");
                                    writer.close();
                                }
                                System.out.println("Success");
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
//    public static void sendWeather(String cityName, Socket client){
//        citySearch.inicialyze();
//        String url = citySearch.getURL(cityName);
//        System.out.println(url);
//        PrintWriter writer = null;
//        try {
//            writer = new PrintWriter(client.getOutputStream(), true);
//        } catch (IOException e) {
//            System.out.printf("Error");;
//        }
//        Document doc = null;
//        try {
//            doc = Jsoup.connect(url).get();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String weather = "";
//        String temperature = "";
//        if (doc!=null) {
//            weather = doc.body().getElementById("wrap").getElementById("pagewrapper").getElementsByClass("content").get(0).getElementsByClass("content_2col").get(0).getElementById("webslice_content").getElementsByClass("entry-content").get(0).getElementsByClass("t_cond").get(0).getElementsByClass("c1").get(0).getElementsByClass("txt-tight").get(0).text();
//            temperature = doc.body().getElementById("wrap").getElementById("pagewrapper").getElementsByClass("content").get(0).getElementsByClass("content_2col").get(0).getElementById("webslice_content").getElementsByClass("entry-content").get(0).getElementsByClass("t_cond").get(0).getElementsByClass("c1").get(0).getElementsByClass("left").get(0).getElementsByClass("txt-xxlarge").text();
//        }
//        else{
//            temperature = "Ошибка";
//            weather = "Ошибка";
//        }
//        String[] arr = weatherAnalyzer(weather);
//        for(String str: arr){
//            writer.println(str);
//        }
//        writer.println(temperature);
//    }
//    public static String[] weatherAnalyzer(String weather){
//        String str[] = new String[9];
//        String arr[] = weather.split("Ощущается");
//        str[0] = arr[0];
//        String arr1[] = arr[1].split("Барометр");
//        str[1] = "Ощущается" + arr1[0];
//        String arr2[] = arr1[1].split("Точка");
//        str[2] = "Давление" + arr2[0];
//        String arr3[] = arr2[1].split("От. влажность");
//        str[3] = "Точка" + arr3[0];
//        String arr4[] = arr3[1].split("Видимость");
//        String arr5[] = null;
//        if(arr4.length == 2){
//            str[4] = "От. влажность" + arr4[0];
//            arr5 = arr4[1].split("Восход");
//            str[5] = "Видимость" + arr5[0];
//            String arr6[] = arr5[1].split("Закат");
//            str[6] = "Восход" + arr6[0];
//            String arr7[] = arr6[1].split("Долгота");
//            str[7] = "Закат" + arr7[0];
//            str[8] = "Долгота" + arr7[1];
//        }
//        else{
//            arr5 = arr3[1].split("Восход");
//            str[4] = "От. влажность" + arr5[0];
//            String arr6[] = arr5[1].split("Закат");
//            str[5] = "Восход" + arr6[0];
//            String arr7[] = arr6[1].split("Долгота");
//            str[6] = "Закат" + arr7[0];
//            str[7] = "Долгота" + arr7[1];
//            str[8] = "";
//        }
//        System.out.println(str);
//        return str;
//    }
}
