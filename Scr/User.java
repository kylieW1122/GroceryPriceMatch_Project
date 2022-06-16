import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**[User.java]
 * This is final project - price match program
 * This class represent the object - User
 * @author Kylie Wong and Michelle Chan, ICS4UE
 * @version 1.0, build June 2, 2022
 */
public class User{
    private String userID;
    private HashSet<String> committedGroupOrderRefNoList;
    
    final String LOCAL_HOST = "192.168.0.140"; //Kylie's macbook ip address //"127.0.0.1";
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
        HashMap<String, ArrayList<String>> clone_result = new HashMap<String, ArrayList<String>>();
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
        setCommittedGroupOrderList();
        for(String result_refNo: result.keySet()){
            ArrayList<String> orderInfo = result.get(result_refNo);
            if(!this.hasCommittedThisGroupOrder(result_refNo)){
                clone_result.put(result_refNo, orderInfo); //.remove(result_refNo);
             }
        }
        return clone_result;
    }
//----------------------------------------------------------------------------
    public void acceptGroupOrder(String refNo, Double percentage){
        output.println(Const.GROUP_JOIN + refNo + Const.SPLIT + this.userID + Const.SPLIT + percentage);
        output.flush();
        setCommittedGroupOrderList();
    }
//----------------------------------------------------------------------------
    private boolean hasCommittedThisGroupOrder(String refNo){
        if(this.committedGroupOrderRefNoList.contains(refNo)){
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
        }catch(ClassNotFoundException ex){
            System.out.println("ERROR - objects do not match"); 
        }catch(IOException e){
            System.out.println("ERROR - problem occurred when requesting user refNo. list from the host"); 
        }
    }
//----------------------------------------------------------------------------
    public ArrayList<HashMap> getGroupOrderList_user(){
        ArrayList<HashMap> combinedList = new ArrayList<HashMap>();
        HashMap<String, ArrayList<String>> pendingList = new HashMap<String, ArrayList<String>>();
        HashMap<String, ArrayList<String>> completeList = new HashMap<String, ArrayList<String>>();
        
        output.println(Const.GET_USER_DETAIL_GROUP_LISTS + this.userID);
        output.flush();
        try{
            Object object1 = objectInput.readObject();
            Object object2 = objectInput.readObject();
            pendingList = (HashMap<String, ArrayList<String>>)object1;
            completeList = (HashMap<String, ArrayList<String>>)object2;
            combinedList.add(pendingList);
            combinedList.add(completeList);
        }catch(ClassNotFoundException ex){
            System.out.println("ERROR - objects do not match"); 
        }catch(IOException e){
            System.out.println("ERROR - problem occurred when requesting user group order list from the host"); 
        }
        return combinedList;
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