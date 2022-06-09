/**[HomePage.java]
  * This is final project - price match program
  * Class of constants; defines the communication tag between host and clients  
  * 
  * @author Kylie Wong and Michelle Chan, ICS4UE
  */ 
public final class Const{ 
    public static final String LOGIN = "login";      
    public static final String PASSWORD = "~"; 
    
    public static final String SEARCH_KEYWORD = "search:"; 
    public static final String RIGHT = ">"; 
    public static final String UP = "A"; 
    public static final String DOWN = "V"; 
    public static final String SCROOGE = "AV<>";  //uncle Scrooge standing on a tile in 4 possible directions 
    //use one of the symbols, depending on the direction 
    private Const(){ 
    } 
}