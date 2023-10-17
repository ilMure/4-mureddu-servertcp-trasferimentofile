package it.fi.meucci;
import java.net.*;
import java.io.*;

public class ServerThread extends Thread{
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    private BufferedReader dataFromClient;
    private DataOutputStream dataToClient;

    public ServerThread(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    public void run(){
        try{
            this.comunica();
        }catch(Exception e){
            System.out.println("Errore nella comunicazione");
        }
    }

    protected void comunica() throws IOException{
        dataToClient = new DataOutputStream(clientSocket.getOutputStream());

        //ottenimento stringa contenente tutti i files
        String listOfNames = listaFile();
        //invio nomi file disponibili
        dataToClient.writeBytes(listOfNames + "\n");
        //leggo la scelta dell'utente
        dataFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        String nomeFileSceltoDaUtente = dataFromClient.readLine();
        
        String nomeFile = "src/main/resources/" + nomeFileSceltoDaUtente; 

        trasmettiFile(nomeFile);

        System.out.println("File inviato.");

        clientSocket.close();

        System.out.println("Server Thread terminato");
        this.interrupt();
    }

    private String listaFile(){
        File resourcesDir = new File("src/main/resources");

        File[] files = resourcesDir.listFiles();

        String listOfNames = "";
        for (File file:files ) {
            listOfNames += ", " + file.getName();
        }

        return listOfNames;
    }

    private void trasmettiFile(String path) throws IOException{
        // stream di scrittura sul socket
        ObjectOutputStream writer = new ObjectOutputStream(clientSocket.getOutputStream());

        // stream di lettura dal file
        FileInputStream reader = new FileInputStream(path);

        //leggo i byte dal file e li scrivo sul socket
        byte[] buffer = new byte[1024];
        int lengthRead;
        while ((lengthRead = reader.read(buffer)) > 0) {
            writer.write(buffer, 0, lengthRead);
        }
        writer.close();
        reader.close();
    }
}