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
 * This is final project - Grocery Helper Program
 * This class represent the User - client socket
 * This is the place to start the program, and it will connect to the host if that is available 
 * @author Kylie Wong and Michelle Chan, ICS4UE
 * @version 2.0, build June 16, 2022
 */
public class User{
    private String userID;
    private HashSet<String> committedGroupOrderRefNoList;
    
   // final String LOCAL_HOST = "192.168.0.140"; //Kylie's macbook ip address
    final String LOCAL_HOST = "127.0.0.1"; //Kylie's macbook ip address
    final int PORT = Const.SOCKET_PORT;
    Socket clientSocket;
    PrintWriter output;    
    BufferedReader input;
    ObjectInputStream objectInput;
    ObjectOutputStream objectOutput;
//----------------------------------------------------------------------------
    public static void main(String[] args){ //start main GUI here
        User user = new User();
        new HomePage(user);
     
    }
//----------------------------------------------------------------------------
    User(){
        this.committedGroupOrderRefNoList = new HashSet<String>();
        this.userID = "";
        this.startConnecting();  //connect with the host using socket
    }
//----------------------------------------------------------------------------
    public String getUserID(){ return this.userID; }
//----------------------------------------------------------------------------
/****************************************************************************************************************/
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
            System.out.println("Message from server: '" + msg + "'"); 
        }catch (Exception e){
            //directly use database, instead of host, block all group order
            e.printStackTrace();
            return false;
        }
        return true;
    }
