package it.fi.meucci;

import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
    public void avvioServer(){
        try {
            ServerSocket serverSckt = new ServerSocket(6789);
            for (;;){
                System.out.println("Server Thread avviato...");
                Socket socket = serverSckt.accept();
                ServerThread serverMultiThread = new ServerThread(socket);
                serverMultiThread.start();
            }
        } catch (Exception e) {
            System.out.println("Errore durante istanza del server");
        }
    }
}
