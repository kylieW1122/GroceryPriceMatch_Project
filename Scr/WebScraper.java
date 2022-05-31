import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.io.BufferedReader;  
import java.io.FileReader;  

/**[WebScraper.java]
 * This is final project - price match program
 * This class gets the website's data and save it as......
 * https://youtu.be/yw7B85174JQ 
 * @author Kylie Wong and Michelle Chan, ICS4UE
 * @version 2.0, build May 31, 2022
 */
public class WebScraper {
    private static HashMap<String, String> sobeysItemsMap = new HashMap<String, String>();
    private static HashMap<String, double[]> statisticsHashmap;
    //-----------------------------
    final static String SOBEYS_URL = "https://voila.ca/products?source=navigation&sublocationId=43a936d1-df1d-4bf1-a09c-b23c6a8edf63";  
    final static String STATISTICS_CANADA_FILENAME = "StatisticsCanada/1810000201-eng.csv";
    final static String COSTCO_FILENAME = "costCo.csv";
    final static String NOFILLS_FILENAME = "noFills.csv";
    final static String WALMART_FILENAME = "walmart.csv";
    
    public static void main(String[] args){ //delete - just for debugging 
        WebScraper scraper = new WebScraper();
    }
//----------------------------------------------------------------------------
    WebScraper(){
        statisticsHashmap = setUpStatisticsCanadaPriceMatchList(STATISTICS_CANADA_FILENAME);
        System.out.println(statisticsHashmap);
        
//        setUpCostcoList(COSTCO_FILENAME);
//        setUpWalmartList(WALMART_FILENAME);
//        setUpNoFillsList(NOFILLS_FILENAME);
//        setUpSobeysList(SOBEYS_URL);
    }
//----------------------------------------------------------------------------
    public static HashMap<String, String> getSobeysItemList(){
        return sobeysItemsMap;
    }
    public static HashMap<String, double[]> getStatisiticsPriceData(){
        return statisticsHashmap;
    }
//----------------------------------------------------------------------------
    private static HashMap<String, double[]> setUpStatisticsCanadaPriceMatchList(String fileName){
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
    }
//----------------------------------------------------------------------------
    private static void setUpSobeysList(String url){ //successful, data stored in a HashMap
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
                sobeysItemsMap.put(itemNameArrayList.get(indexOfMap), a);
                indexOfMap++;
            }
            System.out.println(sobeysItemsMap.toString() + "\n");
        }catch (IOException e){
            e.printStackTrace();
        } 
    }
//----------------------------------------------------------------------------
    private static void setUpCostcoList(String fileName){
         try{
             BufferedReader reader = new BufferedReader(new FileReader("Database/" + fileName));
             String line = "";
             while((line = reader.readLine()) != null){
                 //String[] values
                 //System.out.println(line);
                 //System.out.println("----------------------");
             }
         }catch (IOException e){
             e.printStackTrace();
         } 
    }
//----------------------------------------------------------------------------
    private static void setUpWalmartList(String fileName){
        try{
             BufferedReader reader = new BufferedReader(new FileReader("Database/" + fileName));
             String line = "";
             while((line = reader.readLine()) != null){
                 //String[] values
                 //System.out.println(line);
                 //System.out.println("----------------------");
             }
         }catch (IOException e){
             e.printStackTrace();
         } 
    }

//----------------------------------------------------------------------------
    private static void setUpNoFillsList(String fileName){
         try{
             BufferedReader reader = new BufferedReader(new FileReader("Database/" + fileName));
             String line = "";
             while((line = reader.readLine()) != null){
                 //String[] values
                 //System.out.println(line);
                 //System.out.println("----------------------");
             }
         }catch (IOException e){
             e.printStackTrace();
         } 
    }
}