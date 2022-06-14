import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import java.io.*;
import java.net.*;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.FileReader;
import java.io.BufferedReader;

/**[PriceMatchManagement.java]
 * This is final project - price match program
 * This class contains the main method and the host server to start the program
 * http://helloraspberrypi.blogspot.com/2013/12/pass-arraylist-of-object-in-socket.html
 * 
 * @author Kylie Wong and Michelle Chan, ICS4UE
 * @version 1.0, build May 27, 2022
 */

public class HostManagement{
    private HashMap<String, ArrayList<String>> userIDRefNoMap = new HashMap<String, ArrayList<String>>();
    private HashMap<String, String> userPasswordMap = new HashMap<String, String>();
    private HashMap<String, ArrayList<String>> refNoMap = new HashMap<String, ArrayList<String>>(); // arraylist: itemInfo, users
    
    private DataBase database;
    static FileReader fileReader; 
    static BufferedReader input;
    
    final int PORT = 6666;
    ServerSocket serverSocket;
    Socket clientSocket;
    
//----------------------------------------------------------------------------
    public static void main(String[] args){
        HostManagement hostServer = new HostManagement();
        
    }
    HostManagement(){
        this.database = new DataBase();
        this.setUp();
        try{
            this.start();
        }catch(Exception e){  e.printStackTrace(); }
    }
//----------------------------------------------------------------------------
    private void setUp(){
        String fileName = "resources/UserInfo.txt";
        try{
            fileReader = new FileReader(fileName);
            input = new BufferedReader(fileReader);
            //read the first line "User Information Sheet (#. userID: Password OrderRefNo)"of the file that is useless
            input.readLine(); 
            while(input.ready()){    //keep reading lines while the End-of-File hasn't been reached
                String name = input.readLine(); 
                String password = name.substring(name.indexOf(":")+2);
                name = name.substring(3, name.indexOf(":"));
                userPasswordMap.put(name, password);
            }
            input.close();
        }catch (Exception e){
            System.out.println("ERROR - file '" + fileName + "' not found. ");
        }
        System.out.println("setting up");
        makeGroupOrder("Asparagus 1 kg = $9.09 @ CostCo~A~0.4~");
        makeGroupOrder("Choice Green Peppers 1 1/9 bushel = $44.29 @ CostCo~A~0.6~");
    }
//----------------------------------------------------------------------------
    private boolean loginUser(String id, String password){
        if(userPasswordMap.containsKey(id)){
            String correctPassword = userPasswordMap.get(id);
            if(password.equals(correctPassword)){         // password correct
                return true;
            }
        }
        return false;
    }
//----------------------------------------------------------------------------
    private boolean makeGroupOrder(String msg){
        String itemInfo = msg.substring(0, msg.indexOf(Const.SPLIT));
        String itemName = itemInfo.substring(0, itemInfo.indexOf(" = "));
        String price = itemInfo.substring(itemInfo.indexOf(" = ") +3 , itemInfo.indexOf(" @ "));
        String location =  itemInfo.substring(itemInfo.indexOf(" @ ") +3);
        msg = msg.substring(msg.indexOf(Const.SPLIT)+1);
        String userID = msg.substring(0, msg.indexOf(Const.SPLIT));
        msg = msg.substring(msg.indexOf(Const.SPLIT)+1);
        String amountPercentage = msg.substring(0, msg.indexOf(Const.SPLIT));
        System.out.println("item: " + itemInfo + " userId: " + userID + " amount%: " + amountPercentage);
        /**********Create arraylist to store them into refNoMap and userIDRefNoMap*************/
        ArrayList<String> infoList = new ArrayList<String>();
        infoList.add(0, itemName);
        infoList.add(1, price);
        infoList.add(2, location); //add time, and date
        infoList.add(userID + Const.SPLIT + amountPercentage);
        String refNo = generateUniqueRefNo(itemName);
        refNoMap.put(refNo, infoList);
        ArrayList<String> user_listOfRefNo = new ArrayList<String>(); 
        if(userIDRefNoMap.containsKey(userID)){
            user_listOfRefNo = userIDRefNoMap.get(userID);
            user_listOfRefNo.add(refNo);
        }else{
            user_listOfRefNo.add(refNo);
            userIDRefNoMap.put(userID, user_listOfRefNo);
        }
        return true;
    }
//----------------------------------------------------------------------------
    private String generateUniqueRefNo(String itemInfo){
        int hash_code = Math.abs(itemInfo.hashCode());
        System.out.println(hash_code);
        String refNo = Integer.toString(hash_code);
        if(refNoMap.containsKey(refNo)){
            System.out.println("pick a new number"); //gerneate a new number
        }
        return refNo;
    }
//----------------------------------------------------------------------------
    private boolean registerUser(String id, String password){
        if(userPasswordMap.containsKey(id)){
            return false; //false - userId taken 
        }else{
            userPasswordMap.put(id, password);
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
//------------------------------------------------------------------------------
    class ConnectionHandler extends Thread { 
        Socket socket;
        PrintWriter output;
        BufferedReader input;
        
        ObjectOutputStream objectOutput;
        ObjectInputStream objectInput;
        String msg;
        
        public ConnectionHandler(Socket socket) { 
            this.socket = socket;
        }
        @Override 
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream());
                objectOutput = new ObjectOutputStream(socket.getOutputStream());
                objectInput = new ObjectInputStream(socket.getInputStream());
                
                output.println("Client you are connected!");
                output.flush();         
                
                while((msg = input.readLine())!=null){
                    System.out.println(msg);
                    if(msg.substring(0, Const.LOGIN.length()).equals(Const.LOGIN)){
                        String userName = msg.substring(Const.LOGIN.length(), msg.indexOf("~"));
                        String password = msg.substring(msg.indexOf(Const.SPLIT) +1);
                        boolean resultOfLogin = loginUser(userName, password);
                        System.out.println(resultOfLogin);
                        output.println(resultOfLogin);
                        output.flush();
                    }else if(msg.substring(0, Const.SEARCH_KEYWORD.length()).equals(Const.SEARCH_KEYWORD)){
                        String keyword = msg.substring(Const.SEARCH_KEYWORD.length());
                        ArrayList<String> resultList = DataBase.searchItemKeyword(keyword);
                        objectOutput.writeObject(resultList);
                    }else if (msg.substring(0, Const.KEYWORD_LIST.length()).equals(Const.KEYWORD_LIST)){
                        ArrayList<String> keywordList = DataBase.getWholeList();
                        objectOutput.writeObject(keywordList);
                    }else if(msg.substring(0, Const.GROUP_ORDER.length()).equals(Const.GROUP_ORDER)){
                        msg = msg.substring(Const.GROUP_ORDER.length());
                        makeGroupOrder(msg);
                        System.out.println("map: " + userIDRefNoMap.toString() + "\nanother map: " + refNoMap.toString());
                    }else if (msg.substring(0, Const.GROUP_REFRESH.length()).equals(Const.GROUP_REFRESH)){
                        objectOutput.writeObject(refNoMap);
                        objectOutput.flush();
                    }
                }
                //after completing the communication close the streams but do not close the socket!
                input.close();
                output.close();
            }catch (IOException e) {e.printStackTrace();}
        }
    }  
//------------------------------------------------------------------------------
}