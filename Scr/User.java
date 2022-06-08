import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.InputStream;
/**[User.java]
 * This is final project - price match program
 * This class represent the object - User
 * @author Kylie Wong and Michelle Chan, ICS4UE
 * @version 1.0, build June 2, 2022
 */

public class User{
    private String password;
    private String userID;
    private static ArrayList<String> requestGroupOrderList;
    private ArrayList<String> committedGroupOrderList;
    
    final String LOCAL_HOST = "127.0.0.1";
    final int PORT = 6666;
    Socket clientSocket;
    PrintWriter output;    
    BufferedReader input;
    
    public static void main(String[] args){
        
        //start homepage here, for user
        User user = new User("jel", "ds");
        user.userLogin("", "");
       /* Scanner userInput = new Scanner(System.in);
        String line = userInput.nextLine();
        while(!line.equals("bye")){
            if(line.equals("login")){
                user.userLogin("", "");
            }else{
                user.createGroupOrder(line);
            }
            line = userInput.nextLine();
        }*/
    }
    User(){
        this.requestGroupOrderList = new ArrayList<String>();
        this.userID = "";
        this.password = "";
    }
//----------------------------------------------------------------------------
    User(String id, String password){
        this.requestGroupOrderList = new ArrayList<String>();
        this.userID = id;
        this.password = password;
    }
//----------------------------------------------------------------------------
    public String getUserID(){ return userID; }
//----------------------------------------------------------------------------
    public void createGroupOrder(String requestInfo){
        if(clientSocket.isConnected()){
            output.println("grpOrder - "+ this.getUserID() + "," +requestInfo);               //send the requestInfo to the host
            output.flush();
            this.requestGroupOrderList.add(requestInfo);
            System.out.println("here: " + requestGroupOrderList.toString());
        }
    }
/****************************************************************************/
//Networking
//----------------------------------------------------------------------------
    public boolean userLogin(String id, String password){
        return this.startConnecting();
    }
//----------------------------------------------------------------------------
    public boolean userLogout(){
        return this.stopConnecting();
    }
//----------------------------------------------------------------------------
    private boolean startConnecting(){
        try{
            System.out.println("Attempting to establish a connection ...");
            clientSocket = new Socket(LOCAL_HOST, PORT);          //create and bind a socket, and request connection
            output = new PrintWriter(clientSocket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Connection to server established!");
            HashMap<String, String> list;
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            Object object = objectInputStream.readObject();
            list = (HashMap<String, String>)object;
            
            System.out.println(list.toString());
            //testing - delete after
            output.println("Hi. I am a basic client!");           //send a message to the server
            output.flush();                                       //ensure the message was sent but not kept in the buffer
            String msg = input.readLine();                        //get a response from the server
            System.out.println("Message from server: '" + msg+"'"); 
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
//----------------------------------------------------------------------------
    private boolean stopConnecting(){
        try{
            input.close();
            output.close();
            clientSocket.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
//----------------------------------------------------------------------------
/****************************************************************************/
    @Override
    public String toString(){ return this.userID; }
//----------------------------------------------------------------------------
    @Override
    public boolean equals(Object obj){
        if(obj == this){ return true; }
        if(!(obj instanceof User)){ return false; }
        User user = (User) obj;
        return user.getUserID().equals(this.getUserID());
    }
//----------------------------------------------------------------------------
}