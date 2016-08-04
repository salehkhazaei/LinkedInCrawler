
package linkedincrawler;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Saleh Khazaei
 * @email saleh.khazaei@gmail.com
 */
public class Profile {
    public String name;
    public String link;
    public String headline;
    public String demographic_dt;
    public String demographic_dd;
    
    
    public void crawl () throws IOException 
    {
        crawl(Jsoup.connect(link).get());
    }
    public void crawl ( Document doc ) 
    {
        
    }
}
