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
    private static List<ProductInfo> productList;
    
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
        //TO DO: create function - priceStringToDouble, indexOfTheCompareItem!!!!!!!!!!!!!!!!!!!
        getKeywordList("bag"); //use this method in GUI search field
    }
//----------------------------------------------------------------------------
    private static void setUp(){
        statisticsHashmap = setUpStatisticsCanadaPriceMatchList(STATISTICS_CANADA_FILENAME);
        costCoItemsMap = setUpCostcoList(COSTCO_FILENAME);
        walmartItemsMap = setUpWalmartList(WALMART_FILENAME);
        sobeysItemsMap = setUpSobeysList(SOBEYS_URL);
        
        System.out.println("StatisticsCanada: " + statisticsHashmap.toString() + "\n");
//        System.out.println("costco: \n" + costCoItemsMap.toString() + "\n");
//        System.out.println("walmart: \n" + walmartItemsMap.toString() + "\n");
//        System.out.println("sobeys: \n" + sobeysItemsMap.toString() + "\n");
    }
    // public HashMap<String, String> getCostCoItemMap(){}
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
    public static ArrayList<String> getKeywordList(String userInput){
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
    private static double priceStringToDouble(String priceString){ //!!!!!!!!!!!!!!!!!!!
        return -100.0;
    }
    
    // finds the matching product and price and puts it into the map
    private static Map<String, Map<String, Double>> updateMatchingMap(String company, Map<String, Map<String, Double>> map,
    		HashMap<String, String> data, String matchingProductName ) {
	 
    	// goes through the company product map and searches for items
    	if (map == null) { map = new HashMap<String, Map<String, Double>>(); }
    	Map<String, Double> productMap = new HashMap<String, Double>();
    	if (data != null && !data.isEmpty()) {
    	  Set<String> keys = data.keySet();
	    	 for (String itemKeyName: keys) {
	    		 if (itemKeyName.contains(matchingProductName)) {
	    			 String valStr = data.get(itemKeyName);
	    			 valStr = valStr.substring(1);
	    			 Double value = Double.valueOf(valStr);
	    			 productMap.put(itemKeyName, value);
	    		 }
	    	 }
    	}
    	
    	if (productMap != null && !productMap.isEmpty())
    	{
    		map.put(company, productMap);
    	}
	    return map;
	    	
    }
    
  	// uses the updateMatchingMap function to search for the product and price from each grocery company 
    public static Map<String, Map<String, Double>> productSearch(String productName) {
    	Map<String, Map<String, Double>> map = new HashMap<String, Map<String, Double>>();
    	map = updateMatchingMap("sobeys", map, sobeysItemsMap, productName );
    	map = updateMatchingMap("costco", map, costCoItemsMap, productName );
    	map = updateMatchingMap("walmart", map, walmartItemsMap, productName );
    	map = updateMatchingMap("noFills", map, noFillsItemsMap, productName );
         
    	return map;
    	
    }
    
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
             
             if (statisticsList != null && !statisticsList.isEmpty()) {
            	 
            
            	 Set<String> keys = statisticsList.keySet();
            	 
            	 for (String key: keys) {
            		
            		 
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
}