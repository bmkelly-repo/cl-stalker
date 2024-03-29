/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler4j;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;
import java.util.regex.Pattern;
import java.io.*;

/**
 *
 * @author Kelz
 */
public class DataCollectorCrawler extends WebCrawler{
    
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
                        + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    CrawlStat myCrawlStat;
    
    /**
         * You should implement this function to specify whether the given url
         * should be crawled or not (based on your crawling logic).
         */
        @Override
        public boolean shouldVisit(WebURL url) {
                String href = url.getURL().toLowerCase();
                return !FILTERS.matcher(href).matches();
        }
        

        /**
         * This function is called when a page is fetched and ready to be processed
         * by your program.
         */
        @Override
        public void visit(Page page) {
                int docid = page.getWebURL().getDocid();
                String url = page.getWebURL().getURL();
                String domain = page.getWebURL().getDomain();
                String path = page.getWebURL().getPath();
                String subDomain = page.getWebURL().getSubDomain();
                String parentUrl = page.getWebURL().getParentUrl();

                System.out.println("Docid: " + docid);
                System.out.println("URL: " + url);
                System.out.println("Domain: '" + domain + "'");
                System.out.println("Sub-domain: '" + subDomain + "'");
                System.out.println("Path: '" + path + "'");
                System.out.println("Parent page: " + parentUrl);

                if (page.getParseData() instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    String text = htmlParseData.getText();
                    String html = htmlParseData.getHtml();
                    List<WebURL> links = htmlParseData.getOutgoingUrls();

                    System.out.println("Text length: " + text.length());
                    System.out.println("Html length: " + html.length());
                    System.out.println("Number of outgoing links: " + links.size());

                    //System.out.println("This is the text : " );
                    //System.out.println(text);
                
                
                    try
                    {

                        // Create file
                        int avoid = text.indexOf("Avoid scam");
                        System.out.println("avoid : " + avoid);
                        text = text.substring(avoid, text.length());
                        FileWriter fstream = new FileWriter(docid+".txt");
                        BufferedWriter out = new BufferedWriter(fstream);
                        out.write(text);
                        //Close the output stream
                        out.close();
                        }catch (Exception e){//Catch exception if any
                        System.err.println("Error: " + e.getMessage());
                        
                    }
                }

                System.out.println("=============");
        }
        
         @Override
        public Object getMyLocalData() {
                return myCrawlStat;
        }

        // This function is called by controller before finishing the job.
        // You can put whatever stuff you need here.
        @Override
        public void onBeforeExit() {
                dumpMyData();
        }

        public void dumpMyData() {
                int myId = getMyId();
                // This is just an example. Therefore I print on screen. You may
                // probably want to write in a text file.
                System.out.println("Crawler " + myId + "> Processed Pages: " + myCrawlStat.getTotalProcessedPages());
                System.out.println("Crawler " + myId + "> Total Links Found: " + myCrawlStat.getTotalLinks());
                System.out.println("Crawler " + myId + "> Total Text Size: " + myCrawlStat.getTotalTextSize());
        }
    
}
