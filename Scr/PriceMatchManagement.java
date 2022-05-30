import java.util.HashMap;
/**[PriceMatchManagement.java]
 * This is final project - price match program
 * This class contains the main method to start the program
 * 
 * @author Kylie Wong and Michelle Chan, ICS4UE
 * @version 1.0, build May 27, 2022
 */

public class PriceMatchManagement{
    //private user id list? 
    private static HashMap<String, String> sobeysItemsMap;
    //private static DATATYPE? statisticCanadaData;
//----------------------------------------------------------------------------
    public static void main(String[] args){
        WebScraper scraper = new WebScraper();
        /********************SOBEYS**********************/
        sobeysItemsMap = scraper.getSobeysItemList();
        
        //create object a home page here
        //create object webscraper here
    }
}