/****************************************************************************************************************/
//Methods for networking, send msg with tag to the host to request information 
    //----------------------------------------------------------------------------
    //user-password related methods
    //----------------------------------------------------------------------------
    public String userLogin(String id, String password){
        output.println(Const.LOGIN + id + Const.SPLIT + password);
        output.flush();
        String msg="";
        try{
            msg = input.readLine();
            if(msg.equals(Const.LOGIN_ACCEPTED)){
                this.userID = id;      //store the user id for identity of this user
            }
        }catch(IOException exception){
            System.out.println("ERROR - problem occurred when requesting user-login from the host"); 
        }
        return msg;     //the msg of the status: LOGIN_ACCEPTED or WRONG_PASSWORD or NO_SUCH_USER_ID
    }
    //----------------------------------------------------------------------------
    public String registerUser(String id, String password){
        output.println(Const.REGISTER + id + Const.SPLIT + password);
        output.flush();
        String msg="";
        try{
            msg = input.readLine();
            if(msg.equals(Const.LOGIN_ACCEPTED)){
                this.userID = id;       //store the user id for identity of this user
            }
        }catch(IOException exception){
            System.out.println("ERROR - problem occurred when  when requesting register-user from the host"); 
        }
        return msg;  //the msg of the status: LOGIN_ACCEPTED or USER_ID_TAKEN
    }
    //----------------------------------------------------------------------------
    public boolean resetPassword(String id, String oldPassword, String newPassword){
        output.println(Const.PASSWORD_RESET + id + Const.SPLIT + oldPassword + Const.SPLIT + newPassword);
        output.flush();
        String msg = "";
        try{
            msg = input.readLine();
            if(msg.equals("true")){  //if old password match, and host has updated the list
                return true;
            }
        }catch(IOException exception){
            System.out.println("ERROR - problem occurred when requesting user-login from the host"); 
        }
        return false;
    }
    //----------------------------------------------------------------------------
    //item database, from host's database
    //----------------------------------------------------------------------------
    public ArrayList<String> getSearchResultList(String keyword){
        ArrayList<String> result = new ArrayList<String>();
        output.println(Const.SEARCH_KEYWORD + keyword);
        output.flush();
        try{
            Object object = objectInput.readObject();
            result = (ArrayList<String>)object; //arraylist of every items info in the search result
        }catch(ClassNotFoundException ex){
            System.out.println("ERROR - objects do not match"); 
        }catch(IOException e){
            System.out.println("ERROR - problem occurred when requesting search result from the host"); 
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
            result = (ArrayList<String>)object;  //arraylist of every item's info string in the database
        }catch(ClassNotFoundException ex){
            System.out.println("ERROR - objects do not match"); 
        }catch(IOException e){
            System.out.println("ERROR - problem occurred when requesting item list from the host"); 
        }
        return result;
    }
    //----------------------------------------------------------------------------
    //group order
    //----------------------------------------------------------------------------
    public boolean createGroupOrder(String itemInfo, Double amountPercentage){
        output.println(Const.GROUP_ORDER + itemInfo + Const.SPLIT + this.userID + Const.SPLIT + amountPercentage + Const.SPLIT);
        output.flush();
        String refNo = "";
        try{ 
            refNo = input.readLine();   //host will return a generated unique reference no represent the order
            setCommittedGroupOrderList();  //update this user's committed group order list
        }catch(IOException exception){
            System.out.println("ERROR - problem occurred when requesting refNo. from the host"); 
        }
        return true;
    }
    //----------------------------------------------------------------------------
    public HashMap<String, ArrayList<String>> refreshGroupOrder(){ //a list for the group order panel stream
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
        HashMap<String, ArrayList<String>> clone_result = new HashMap<String, ArrayList<String>>();
        output.println(Const.GROUP_REFRESH);
        output.flush();
        try{
            Object object = objectInput.readObject();
            result = (HashMap<String, ArrayList<String>>)object;  //the whole list of group order from the host
        }catch(ClassNotFoundException ex){
            System.out.println("ERROR - objects do not match"); 
        }catch(IOException e){
            System.out.println("ERROR - problem occurred when requesting group order list from the host"); 
        }
        setCommittedGroupOrderList();  //update this user's committed group order list
        /**********Only return the group order that does not include user itself************/
        for(String result_refNo: result.keySet()){
            ArrayList<String> orderInfo = result.get(result_refNo);
            if(!this.hasCommittedThisGroupOrder(result_refNo)){ //if the user isn't in that group, add it to the stream
                clone_result.put(result_refNo, orderInfo); 
             }
        }
        return clone_result;
    }
    //----------------------------------------------------------------------------
    public void acceptGroupOrder(String refNo, Double percentage){
        output.println(Const.GROUP_JOIN + refNo + Const.SPLIT + this.userID + Const.SPLIT + percentage);
        output.flush();
        setCommittedGroupOrderList();  //update this user's committed group order list
    }
    //----------------------------------------------------------------------------
    private boolean hasCommittedThisGroupOrder(String refNo){  //check if user is in this group order
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
            //request the updated refNo list from host, the list contains the refNo of the grps user committed
            this.committedGroupOrderRefNoList = (HashSet<String>)object;  
        }catch(ClassNotFoundException ex){
            System.out.println("ERROR - objects do not match"); 
        }catch(IOException e){
            System.out.println("ERROR - problem occurred when requesting user refNo. list from the host"); 
        }
    }
    //----------------------------------------------------------------------------
    //account detail 
    //----------------------------------------------------------------------------
    public ArrayList<HashMap> getGroupOrderList_user(){ // return 
        ArrayList<HashMap> combinedList = new ArrayList<HashMap>();
        HashMap<String, ArrayList<String>> pendingList = new HashMap<String, ArrayList<String>>();
        HashMap<String, ArrayList<String>> completeList = new HashMap<String, ArrayList<String>>();
        
        output.println(Const.GET_USER_DETAIL_GROUP_LISTS + this.userID);
        output.flush();
        try{
            Object object1 = objectInput.readObject();
            Object object2 = objectInput.readObject();
            //the list of grp orders that the user created and haven't formed a grp yet
            pendingList = (HashMap<String, ArrayList<String>>)object1; 
            //the list of grp orders that the user has joined/created, and have formed a complete group
            completeList = (HashMap<String, ArrayList<String>>)object2;
            combinedList.add(0, pendingList);  //add them into an arraylist, {pendingList, completeList}
            combinedList.add(1, completeList);
        }catch(ClassNotFoundException ex){
            System.out.println("ERROR - objects do not match"); 
        }catch(IOException e){
            System.out.println("ERROR - problem occurred when requesting user group order list from the host"); 
        }
        return combinedList;
    }
}