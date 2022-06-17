/**[Const.java]
  * This is final project - Grocery Helper Program
  * Class of constants; defines the communication tags between host and clients  
  * 
  * @author Kylie Wong, ICS4UE
  */ 
public final class Const{ 
    public static final int SOCKET_PORT = 6666;
    //communication tags between host and clients
    public static final String LOGIN = "login        ";  
    public static final String REGISTER = "register     ";  
    public static final String SEARCH_KEYWORD = "search:      "; 
    public static final String KEYWORD_LIST = "keyList:     ";
    public static final String GROUP_ORDER = "group:       "; 
    public static final String GROUP_REFRESH = "refresh:     "; 
    public static final String GROUP_JOIN = "join:        "; 
    public static final String GET_USER_REFNO_LIST = "userRefnoList"; 
    public static final String GET_USER_DETAIL_GROUP_LISTS = "userGrpLists"; 
    public static final String PASSWORD_RESET = "resetPassword"; 
    //messages between host and client
    public static final String SPLIT = "~"; 
    public static final String LOGIN_ACCEPTED = "accept"; 
    public static final String WRONG_PASSWORD = "wrong password"; 
    public static final String USER_ID_TAKEN = "id taken"; 
    public static final String NO_SUCH_USER_ID = "no id"; 
    //string for communication between host and reading .txt files
    public static final String GROUP_INFO_PENDING_SPLIT = "--------------Pending Group Order--------------";   
    public static final String GROUP_INFO_COMPLETED_SPLIT = "--------------Completed Group Order--------------";
   
    private Const(){ 
    } 
}