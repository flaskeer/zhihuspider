package cc.hao.jsoup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlSpider{
	static int count = 0;
	
	static String url = "http://www.27270.com/ent/meinvtupian/";
	static Document document = null;
	static{
		try {
			document = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36")
					.referrer("http://www.27270.com/ent/meinvtupian/")
					.cookie("Hm_lvt_1ea46a2b041a39dc4bafecbe227a357b", "1446861112")
					.cookie("Hm_lpvt_1ea46a2b041a39dc4bafecbe227a357b", "1446861371")
					.get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void schedule(String url){
		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(10);
		schedule.scheduleAtFixedRate(() -> conn(url), 10, 20, TimeUnit.MILLISECONDS);
	}
	public static void main(String[] args) {
		
		schedule(url);
//		
	}
	
	private static void conn(String url){
		Elements href = document.select("a");

		for (Element element : href) {
			String path = element.attr("href");
			if(path.contains("meinvtupian")){
				download(path);
//					conn(path);
			}

		}
	}

	private static void download(String path) {
		Elements links = document.select("img[src$=.jpg]");
		Elements hrefs = document.select("li > a");
		
		if(links == null){
			return;
		}
		for (Element link : links) {
			String imagePath = link.attr("src");
			if(!imagePath.contains("mx")){
				for (Element href : hrefs) {
					String linkPath = href.attr("href");
					if(linkPath.startsWith("\\d+") && href.text() == "下一页"){
						System.out.println("val is " + href.text());
						linkPath = "http://www.27270.com/ent/meinvtupian/2015/" + linkPath;
						System.out.println("linkpath is :" + linkPath);
						download(linkPath);
					}
					
				}
				System.out.println("正在下载图片：" + imagePath);
				getImages(imagePath,"F://picdata2//000" + ++count + ".jpg");
			}
			
		}
		
	}
	//http://img62.imageshimage.com/th/09892/uehg36driij7.jpg
	private static void getImages(String imagePath, String filePath) {
		FileOutputStream fous = null;
		try {
			System.out.println("---->");
			URL url = new URL(imagePath);
			System.out.println("url is :" + url.toString());
			Connection connect = Jsoup.connect(url.toString());
			Response execute = connect.execute();
			byte[] data = execute.bodyAsBytes();
			fous = new FileOutputStream(new File(filePath));
			fous.write(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			getImages(imagePath, filePath);
			e.printStackTrace();
		}finally{
			try {
				fous.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
