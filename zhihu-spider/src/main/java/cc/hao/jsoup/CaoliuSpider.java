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

public class CaoliuSpider {
	static int count = 0;
	
	public static void schedule(String url){
//		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 - 1);
//		ExecutorService pool = Executors.newFixedThreadPool(10);
		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(10);
		schedule.scheduleAtFixedRate(() -> conn(url), 10, 20, TimeUnit.MILLISECONDS);
//		pool.submit(() -> conn(url));
	}
	public static void main(String[] args) {
		
		String url = "http://cl.tuiaa.com/thread0806.php?fid=21";
		schedule(url);
//		
	}
	
	private static void conn(String url){
		try {
			
			Document document = Jsoup.connect(url)
					                 .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36")
					                 .referrer("http://cl.tuiaa.com/thread0806.php?fid=21")
					                 .cookie("__cfduid", "de92d19c05744b8947b935a8249546a861446425480")
					                 .cookie("227c9_lastfid", "0")
					                 .cookie("__utma", "184629883.1404098820.1446425516.1446815899.1446859581.8")
					                 .cookie("__utmb","184629883.2.10.1446859581")
					                 .get();
//			System.out.println(document.toString());
			Elements images = document.select("img");
			Elements href = document.select("a");
			System.out.println(images.toString());
//			System.out.println("href:--->" + href.toString());
			for (Element element : href) {
				String path = element.attr("href");
//				System.out.println("path is:" + path);
				if(path.contains("http")){
					continue;
				}
				if(path.endsWith("html") && path.startsWith("htm") && !path.contains("1111/30611") && !path.contains("0907/341553")){
					System.out.println("end with html:" + path);
					
					path = "http://cl.tuiaa.com/" + path;
					download(path);
				}
//				if(path.startsWith("javascript")){
//					continue;
//				}
//				if(path.contains("upload")){
//					continue;
//				}
//				if(path.startsWith("http")){
//					conn(path);
//				}else{
//					path = "http://cl.tuiaa.com/" + path;
//					download(path);
//				}
			}
		} catch (IOException e) {
			System.err.println(e);
			conn(url);
		}
	}

	private static void download(String path) {
		try {
			Document document = Jsoup.connect(path)
	                 .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36")
	                 .referrer("http://cl.tuiaa.com/thread0806.php?fid=21")
	                 .cookie("__cfduid", "de92d19c05744b8947b935a8249546a861446425480")
	                 .cookie("227c9_lastfid", "0")
	                 .cookie("__utma", "184629883.1404098820.1446425516.1446815899.1446859581.8")
	                 .cookie("__utmb","184629883.2.10.1446859581")
	                 .get();
			Elements links = document.select("img[src$=.jpg]");
			if(links == null){
				return;
			}
			for (Element link : links) {
				String imagePath = link.attr("src");
				System.out.println("ÕýÔÚÏÂÔØÍ¼Æ¬£º" + imagePath);
				
				getImages(imagePath,"F://picdata2//000" + ++count + ".jpg");
//				if(imagePath.startsWith("http://img") && imagePath.contains("imageshimage")){
//				}
			}
		} catch (IOException e) {
			download(path);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			getImages(imagePath, filePath);
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
