//package es.neil.networking;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import java.net.*;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
/**[PriceMatchManagement.java]
 * This is final project - price match program
 * This class contains the main method and the host server to start the program
 * 
 * @author Kylie Wong and Michelle Chan, ICS4UE
 * @version 1.0, build May 27, 2022
 */

public class HostManagement{
    private HashMap<String, User> userHashMap = new HashMap<String, User>();
    
    final int PORT = 6666;
    ServerSocket serverSocket;
    Socket clientSocket;
    PrintWriter output;
    BufferedReader input;
//----------------------------------------------------------------------------
    public static void main(String[] args) throws Exception{
        DataBase database = new DataBase();
        HostManagement hostServer = new HostManagement();
        hostServer.start();
        
    }
    HostManagement(){
        this.userHashMap.put("trying", new User());
    }
//----------------------------------------------------------------------------
    public boolean registerUser(String id, String password){
        if(userHashMap.containsKey(id)){
            return false; //false - userId taken 
        }else{
            //userHashMap.put(id,password);
        }
        return true;
    }
//----------------------------------------------------------------------------
    private void start() throws Exception{ 
        //create a socket with the local IP address and wait for connection request       
        System.out.println("Waiting for a connection request from a client ...");
        serverSocket = new ServerSocket(PORT);                //create and bind a socket
        while(true){
            clientSocket = serverSocket.accept();             //wait for connection request
            System.out.println("Client connected");
            Thread connectionThread = new Thread(new ConnectionHandler(clientSocket));
            connectionThread.start();                         //start a new thread to handle the connection
        }
    }
//----------------------------------------------------------------------------
    public boolean userLogin(String userId){
        return false;
    }
//------------------------------------------------------------------------------
    class ConnectionHandler extends Thread { 
        Socket socket;
        PrintWriter output;
        BufferedReader input;
        OutputStream outputStream;
        
        public ConnectionHandler(Socket socket) { 
            this.socket = socket;
        }
        @Override 
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream());
                outputStream = socket.getOutputStream();
                
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(userHashMap);
                
                //receive a message from the client
                String msg = input.readLine();
                System.out.println("Message from the client: " + msg);
                //send a response to the client
                output.println("Client you are connected!");
                output.flush();         
                while((msg = input.readLine())!=null){
                    System.out.println(msg);
                }
                //after completing the communication close the streams but do not close the socket!
//                input.close();
//                output.close();
            }catch (IOException e) {e.printStackTrace();}
        }
    }  
//------------------------------------------------------------------------------
}