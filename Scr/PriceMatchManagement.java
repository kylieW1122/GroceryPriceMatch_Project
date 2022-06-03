import java.util.ArrayList;
/**[PriceMatchManagement.java]
 * This is final project - price match program
 * This class contains the main method to start the program
 * 
 * @author Kylie Wong and Michelle Chan, ICS4UE
 * @version 1.0, build May 27, 2022
 */

public class PriceMatchManagement{
    private static ArrayList<User> userList = new ArrayList<User>();
//----------------------------------------------------------------------------
    public static void main(String[] args){
        DataBase database = new DataBase();
        //create object a home page here
    }
//----------------------------------------------------------------------------
    public static boolean registerUser(String id, String password){
        User tempUser = new User(id, password);
        if(userList.contains(tempUser)){
            return false; //false - userId taken 
        }else{
            userList.add(tempUser);
        }
        return true;
    }
//----------------------------------------------------------------------------
}