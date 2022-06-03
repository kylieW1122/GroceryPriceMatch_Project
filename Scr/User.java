/**[User.java]
 * This is final project - price match program
 * This class represent the object - User
 * @author Kylie Wong and Michelle Chan, ICS4UE
 * @version 1.0, build June 2, 2022
 */

public class User{
    private String password;
    private String userID;
//----------------------------------------------------------------------------
    User(String id, String password){
        this.userID = id;
        this.password = password;
    }
//----------------------------------------------------------------------------
    public String getUserID(){ return userID; }
//----------------------------------------------------------------------------
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