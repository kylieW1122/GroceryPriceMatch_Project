import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.io.IOException;
import java.io.BufferedReader;  
import java.io.FileReader;  

/**[DataBase.java]
  * This is final project - Grocery Helper Program
  * This class gets the item data from website and CSV flies and save them as HashMaps
  * 
  * @author Kylie Wong and Michelle Chan, ICS4UE
  * @version 2.0, build June 16, 2022
  */
public class DataBase {
    private static HashMap<String, String> sobeysItemsMap;
    private static HashMap<String, String> costCoItemsMap;
    private static HashMap<String, String> walmartItemsMap;   

    private static List<ProductInfo> productList;
    private static ArrayList<String> wholeProductList;
    
    private static HashMap<String, ArrayList<String>> keywordMap;
    
    private static HashMap<String, double[]> statisticsHashmap;
    private static ArrayList<String> statisticsItemNames;
    
    final static String SOBEYS_URL = "https://voila.ca/products?source=navigation&sublocationId=43a936d1-df1d-4bf1-a09c-b23c6a8edf63";  
    final static String STATISTICS_CANADA_FILENAME = "resources/Database/1810000201-eng.csv";
    final static String COSTCO_FILENAME = "resources/Database/costCo.csv";
    final static String WALMART_FILENAME = "resources/Database/walmart.csv";
//----------------------------------------------------------------------------
    DataBase(){
        keywordMap = new HashMap<String, ArrayList<String>>();
        wholeProductList = new ArrayList<String>();
        statisticsItemNames = new ArrayList<String>();
        setUp();
    }
//----------------------------------------------------------------------------
    private static void setUp(){
        statisticsHashmap = setUpStatisticsCanadaPriceMatchList(STATISTICS_CANADA_FILENAME);
        costCoItemsMap = setUpCostcoList(COSTCO_FILENAME);
        walmartItemsMap = setUpWalmartList(WALMART_FILENAME);
        sobeysItemsMap = setUpSobeysList(SOBEYS_URL);
    }
//----------------------------------------------------------------------------
    private static void addKeyword(String itemName, String storeName){
        wholeProductList.add(itemName + " @ " + storeName);
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
                    keywordMap.put(str, arrayList); 
                }
            }
        }
    }
//----------------------------------------------------------------------------
    public static HashMap<String, ArrayList<String>> getKeywordMap(){
        return keywordMap;
    }
//----------------------------------------------------------------------------
    public static ArrayList<String> getWholeList(){
        return wholeProductList;
    }
//----------------------------------------------------------------------------
    public static ArrayList<String> getStatisticsItemList(){
        return statisticsItemNames;
    }
//----------------------------------------------------------------------------
    public static ArrayList<String> searchItemKeyword(String userInput){ //sort the list
        ArrayList<String> searchResult = new ArrayList<String>();
        userInput = userInput.toLowerCase();
        for(String str: keywordMap.keySet()){
            if(str.indexOf(userInput)!=-1){
                searchResult.addAll(keywordMap.get(str));
            }
        }
        Set<String> set = new HashSet<String>(searchResult); // remove all duplicates
        searchResult.clear();
        searchResult.addAll(set);
        return searchResult; 
    }
//----------------------------------------------------------------------------
    // matches the product item name and the year from the statistics Canada data
    public static Map<String, Double> getPriceListByItemNYear(String item, String year) {
        Map<String, Double> map = new HashMap<String, Double>();
        
        if (productList != null && !productList.isEmpty()) {
            for (ProductInfo prodInfo: productList) {
                if (prodInfo.getItemName().equalsIgnoreCase(item) && prodInfo.getMonYear().contains(year)) {
                    map.put(prodInfo.getMonYear(), prodInfo.getPrice());
                }
            }
        }
        return map;
    }
//----------------------------------------------------------------------------
    private static HashMap<String, double[]> setUpStatisticsCanadaPriceMatchList(String fileName){ //DONE
        HashMap<String, double[]> statisticsList = new  HashMap<String, double[]>();
        if (productList == null || productList.isEmpty()) {
            productList = new ArrayList<ProductInfo>();
        }
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
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
            
            if (statisticsList != null && !statisticsList.isEmpty()) {
                Set<String> keys = statisticsList.keySet();
                
                for (String key: keys) {
                    statisticsItemNames.add(key);
                    double[] prices = statisticsList.get(key);
                    for (int i=0; i < prices.length; i++) {
                        ProductInfo prodInfo = new ProductInfo();
                        prodInfo.setItemName(key);
                        prodInfo.setMonYear(timeline[i]);
                        
                        prodInfo.setPrice(prices[i]);
                        productList.add(prodInfo);
                    }
                }
            }
            /*******************************************************************************/
        }catch (IOException e){
            //leave it as an empty list
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
            //leave it as an empty list
        } 
        return sobeysList;
    }
//----------------------------------------------------------------------------
    private static HashMap<String, String> setUpCostcoList(String fileName){ 
        HashMap<String, String> costCoList = new HashMap<String, String>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
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
            //leave it as an empty list
        } 
        return costCoList;
    }
//----------------------------------------------------------------------------
    private static HashMap<String, String> setUpWalmartList(String fileName){ 
        HashMap<String, String> walmartList = new HashMap<String, String>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
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
            //leave it as an empty list
        }
        return walmartList;
    }
//----------------------------------------------------------------------------
    /**inner class - ProductInfo
     * @author Michelle
     * Stores the product info in this Object
     */
//----------------------------------------------------------------------------
    private static class ProductInfo {
        
        private String monYear;
        private String itemName;
        private Double price;
        public String getMonYear() { return monYear; }
        public void setMonYear(String monYear) { this.monYear = monYear; }
        public String getItemName() {
            return itemName;
        }
        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
        public Double getPrice() {
            return price;
        }
        public void setPrice(Double price) {
            this.price = price;
        } 
    }
}