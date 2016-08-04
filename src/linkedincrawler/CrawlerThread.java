package linkedincrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Saleh Khazaei
 * @email saleh.khazaei@gmail.com
 */
public class CrawlerThread extends Thread {

    HashSet<String> crawled_links;
    HashSet<String> noncrwaled_links;
    ArrayList<Profile> profiles;
    public boolean stop = false;

    public CrawlerThread(HashSet<String> crawled_links, HashSet<String> noncrwaled_links, ArrayList<Profile> profiles) {
        this.crawled_links = crawled_links;
        this.noncrwaled_links = noncrwaled_links;
        this.profiles = profiles;
    }

    @Override
    public void run() {
        while (!stop) {
            if (noncrwaled_links.size() > 0) {
                try {
                    String str;
                    synchronized ( noncrwaled_links )
                    {
                        if ( noncrwaled_links.size() <= 0 )
                            continue;
                        str = noncrwaled_links.iterator().next();
                        noncrwaled_links.remove(str);
                    }

                    Document doc = Jsoup.connect(str).get();

                    String name = doc.select("#topcard").select(".profile-overview-content").select("#name").first().text();

                    FileOutputStream file = new FileOutputStream(new File(name + ".xml"));
                    file.write(doc.toString().getBytes());

                    System.out.println("Crawling " + name + "'s profile.");

                    Profile pf = new Profile();
                    pf.name = name;
                    pf.link = str;
                    pf.crawl(doc);

                    Elements peoples = doc.select("#aux").select(".browse-map").select("ul").select("li");
                    for (int i = 0; i < peoples.size(); i++) {
                        Element p = peoples.get(i).select(".item-title").select("a").first();
                        String nn = p.text();
                        String ll = p.attr("href");

                        if (!crawled_links.contains(ll)) {
                            noncrwaled_links.add(ll);
                        }
                    }
                    crawled_links.add(str);
                    System.out.println("Number of crawled profiles: " + crawled_links.size() + ", Non-crawled ones: " + noncrwaled_links.size());
                } catch (IOException ex) {
                    Logger.getLogger(LinkedinCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CrawlerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
