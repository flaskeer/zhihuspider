package cc.hao.spider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cc.hao.entity.ZhihuEntity;

public class Spider {
	
	public static String getSourceCode(String url){
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);
		try {
			CloseableHttpResponse resp = client.execute(request);
			String result = EntityUtils.toString(resp.getEntity());
			System.out.println(result);
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static ArrayList<ZhihuEntity> getRecommendations(String content){
		ArrayList<ZhihuEntity> results = new ArrayList<>();
		Document doc = Jsoup.parse(content);
		Elements items = doc.getElementsByClass("zm-item");
		for (Element element : items) {
			Element h2Elem = element.getElementsByTag("h2").first();
			Element aTag = h2Elem.getElementsByTag("a").first();
			String href = aTag.attr("href");
			if(href.contains("question")){
				results.add(new ZhihuEntity(href));
			}
		}
		return results;
	}
	
	public static void main(String[] args) {
		String url = "http://www.zhihu.com/explore/recommendations";
		String content = Spider.getSourceCode(url);
		ArrayList<ZhihuEntity> results = Spider.getRecommendations(content);
		for (ZhihuEntity zhihuEntity : results) {
			try {
				FileUtils.writeStringToFile(new File("F://zhihu/recommand.txt"), zhihuEntity.toString(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
