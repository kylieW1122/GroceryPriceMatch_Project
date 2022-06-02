import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.io.BufferedReader;  
import java.io.FileReader;  

/**[DataBase.java]
 * This is final project - price match program
 * This class gets the item data from website and CSV flies and save them as HashMaps
 * @author Kylie Wong and Michelle Chan, ICS4UE
 * @version 2.1, build June 1, 2022
 */
public class DataBase {
    private static HashMap<String, String> sobeysItemsMap;
    private static HashMap<String, String> costCoItemsMap;
    private static HashMap<String, String> walmartItemsMap;   
    private static HashMap<String, String> noFillsItemsMap;
    
    private static HashMap<String, double[]> statisticsHashmap;
    //-----------------------------
    final static String SOBEYS_URL = "https://voila.ca/products?source=navigation&sublocationId=43a936d1-df1d-4bf1-a09c-b23c6a8edf63";  
    final static String STATISTICS_CANADA_FILENAME = "StatisticsCanada/1810000201-eng.csv";
    final static String COSTCO_FILENAME = "costCo.csv";
    final static String NOFILLS_FILENAME = "noFills.csv";
    final static String WALMART_FILENAME = "walmart.csv";
    
    public static void main(String[] args){ //delete - just for debugging 
        DataBase base = new DataBase();
    }
//----------------------------------------------------------------------------
    DataBase(){
//        statisticsHashmap = setUpStatisticsCanadaPriceMatchList(STATISTICS_CANADA_FILENAME);
//        System.out.println(statisticsHashmap);
//        costCoItemsMap = setUpCostcoList(COSTCO_FILENAME);
        walmartItemsMap = setUpWalmartList(WALMART_FILENAME);
//        noFillsItemsMap = setUpNoFillsList(NOFILLS_FILENAME);
//       sobeysItemsMap = setUpSobeysList(SOBEYS_URL);
    }
//----------------------------------------------------------------------------
    public static HashMap<String, String> getSobeysItemList(){
        return sobeysItemsMap;
    }
    public static HashMap<String, double[]> getStatisiticsPriceData(){
        return statisticsHashmap;
    }
   // public static HashMap
//----------------------------------------------------------------------------
    private static HashMap<String, double[]> setUpStatisticsCanadaPriceMatchList(String fileName){ //DONE, move on to line grpah
        HashMap<String, double[]> statisticsList = new  HashMap<String, double[]>();
         try{
             BufferedReader reader = new BufferedReader(new FileReader("Database/" + fileName));
             String line = "";
             line = reader.readLine();
             String[] timeline = line.substring(1).split(",");
             int size = timeline.length;
             reader.readLine(); //read the useless line between the title and the data
             /*******************************************************************************/
             while((line = reader.readLine()) != null){
                 String[] values = line.split(",");
                 double[] prices = new double[size];
                 int index=0;
                 for(int i=1; i<values.length; i++){
                     String temp = values[i];
                     Double d;
                     try{
                         d = Double.parseDouble(temp);
                     }catch (NumberFormatException numberEx){ //if the data format is not a double
                         d = 0.00;   
                     }
                     prices[index] = d;
                     index++;
                 }
                 statisticsList.put(values[0], prices);
             }
             /*******************************************************************************/
         }catch (IOException e){
             e.printStackTrace();
         } 
         return statisticsList;
    }
    private static void printArray(String[] arr){
        for(String str:arr){
            System.out.print(str + ", ");
        }
        System.out.println("\n------------------------------------");
    }
//----------------------------------------------------------------------------
    private static HashMap<String, String> setUpSobeysList(String url){ //successful, data stored in a HashMap
         HashMap<String, String> sobeysList = new  HashMap<String, String>();
//todo: format the text, price value
        try{
            final Document document = Jsoup.connect(url).get();
            ArrayList<String> itemNameArrayList = new ArrayList<String>();
            
            Elements test = document.select("div.layout__Container-sc-1cgl98j-0.laSkfV");
            Element selectByTag = test.last();
            /************************/
            Elements tag = selectByTag.getElementsByTag("h3");
            Elements prices = selectByTag.getElementsByTag("span");
            String itemName = "";
            for(Element tags: tag){
                itemName = tags.text();
                itemNameArrayList.add(itemName);
            }
            String spanTagString = "";
            for(Element price: prices){
                spanTagString = spanTagString + price.text();
            }
            
            String[] arrOfStr = spanTagString.split("You have of this item in your cart");
            int indexOfMap = 0;
            for (String a : arrOfStr){
                sobeysList.put(itemNameArrayList.get(indexOfMap), a);
                indexOfMap++;
            }
            System.out.println(sobeysItemsMap.toString() + "\n");
        }catch (IOException e){
            e.printStackTrace();
        } 
        return sobeysList;
    }
//----------------------------------------------------------------------------
    private static HashMap<String, String> setUpCostcoList(String fileName){ 
        HashMap<String, String> costCoList = new HashMap<String, String>();
         try{
             BufferedReader reader = new BufferedReader(new FileReader("Database/" + fileName));
             String line = "";
             
             reader.readLine(); //read the useless line between the title and the data
             /*******************************************************************************/
             while((line = reader.readLine()) != null){
                 String[] values = line.split(",");
                 costCoList.put(values[2], values[3]);
             }
             System.out.println(costCoList.toString());
         }catch (IOException e){
             e.printStackTrace();
         } 
         return costCoList;
    }
//----------------------------------------------------------------------------
    private static HashMap<String, String> setUpWalmartList(String fileName){ 
        HashMap<String, String> walmartList = new HashMap<String, String>();
        try{
             BufferedReader reader = new BufferedReader(new FileReader("Database/" + fileName));
             String line = "";
             reader.readLine(); //read the useless line between the title and the data
             while((line = reader.readLine()) != null){
                 String[] values = line.split(",");
                 String temp = values[2] + " " + values[3];
                 walmartList.put(temp, values[4]);
             }
             System.out.println(walmartList.toString());
         }catch (IOException e){
             e.printStackTrace();
         }
         return walmartList;
    }
//----------------------------------------------------------------------------
    private static HashMap<String, String> setUpNoFillsList(String fileName){
        HashMap<String, String> noFillsList = new HashMap<String, String>();
         try{
             BufferedReader reader = new BufferedReader(new FileReader("Database/" + fileName));
             String line = "";
             reader.readLine(); //read the useless line between the title and the data
             while((line = reader.readLine()) != null){
                 String[] values = line.split(",");
                 String temp = values[3] + ", " + values[4];
                 temp = temp.replaceAll("\"", "");
                 noFillsList.put(values[2], temp); //format: itemName = price/kg, price(as a whole quantity)
             }
             System.out.println(noFillsList.toString());
         }catch (IOException e){
             e.printStackTrace();
         } 
         return noFillsList;
    }
}