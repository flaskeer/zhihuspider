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

public class ZhihuCrawl {
	static int count = 0;
	
	public static void schedule(String url){
		ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 - 1);
		pool.submit(() -> conn(url));
	}
//	http://www.zhuamei5.com/home.php?mod=space&uid=7&do=album&id=935
	public static void main(String[] args) {
		
		String url = "http://www.zhuamei5.com/home.php?mod=space&do=album&catid=10&view=all";
		schedule(url);
//		for (int i = 1; i < 200; i++) {
//			String url = "http://www.zhuamei5.com/home.php?mod=space&uid=11279&do=album&view=me&from=space";
//			url = url + i;
//			System.out.println(url);
//			schedule(url);
////			conn(url);
//		}
	}
	
	private static void conn(String url){
		try {
			
			Document document = Jsoup.connect(url)
					                 .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36")
//					                 .referrer("http://www.lofter.com/recommend?blogId=2923622")
//					                 .cookie("usertrack", "ZUcIhlUfiVyV20kCCJNTAg==")
//					                 .cookie("JSESSIONID-WLF-XXD", "915d6303f35d559e742d554e2b1534f8bc004b8d0847812992f9cc485a8b2279854e9e16577c9eff08eb8b18a2b97066b3f6a23642c725b175c62cd20b169d910b491338f51b4c522dc1ad9165714626de76a96a075e34b2212515237b0dac6cb40489eef81dde14e7d63200d966efa9e3a26b6031ce38023bceb009416385facbd390a0")
					                 .referrer("http://www.zhuamei5.com/home.php?mod=space&uid=11279&do=album&view=me&from=space")
					                 .cookie("pgv_pvi", "3431659775")
					                 .cookie("A7p0_2132_sid", "JYpM3l")
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
				if(path.startsWith("http")){
					download(path);
				}else{
					path = "http://www.zhuamei5.com/" + path;
					download(path);
				}
			}
//			for (Element link : images) {
//				String imagePath = link.attr("src");
//				imagePath = "http://www.zhuamei5.com/" + imagePath;
//				System.out.println("正在下载图片：" + imagePath);
//				
//				getImages(imagePath,"F://picdata2//000" + ++count + ".jpg");
//			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private static void download(String path) {
		try {
			Document document = Jsoup.connect(path)
			        .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36")
			        .referrer("http://www.zhuamei5.com/home.php?mod=space&uid=11279&do=album&view=me&from=space")
			        .cookie("pgv_pvi", "3431659775")
			        .cookie("A7p0_2132_sid", "JYpM3l")
			        .get();
			Elements links = document.select("img[src$=.jpg]");
			for (Element link : links) {
				String imagePath = link.attr("src");
				imagePath = "http://www.zhuamei5.com/" + imagePath;
				System.out.println("正在下载图片：" + imagePath);
				
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
