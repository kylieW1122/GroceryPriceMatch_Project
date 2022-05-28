import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
/**[WebScraper.java]
 * This is final project - price match program
 * This class ......
 * 
 * @author Kylie Wong and Michelle Chan, ICS4UE
 * @version 1.0, build May 27, 2022
 */
public class WebScraper {

    public static void main(String[] args) throws Exception {
        final Document document = Jsoup.connect
            ("https://www150.statcan.gc.ca/t1/tbl1/en/tv.action?pid=1810000201&cubeTimeFrame.startMonth=10&cubeTimeFrame.startYear=2021&cubeTimeFrame.endMonth=04&cubeTimeFrame.endYear=2022&referencePeriods=20211001%2C20220401").get();
        //System.out.println(document.outerHtml()); //"http://www.imdb.com/chart/top"

        for (Element row : document.select("table.pub-table.simpleTable dataTable tr")) { //"table.chart.full-width tr"
            System.out.println("hello"); //class="pub-table simpleTable dataTable"
            final String title = row.select(".highlight-row").text(); //.titleColumn a
            //final String rating = row.select(".imdbRating").text();

            System.out.println(title); //+ " -> Rating: " + rating);
        }
    }
}