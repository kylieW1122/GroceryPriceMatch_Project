import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Random;
import java.util.HashSet;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;

/**[HostManagement.java]
  * This is final project - Grocery Helper Program
  * This class contains the main method to start the host server
  * 
  * Only the host has access to Database, and user(client) sends request message to host.
  * Host then returns the object list or message.
  * Const.java is the "language" used between host and client.
  * 
  * @author Kylie Wong, ICS4UE
  * @version 2.0, build June 16, 2022
  */

public class HostManagement{
    private DataBase database;
    //userIDRefNoMap format: {userId=the grpOrderList of that user}
    private static HashMap<String, ArrayList<String>> userIDRefNoMap = new HashMap<String, ArrayList<String>>();
    //userPasswordMap format: {userId=password}
    private static HashMap<String, String> userPasswordMap = new HashMap<String, String>();
    //refNoMapPending format: {refNo=order info list of the group orders that are not yet completed}
    private static HashMap<String, ArrayList<String>> refNoMapPending = new HashMap<String, ArrayList<String>>();
    //refNoMapCompleted format:  {refNo=order info list of the group orders that are completed}
    private static HashMap<String, ArrayList<String>> refNoMapCompleted = new HashMap<String, ArrayList<String>>();
    
    private static FileReader fileReader; 
    private static BufferedReader input;
    private static PrintWriter output_writer; 
    private static File groupOrderFile;
    private static File userInfoFile;
    
    private final String userinfo_fileName =  "resources/UserInfo.txt";
    private final String groupOrder_fileName = "resources/GroupOrderInfo.txt";
    private String userInfo_titleStr;
    
