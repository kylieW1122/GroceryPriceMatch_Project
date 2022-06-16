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
    private String userID;
    private HashSet<String> committedGroupOrderRefNoList;
    //private HashMap<String, ArrayList<String>> ;
    
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
        this.committedGroupOrderRefNoList = new HashSet<String>();
        this.userID = "";
        this.startConnecting();
    }
//----------------------------------------------------------------------------
    public String getUserID(){ return this.userID; }
//----------------------------------------------------------------------------
    public void createGroupOrder(String requestInfo){
        if(clientSocket.isConnected()){
            output.println("grpOrder - "+ this.getUserID() + "," +requestInfo);               //send the requestInfo to the host
            output.flush();
        }
    }
/****************************************************************************/
//Networking
//----------------------------------------------------------------------------
    public String userLogin(String id, String password){
        output.println(Const.LOGIN + id + Const.SPLIT + password);
        output.flush();
        String msg="";
        try{
            msg = input.readLine();
            if(msg.equals(Const.LOGIN_ACCEPTED)){
                this.userID = id;
            }
        }catch(IOException exception){
            System.out.println("ERROR - problem occurred when requesting user-login from the host"); 
        }
        return msg;
    }
//----------------------------------------------------------------------------
    public String registerUser(String id, String password){
        output.println(Const.REGISTER + id + Const.SPLIT + password);
        output.flush();
        String msg="";
        try{
            msg = input.readLine();
            if(msg.equals(Const.LOGIN_ACCEPTED)){
                this.userID = id;
            }
        }catch(IOException exception){
            System.out.println("ERROR - problem occurred when  when requesting register-user from the host"); 
        }
        return msg;
    }
//----------------------------------------------------------------------------
    public boolean resetPassword(String id, String oldPassword, String newPassword){
        output.println(Const.PASSWORD_RESET + id + Const.SPLIT + oldPassword + Const.SPLIT + newPassword);
        output.flush();
        String msg = "";
        try{
            msg = input.readLine();
            if(msg.equals("true")){
                return true;
            }
        }catch(IOException exception){
            System.out.println("ERROR - problem occurred when requesting user-login from the host"); 
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
            System.out.println("ERROR - objects do not match"); 
        }catch(IOException e){
            System.out.println("ERROR - problem occurred when requesting search result from the host"); 
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
            System.out.println("ERROR - objects do not match"); 
        }catch(IOException e){
            System.out.println("ERROR - problem occurred when requesting item list from the host"); 
        }
        return result;
    }
//----------------------------------------------------------------------------
    public boolean createGroupOrder(String itemInfo, Double amountPercentage){
        output.println(Const.GROUP_ORDER + itemInfo + Const.SPLIT + this.userID + Const.SPLIT + amountPercentage + Const.SPLIT);
        output.flush();
        String refNo = "";
        try{
            refNo = input.readLine();
            setCommittedGroupOrderList();
        }catch(IOException exception){
            System.out.println("ERROR - problem occurred when requesting refNo. from the host"); 
        }
        return true;
    }
//----------------------------------------------------------------------------
    public HashMap<String, ArrayList<String>> refreshGroupOrder(){
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
        output.println(Const.GROUP_REFRESH);
        output.flush();
        try{
            Object object = objectInput.readObject();
            result = (HashMap<String, ArrayList<String>>)object;
        }catch(ClassNotFoundException ex){
            System.out.println("ERROR - objects do not match"); 
        }catch(IOException e){
            System.out.println("ERROR - problem occurred when requesting group order list from the host"); 
        }
        return result;
    }
//----------------------------------------------------------------------------
    public void acceptGroupOrder(String refNo, Double percentage){
        output.println(Const.GROUP_JOIN + refNo + Const.SPLIT + this.userID + Const.SPLIT + percentage);
        output.flush();
        setCommittedGroupOrderList();
    }
//----------------------------------------------------------------------------
    public boolean hasCommittedThisGroupOrder(String refNo){
        if(committedGroupOrderRefNoList.contains(refNo)){
            return true;
        }
        return false;
    }
//----------------------------------------------------------------------------
    private void setCommittedGroupOrderList(){
        output.println(Const.GET_USER_REFNO_LIST + this.userID);
        output.flush();
        try{
            Object object = objectInput.readObject();
            this.committedGroupOrderRefNoList = (HashSet<String>)object;
            System.out.println(committedGroupOrderRefNoList.toString());
        }catch(ClassNotFoundException ex){
            System.out.println("ERROR - objects do not match"); 
        }catch(IOException e){
            System.out.println("ERROR - problem occurred when requesting user refNo. list from the host"); 
        }
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
}