import java.util.ArrayList;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
    ObjectInputStream objectInput;
    ObjectOutputStream objectOutput;
//----------------------------------------------------------------------------
    public static void main(String[] args){ //start homepage here, for user
        User user = new User();
        HomePage frame = new HomePage(user);
    }
//----------------------------------------------------------------------------
    User(){
        this.requestGroupOrderList = new ArrayList<String>();
        this.userID = null;
        this.password = null;
        this.startConnecting();
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
        output.println(Const.LOGIN + id + Const.SPLIT + password);
        output.flush();
        try{
            String msg = input.readLine();
            if(msg.equals("true")){
                this.userID = id;
                this.password = password;
                return true;
            }
        }catch(IOException exception){
            exception.printStackTrace();
        }
        return false;
    }
//----------------------------------------------------------------------------
    public ArrayList<String> getSearchResultList(String keyword){ // from host's database
        ArrayList<String> result = new ArrayList<String>();
        output.println(Const.SEARCH_KEYWORD + keyword);
        output.flush();
        try{
            Object object = objectInput.readObject();
            result = (ArrayList<String>)object;
        }catch(ClassNotFoundException ex){
            System.out.println(ex.toString());
        }catch(IOException e){
            System.out.println(e.toString());
        }
        if(result == null){
            return new ArrayList<String>();
        }
        return result;
    }
//----------------------------------------------------------------------------
    public ArrayList<String> getWholeItemArrayList(){
        ArrayList<String> result = new ArrayList<String>();
        output.println(Const.KEYWORD_LIST);
        output.flush();
        try{
            Object object = objectInput.readObject();
            result = (ArrayList<String>)object;
        }catch(ClassNotFoundException ex){
            System.out.println(ex.toString());
        }catch(IOException e){
            System.out.println(e.toString());
        }
        if(result == null){
            return new ArrayList<String>();
        }
        return result;
    }
//----------------------------------------------------------------------------
    public boolean createGroupOrder(String itemInfo, Double amountPercentage){
        output.println(Const.GROUP_ORDER + itemInfo + Const.SPLIT + this.userID + Const.SPLIT + amountPercentage + Const.SPLIT);
        output.flush();
        return false;
    }
//----------------------------------------------------------------------------
    public HashMap<String, ArrayList<String>> refreshGroupOrder(){
        HashMap<String, ArrayList<String>> groupOrderRefList = new HashMap<String, ArrayList<String>>();
        output.println(Const.GROUP_REFRESH);
        output.flush();
        try{
            Object object = objectInput.readObject();
            groupOrderRefList = (HashMap<String, ArrayList<String>>)object;
        }catch(ClassNotFoundException ex){
            System.out.println(ex.toString());
        }catch(IOException e){
            System.out.println(e.toString());
        }
        return groupOrderRefList;
    }
//----------------------------------------------------------------------------
    private boolean userLogout(){
        return this.stopConnecting();
    }
//----------------------------------------------------------------------------
    private boolean startConnecting(){
        try{
            System.out.println("Attempting to establish a connection ...");
            clientSocket = new Socket(LOCAL_HOST, PORT);          //create and bind a socket, and request connection
            output = new PrintWriter(clientSocket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            objectInput = new ObjectInputStream(clientSocket.getInputStream());
            objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("Connection to server established!");
            String msg = input.readLine();                        //get a response from the server
            System.out.println("Message from server: '" + msg+"'"); 
        }catch (Exception e){
            //directly use database, instead of host, block all group order
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
}