    final int PORT = Const.SOCKET_PORT;
    ServerSocket serverSocket;
    Socket clientSocket;
//----------------------------------------------------------------------------
    public static void main(String[] args){
        HostManagement hostServer = new HostManagement();
    }
//----------------------------------------------------------------------------
    HostManagement(){
        this.database = new DataBase();
        this.setUp();
        this.writeUpdatedInfoToFiles();
        try{
            this.start();
        }catch(Exception e){ System.out.println("ERROR - problem occurred starting the server"); }
    }
    //----------------------------------------------------------------------------
    //read and write UserInfo.txt and GroupOrderInfo.txt, when there are changes to the lists (order groups or password)
    //----------------------------------------------------------------------------
    private void setUp(){
        try{
            /************read user info file which has the userid, password, and user's refNo list of grp orders****************/
            fileReader = new FileReader(userinfo_fileName);
            input = new BufferedReader(fileReader);
            
            this.userInfo_titleStr = input.readLine(); 
            while(input.ready()){    //keep reading lines while the End-of-File hasn't been reached
                //format example: 3. userId: password~[2080537622]
                String msg = input.readLine(); 
                String name = msg.substring(3, msg.indexOf(": "));
                msg = msg.substring(msg.indexOf(": ")+2);
                String password;
                /*******set up the user's group order refNo list: userPasswordMap & userIDRefNoMap********/             
                if(msg.indexOf(Const.SPLIT)!=-1){
                    password = msg.substring(0, msg.indexOf(Const.SPLIT));
                    msg = msg.substring(msg.indexOf(Const.SPLIT)+1);
                    String listString = msg.substring(1, msg.length()-1);
                    String[] arr = listString.split(", ");
                    ArrayList<String> refNoArrayList = new ArrayList<String>();
                    Collections.addAll(refNoArrayList, arr);
                    userIDRefNoMap.put(name, refNoArrayList);
                }else{ //the user have no group order
                    password = msg;
                }
                userPasswordMap.put(name, password);
            }
            input.close();
            
            /*******set up the overall group order refNo list: refNoMapPending & refNoMapCompleted********/
            fileReader = new FileReader(groupOrder_fileName);
            input = new BufferedReader(fileReader);
            String line;
            while((line = input.readLine())!= null && !line.equals(Const.GROUP_INFO_COMPLETED_SPLIT)){
                if(!line.equals(Const.GROUP_INFO_PENDING_SPLIT)){
                    //line format example: 887974418~[Choice Green Peppers 1 1/9 bushel, $44.29, CostCo, A~0.6]
                    String refNo = line.substring(0, line.indexOf(Const.SPLIT));
                    line = line.substring(line.indexOf(Const.SPLIT)+1);
                    line = line.substring(1, line.length()-1);
                    String[] arr = line.split(", ");
                    ArrayList<String> infoList = new ArrayList<String>();
                    Collections.addAll(infoList, arr);
                    refNoMapPending.put(refNo, infoList); //refNo, ArrayList<String> infoList
                }
            }
            while((line = input.readLine()) != null){
                //line format example: 887974418~[Choice Green Peppers 1 1/9 bushel, $44.29, CostCo, A~0.6, mike~0.4]
                String refNo = line.substring(0, line.indexOf(Const.SPLIT));
                line = line.substring(line.indexOf(Const.SPLIT)+1);
                line = line.substring(1, line.length()-1);
                String[] arr = line.split(", ");
                ArrayList<String> infoList = new ArrayList<String>();
                Collections.addAll(infoList, arr);
                refNoMapCompleted.put(refNo, infoList); //refNo, ArrayList<String> infoList
            }
            input.close();
            
    
        }catch (IOException e){
            System.out.println("ERROR - file '" + userinfo_fileName + "' not found. ");
        }
    }
    //----------------------------------------------------------------------------
    private void writeUpdatedInfoToFiles(){ //rewrite the file everytime group order list has changed
        try{
            /**********************write the GroupOrderInfo.txt**************************/
            groupOrderFile = new File(groupOrder_fileName);
            output_writer = new PrintWriter(groupOrderFile);
            //write group order info for pending orders
            output_writer.println(Const.GROUP_INFO_PENDING_SPLIT);
            for(String refNo: refNoMapPending.keySet()){
                ArrayList<String> list = refNoMapPending.get(refNo);
                output_writer.println(refNo + Const.SPLIT + list.toString());
            }
            //write group order info for completed orders
            output_writer.println(Const.GROUP_INFO_COMPLETED_SPLIT);
            for(String refNo: refNoMapCompleted.keySet()){
                ArrayList<String> list = refNoMapCompleted.get(refNo);
                output_writer.println(refNo + Const.SPLIT + list.toString());
            }
            output_writer.close();
            /**********************write the UserInfo.txt****************************/
            userInfoFile = new File(userinfo_fileName);
            output_writer = new PrintWriter(userInfoFile);
            output_writer.println(userInfo_titleStr);
            int index=1;
            for(String userID: userPasswordMap.keySet()){
                ArrayList<String> refNoList = userIDRefNoMap.get(userID);
                //format example: 3. userId: password~[2080537622]
                if(refNoList!=null){
                    output_writer.println(index + ". " + userID + ": " + userPasswordMap.get(userID) + 
                                          Const.SPLIT + refNoList.toString());
                }else{ //if the user dos not have group order
                    output_writer.println(index + ". " + userID + ": " + userPasswordMap.get(userID)); 
                }
                index++;
            }
            output_writer.close();
        }catch(IOException e){
            System.out.println("ERROR - file '" + groupOrder_fileName + "' not found. ");
        }
    }
    //----------------------------------------------------------------------------
    //user-password related methods
    //----------------------------------------------------------------------------
    private String loginUser(String id, String password){
        if(userPasswordMap.containsKey(id)){
            String correctPassword = userPasswordMap.get(id);
            if(password.equals(correctPassword)){
                return Const.LOGIN_ACCEPTED;
            }else{
                return Const.WRONG_PASSWORD;
            }
        }else{
            return Const.NO_SUCH_USER_ID;
        }
    }
    //----------------------------------------------------------------------------
    private String registerUser(String id, String password){
        if(userPasswordMap.containsKey(id)){
            return Const.USER_ID_TAKEN;
        }else{
            userPasswordMap.put(id, password);
            writeUpdatedInfoToFiles();
        }
        return Const.LOGIN_ACCEPTED;
    }
    //------------------------------------------------------------------------------
    private boolean resetUserPassword(String id, String oldPassword, String newPassword){
        if(userPasswordMap.containsKey(id)){
            String password = userPasswordMap.get(id);
            if(oldPassword.equals(password)){  //verified user, match password
                userPasswordMap.put(id, newPassword); // this will overwrite the value-password in the key-id
                writeUpdatedInfoToFiles();
                return true;
            }
        }
        return false; // oldpassword does not match
    }
    //----------------------------------------------------------------------------
    //group order
    //----------------------------------------------------------------------------
    private String makeGroupOrder(String msg){
        //msg format example: Mini Cucumbers 2.27 kg = $7.49 @ CostCo~a-b-c~0.5~
        String itemInfo = msg.substring(0, msg.indexOf(Const.SPLIT));
        String itemName = itemInfo.substring(0, itemInfo.indexOf(" = "));
        String price = itemInfo.substring(itemInfo.indexOf(" = ") +3 , itemInfo.indexOf(" @ "));
        String location =  itemInfo.substring(itemInfo.indexOf(" @ ") +3);
        msg = msg.substring(msg.indexOf(Const.SPLIT)+1);
        String userID = msg.substring(0, msg.indexOf(Const.SPLIT));
        msg = msg.substring(msg.indexOf(Const.SPLIT)+1);
        String amountPercentage = msg.substring(0, msg.indexOf(Const.SPLIT));
        /**********Create arraylist to store them into refNoMap and userIDRefNoMap*************/
        ArrayList<String> infoList = new ArrayList<String>();
        infoList.add(0, itemName);
        infoList.add(1, price);
        infoList.add(2, location); 
        infoList.add(userID + Const.SPLIT + amountPercentage);
        String refNo = generateUniqueRefNo(itemName);
        
        refNoMapPending.put(refNo, infoList);
        updateRefNoListsInUser(refNo, userID);
        writeUpdatedInfoToFiles(); // update the two .txt files
        return refNo;
    }
    //----------------------------------------------------------------------------
    private void updateRefNoListsInUser(String refNo, String userID){
        ArrayList<String> user_listOfRefNo = new ArrayList<String>(); 
        if(userIDRefNoMap.containsKey(userID)){
            user_listOfRefNo = userIDRefNoMap.get(userID);
            user_listOfRefNo.add(refNo);
        }else{
            user_listOfRefNo.add(refNo);
        }
        userIDRefNoMap.put(userID, user_listOfRefNo);
    }
    //----------------------------------------------------------------------------
    private void delay(long timeInMilliSeconds){ //@author Michelle
        long timestamp = System.currentTimeMillis();
        do{}while (System.currentTimeMillis() < timestamp + timeInMilliSeconds);
    }
    //----------------------------------------------------------------------------
    private String generateUniqueRefNo(String itemInfo){ //@author Michelle
        String refNo = Long.toString(System.nanoTime());
        while(refNoMapPending.containsKey(refNo)){ //if refNo is taken, gererate a new one
            delay(300);
            refNo = Long.toString(System.nanoTime());
        }
        return refNo;
    }
    //----------------------------------------------------------------------------
    private void acceptGroupOrder(String msg){
        String refNo = msg.substring(0, msg.indexOf(Const.SPLIT));
        msg = msg.substring(msg.indexOf(Const.SPLIT)+1);
        String userID = msg.substring(0, msg.indexOf(Const.SPLIT));
        msg = msg.substring(msg.indexOf(Const.SPLIT)+1);
        String amountPercentage = msg;
        
        if(refNoMapPending.containsKey(refNo)){
            ArrayList<String> orderInfoList = refNoMapPending.get(refNo);
            orderInfoList.add(userID + Const.SPLIT + amountPercentage);
            refNoMapCompleted.put(refNo, orderInfoList);
            refNoMapPending.remove(refNo);
        }
        updateRefNoListsInUser(refNo, userID);
        writeUpdatedInfoToFiles(); // update the two .txt files
    }
//----------------------------------------------------------------------------
    private HashSet<String> getRefNoList_user(String userID){
        HashSet<String> result = new HashSet<String>();
        ArrayList<String> user_refList = userIDRefNoMap.get(userID);
        if(user_refList!=null){
            result.addAll(user_refList);
        }
        return result;
    }
//----------------------------------------------------------------------------
    //only return a list that is part of the user's order
    private HashMap<String, ArrayList<String>> getRefNoMapCompleted_user(String userID){
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
        ArrayList<String> user_refList = userIDRefNoMap.get(userID);
        for(String refNo: refNoMapCompleted.keySet()){
            ArrayList<String> orderInfoList = refNoMapCompleted.get(refNo);
            if(user_refList.contains(refNo)){        // if that refNo is part of user's completed group order
                result.put(refNo, orderInfoList);
            }
        }
        return result;
    }
//----------------------------------------------------------------------------
    //only return a list that is part of the user's order
    private HashMap<String, ArrayList<String>> getRefNoMapPending_user(String userID){
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
        ArrayList<String> user_refList = userIDRefNoMap.get(userID);
        for(String refNo: refNoMapPending.keySet()){
            ArrayList<String> orderInfoList = refNoMapPending.get(refNo);
            if(user_refList.contains(refNo)){        // if that refNo is part of user's pending group order
                result.put(refNo, orderInfoList);
            }
        }
        return result;
    }
//----------------------------------------------------------------------------
//----------------------------------------------------------------------------
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
        //----------------------------------------------------------------------------
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
                    //call differnt method depends on the msg's tags(Const.)
                    if(msg.substring(0, Const.LOGIN.length()).equals(Const.LOGIN)){
                        String userName = "";
                        String password = "";
                        userName = msg.substring(Const.LOGIN.length(), msg.indexOf(Const.SPLIT));
                        password = msg.substring(msg.indexOf(Const.SPLIT) +1);
                        String resultOfLogin = loginUser(userName, password);
                        output.println(resultOfLogin);
                        output.flush();
                    }else if (msg.substring(0, Const.REGISTER.length()).equals(Const.REGISTER)){
                        String userName = msg.substring(Const.REGISTER.length(), msg.indexOf(Const.SPLIT));
                        String password = msg.substring(msg.indexOf(Const.SPLIT) +1);
                        String resultOfLogin = registerUser(userName, password);
                        output.println(resultOfLogin);
                        output.flush();
                    }else if(msg.substring(0, Const.SEARCH_KEYWORD.length()).equals(Const.SEARCH_KEYWORD)){
                        ArrayList<String> resultList = new  ArrayList<String>();
                        if(msg.length() > Const.SEARCH_KEYWORD.length()){ // if the keyword is not empty ""
                            String keyword = msg.substring(Const.SEARCH_KEYWORD.length());
                            resultList = DataBase.searchItemKeyword(keyword);
                        }
                        objectOutput.writeObject(resultList);
                        objectOutput.flush();
                    }else if (msg.substring(0, Const.KEYWORD_LIST.length()).equals(Const.KEYWORD_LIST)){
                        ArrayList<String> keywordList = DataBase.getWholeList();
                        objectOutput.writeObject(keywordList);
                        objectOutput.flush();
                    }else if(msg.substring(0, Const.GROUP_ORDER.length()).equals(Const.GROUP_ORDER)){
                        msg = msg.substring(Const.GROUP_ORDER.length());
                        String refNo = makeGroupOrder(msg);
                        output.println(refNo);
                        output.flush();
                    }else if (msg.substring(0, Const.GROUP_REFRESH.length()).equals(Const.GROUP_REFRESH)){
                        HashMap<String, ArrayList<String>> sendMap = new HashMap<String, ArrayList<String>>(refNoMapPending);
                        objectOutput.writeObject(sendMap);
                        objectOutput.flush();
                    }else if (msg.substring(0, Const.GROUP_JOIN.length()).equals(Const.GROUP_JOIN)){
                        msg = msg.substring(Const.GROUP_JOIN.length());
                        acceptGroupOrder(msg);
                    }else if (msg.substring(0, Const.PASSWORD_RESET.length()).equals(Const.PASSWORD_RESET)){
                        String userName = msg.substring(Const.PASSWORD_RESET.length(), msg.indexOf(Const.SPLIT));
                        msg = msg.substring(msg.indexOf(Const.SPLIT) +1);
                        String oldPassword = msg.substring(0, msg.indexOf(Const.SPLIT));
                        String newPassword = msg.substring(msg.indexOf(Const.SPLIT)+1);
                        boolean status = resetUserPassword(userName, oldPassword, newPassword);
                        output.println(status);
                        output.flush();
                    }else if (msg.substring(0, Const.GET_USER_REFNO_LIST.length()).equals(Const.GET_USER_REFNO_LIST)){
                        String userName = msg.substring(Const.GET_USER_REFNO_LIST.length());
                        HashSet<String> refNoList_user = getRefNoList_user(userName);
                        objectOutput.writeObject(refNoList_user);
                        objectOutput.flush();
                    }else if (msg.substring(0, Const.GET_USER_DETAIL_GROUP_LISTS.length()).equals(Const.GET_USER_DETAIL_GROUP_LISTS)){
                        String userName = msg.substring(Const.GET_USER_DETAIL_GROUP_LISTS.length());
                        HashMap<String, ArrayList<String>> pendingMap = getRefNoMapPending_user(userName);
                        HashMap<String, ArrayList<String>> completedMap= getRefNoMapCompleted_user(userName);
                        objectOutput.writeObject(pendingMap);
                        objectOutput.writeObject(completedMap);
                        objectOutput.flush();
                    }else{
                        System.out.println("ERROR - a communication problem occurred between host and user.");
                    }
                }
                input.close();
                output.close();
            }catch (IOException e){ System.out.println("ERROR - problem occurred in the server"); }
        }
    }  
//------------------------------------------------------------------------------
}