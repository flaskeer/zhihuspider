package cc.hao.jsoup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MMSpider {
	static int count = 0;
	
	public static void schedule(String url){
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 - 1);
		pool.submit(() -> conn(url));
	}
//	http://www.zhuamei5.com/home.php?mod=space&uid=7&do=album&id=935
	public static void main(String[] args) {
		
		String url = "http://www.mm131.com/";
		schedule(url);
//		
	}
	
	private static void conn(String url){
		try {
			
			Document document = Jsoup.connect(url)
					                 .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36")
					                 .referrer("http://www.mm131.com/")
					                 .cookie("Hm_lvt_41f1f6047da379b7773e0a67fe71cbf6", "1446785996")
					                 .cookie("Hm_lpvt_41f1f6047da379b7773e0a67fe71cbf6", "1446786019")
					                 .get();
//			System.out.println(document.toString());
			Elements images = document.select("img[src$=.jpg]");
			Elements href = document.select("a");
			System.out.println(images.toString());
			System.out.println("href:--->" + href.toString());
			for (Element element : href) {
				String path = element.attr("href");
				System.out.println("path is:" + path);
				if(path.startsWith("javascript")){
					continue;
				}
				if(path.contains("search")){
					continue;
				}
				if(path.startsWith("http")){
					download(path);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private static void download(String path) {
		try {
			Document document = Jsoup.connect(path)
	                 .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36")
	                 .referrer("http://www.mm131.com/")
	                 .cookie("Hm_lvt_41f1f6047da379b7773e0a67fe71cbf6", "1446785996")
	                 .cookie("Hm_lpvt_41f1f6047da379b7773e0a67fe71cbf6", "1446786019")
	                 .get();
			Elements links = document.select("img[src$=.jpg]");
			for (Element link : links) {
				String imagePath = link.attr("src");
				System.out.println("ÕýÔÚÏÂÔØÍ¼Æ¬£º" + imagePath);
				
				getImages(imagePath,"F://picdata2//000" + ++count + ".jpg");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void getImages(String imagePath, String filePath) {
		FileOutputStream fous = null;
		try {
			System.out.println("---->");
			URL url = new URL(imagePath);
			Connection connect = Jsoup.connect(url.toString());
			Response execute = connect.execute();
			byte[] data = execute.bodyAsBytes();
			fous = new FileOutputStream(new File(filePath));
			fous.write(data);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				fous.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
