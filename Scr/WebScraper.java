import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.List;
import java.util.*;
/**[WebScraper.java]
 * This is final project - price match program
 * This class gets the website's data and save it as......
 * https://youtu.be/yw7B85174JQ 
 * @author Kylie Wong and Michelle Chan, ICS4UE
 * @version 1.1, build May 27, 2022
 */
public class WebScraper {
    private static LinkedHashMap<String, String> sobeysItemsMap = new LinkedHashMap<String, String>();
    final static String SOBEYS_URL = "https://voila.ca/products?source=navigation";
//----------------------------------------------------------------------------
    public static void main(String[] args){
        setUpSobeysList();
    }
//----------------------------------------------------------------------------
    private static void setUpSobeysList(){
        try{
            final Document document = Jsoup.connect(SOBEYS_URL).get();
            //System.out.println(document.outerHtml());
            String title = document.title();
            System.out.println("website: " + title + "\n");
            
            Elements test = document.select("div.layout__Container-sc-1cgl98j-0.laSkfV");
            Element selectByTag = test.last();
            /************************/
            Elements tag = selectByTag.getElementsByTag("h3");
            Elements prices = selectByTag.getElementsByTag("span");
            String itemName = "";
            for(Element tags: tag){
                itemName = tags.text();
                sobeysItemsMap.put(itemName, "");
            }
            System.out.println(sobeysItemsMap.toString() + "\n");
            String spanTagString = "";
            for(Element price: prices){
                spanTagString = spanTagString + price.text();
            }
            
            //System.out.println(spanTagString);
            String[] arrOfStr = spanTagString.split("You have of this item in your cart");
            int indexOfMap = 0;
            for (String a : arrOfStr){
                System.out.println(a);
            }
        }catch (IOException e){
            e.printStackTrace();
        } 
    }
}