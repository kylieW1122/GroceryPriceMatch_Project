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
 * @version 3.0, build June 3, 2022
 */
public class DataBase {
    private static HashMap<String, String> sobeysItemsMap;
    private static HashMap<String, String> costCoItemsMap;
    private static HashMap<String, String> walmartItemsMap;   
    private static HashMap<String, String> noFillsItemsMap;
    
    private static HashMap<String, ArrayList<String>> keywordMap;
    
    private static HashMap<String, double[]> statisticsHashmap;

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
        keywordMap = new HashMap<String, ArrayList<String>>();
        setUp();
     
        searchItemKeyword("bag"); //use this method in GUI search field
        //System.out.println("final: " + keywordMap.keySet().toString() + "size: " + keywordMap.size());
    }
//----------------------------------------------------------------------------
    private static void setUp(){
        statisticsHashmap = setUpStatisticsCanadaPriceMatchList(STATISTICS_CANADA_FILENAME);
        costCoItemsMap = setUpCostcoList(COSTCO_FILENAME);
        walmartItemsMap = setUpWalmartList(WALMART_FILENAME);
        sobeysItemsMap = setUpSobeysList(SOBEYS_URL);
//        noFillsItemsMap = setUpNoFillsList(NOFILLS_FILENAME);
//        System.out.println("noFills: \n" + noFillsItemsMap.toString() + "\n");
        
//        System.out.println("StatisticsCanada: " + statisticsHashmap.toString() + "\n");
        System.out.println("costco: \n" + costCoItemsMap.toString() + "\n");
        System.out.println("walmart: \n" + walmartItemsMap.toString() + "\n");
        System.out.println("sobeys: \n" + sobeysItemsMap.toString() + "\n");
    }
//----------------------------------------------------------------------------
    private static void addKeyword(String itemName, String storeName){
        String temp = itemName.toLowerCase();
        ArrayList<String> removeTargets = new ArrayList<String>();
        Collections.addAll(removeTargets, "kg", "g", "lb", "ml", "oz", "x");
        
        temp = temp.replaceAll("\\d", ""); 
        temp = temp.replaceAll("\\p{Punct}", "");
        String infoName = storeName + " - " + itemName;
        String[] arr = temp.split(" ");
        for(String str: arr){
            str = str.replaceAll(" ", "");
            if( (removeTargets.contains(str)) || (str.equals(""))){
                //do not add these into the keyword list
            }else{
                if(keywordMap.containsKey(str)){
                    ArrayList<String> tempList = keywordMap.get(str);
                    tempList.add(infoName);
                }else{
                    ArrayList<String> arrayList = new ArrayList<String>();
                    arrayList.add(infoName);
                    //indexOfTheCompareItem(arrayList, infoName, 1);
                    keywordMap.put(str, arrayList); 
                }
            }
        }
    }
//----------------------------------------------------------------------------
    public static ArrayList<String> searchItemKeyword(String userInput){
        userInput = userInput.toLowerCase();
        if(keywordMap.containsKey(userInput)){
            //System.out.println("Yes\n" + keywordMap.get(userInput).toString());
            return keywordMap.get(userInput);
        }
        return null; 
    }
//----------------------------------------------------------------------------
    public static int indexOfTheCompareItem(ArrayList<String> arrayList, String targetItem, int indexOfStore){
        String storeName = targetItem.substring(0, targetItem.indexOf(" - "));
        String itemName = targetItem.substring(targetItem.indexOf(" - ") + 3);
        String priceString = targetItem.substring(targetItem.indexOf(" = " + 3));
        double itemPrice = priceStringToDouble(priceString);
        
       // System.out.println(storeName + " " + itemName + " " + itemPrice);
//        for(int i=0; i<arrayList.size(); i++){
//            
//        }
        return -1;
    
    }
//----------------------------------------------------------------------------
    private static double priceStringToDouble(String priceString){
        
        return -100.0;
    }
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
//----------------------------------------------------------------------------
    private static HashMap<String, String> setUpSobeysList(String url){ 
         HashMap<String, String> sobeysList = new  HashMap<String, String>();
        try{
            final Document document = Jsoup.connect(url).get();
            ArrayList<String> itemNameArrayList = new ArrayList<String>();
            //System.out.println(document.outerHtml());
            Elements test = document.select("div.layout__Container-sc-1cgl98j-0.laSkfV");
            Element selectByTag = test.last();
            /************************/
            Elements tag = selectByTag.getElementsByTag("h3");
            Elements prices = selectByTag.getElementsByTag("strong");
            String itemName = "";
            for(Element tags: tag){
                itemName = tags.text();
                itemNameArrayList.add(itemName);
            }
            String spanTagString = "";
            for(Element price: prices){
                spanTagString = spanTagString + price.text() + "~";
            }
            
            String[] arrOfStr = spanTagString.split("~");
            int indexOfMap = 0;
            for (String a : arrOfStr){
                String itemNameTemp = itemNameArrayList.get(indexOfMap);
                sobeysList.put(itemNameTemp, a);
                addKeyword(itemNameTemp + " = " + a ,"Sobeys");
                indexOfMap++;
            }
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
                 String itemNameTemp = values[2];
                 String itemPriceTemp = values[3];
                 costCoList.put(itemNameTemp, itemPriceTemp);
                 addKeyword(itemNameTemp + " = " + itemPriceTemp, "CostCo");
             }
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
                 String itemNameTemp = values[2] + " " + values[3];
                 String itemPriceTemp = values[4];
                 walmartList.put(itemNameTemp, itemPriceTemp);
                 addKeyword(itemNameTemp + " = " + itemPriceTemp,"Walmart");
             }
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
                 noFillsList.put(values[2].replaceAll("\"", ""), temp); //format: itemName = price/kg, price(as a whole quantity)
//                 addKeyword(itemNameTemp,"NoFills");
             }
         }catch (IOException e){
             e.printStackTrace();
         } 
         return noFillsList;
    }
}