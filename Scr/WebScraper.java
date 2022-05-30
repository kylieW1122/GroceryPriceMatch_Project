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
 * @version 1.2, build May 28, 2022
 */
public class WebScraper {
    private static HashMap<String, String> sobeysItemsMap = new HashMap<String, String>();
    final static String STATISTICS_URL = "https://www150.statcan.gc.ca/t1/tbl1/en/tv.action?pid=1810000201&cube" + 
        "TimeFrame.startMonth=10&cubeTimeFrame.startYear=2021&cubeTimeFrame.endMonth=04&cubeTimeFrame.endYear=2022&" + 
        "referencePeriods=20211001%2C20220401";
    
    final static String WALMART_URL = "https://www.walmart.ca/browse/grocery/10019";
    final static String COSTCO_URL = "https://www.costcobusinesscentre.ca/fruits-vegetables.html?sortBy=+"; 
    //https://www.costcobusinesscentre.ca/grocery.html
    final static String NO_FILLS_URL = "https://www.nofrills.ca/food/fruits-vegetables/c/28000?navid=flyout-L2-fruits-vegetables";
    final static String SOBEYS_URL = "https://voila.ca/products?source=navigation&sublocationId=43a936d1-df1d-4bf1-a09c-b23c6a8edf63";    
    final static String FORTINO_URL = "https://www.loblaws.ca/food/fruits-vegetables/c/28000?navid=flyout-L2-fruits-vegetables";
    //"https://www.longos.com/Fresh-Fruits-and-Vegetables/c/701";
    //"https://www.fortinos.ca/food/fruits-vegetables/c/28000?navid=flyout-L2-fruits-vegetables";
//----------------------------------------------------------------------------
    WebScraper(){
        setUpStatisticsCanadaPriceMatchList(STATISTICS_URL);
        //setUpCostcoList(COSTCO_URL);
       // setUpFortinoList(FORTINO_URL);
//        setUpWalmartList(WALMART_URL);
//        setUpNoFillsList(NO_FILLS_URL);
        setUpSobeysList(SOBEYS_URL);
    }
//----------------------------------------------------------------------------
    public static HashMap<String, String> getSobeysItemList(){
        return sobeysItemsMap;
    }
//----------------------------------------------------------------------------
    private static void setUpStatisticsCanadaPriceMatchList(String url){
         try{
             BufferedReader reader = new BufferedReader(new FileReader("Database/StatisticsCanada/1810000201-eng.csv"));
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
    private static void setUpCostcoList(String url){ //missing prices
         try{
            final Document document = Jsoup.connect(url).get();
            //System.out.println(document.outerHtml());
            String title = document.title();
            System.out.println("website: " + title + "\n");
            
            Elements prices = document.getElementsByTag("span");
            String itemName = "";
            for(Element tag: prices){
                //itemName = tags.text();
               // sobeysItemsMap.put(itemName, "");
                System.out.println(tag.text());
            }

        }catch (IOException e){
            e.printStackTrace();
        } 
    }
//----------------------------------------------------------------------------
    private static void setUpFortinoList(String url){
         try{
            final Document document = Jsoup.connect(url).get();
            System.out.println(document.outerHtml());
            String title = document.title();
            System.out.println("website: " + title + "\n");
            
            Elements prices = document.getElementsByTag("span");
            String itemName = "";
            for(Element tag: prices){
                //itemName = tags.text();
               // sobeysItemsMap.put(itemName, "");
              //  System.out.println(tag.text());
            }

        }catch (IOException e){
            e.printStackTrace();
        } 
    }
//----------------------------------------------------------------------------
    private static void setUpWalmartList(String url){ //failed
         try{
            final Document document = Jsoup.connect(url).get();
            System.out.println(document.outerHtml());
            String title = document.title();
            System.out.println("website: " + title + "\n");
        }catch (IOException e){
            e.printStackTrace();
        } 
    }

//----------------------------------------------------------------------------
    private static void setUpNoFillsList(String url){ //failed
         try{
            final Document document = Jsoup.connect(url).get();
            //System.out.println(document.outerHtml());
            System.out.println(document.select(""));
            String title = document.title();
            System.out.println("website: " + title + "\n");
        }catch (IOException e){
            e.printStackTrace();
        } 
    }
}