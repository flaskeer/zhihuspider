package cc.hao.spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import cc.hao.entity.Zhihu;

public class ZhihuSpider {
	
	public static String getSourceCode(String url) throws IOException{
		StringBuilder sb = new StringBuilder();
		BufferedReader reader;
		URL realUrl = new URL(url);
		try {
			URLConnection connection = realUrl.openConnection();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while((line = reader.readLine()) != null){
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static ArrayList<Zhihu> getRecommandQuestions(String content) throws IOException{
		ArrayList<Zhihu> result = new ArrayList<>();
		Pattern pattern = Pattern.compile("<h2>.+?question_link.+?href=\"(.+?)\".+?</h2>");
		Matcher matcher = pattern.matcher(content);
		while(matcher.find()){
			System.out.println(matcher.group(1));
			Zhihu zhihu = new Zhihu(matcher.group(1));
			result.add(zhihu);
		}
		return result;
	}
	
	public static void main(String[] args) throws IOException {
		String url = "http://www.zhihu.com/explore/recommendations";
		String sourceCode = getSourceCode(url);
		ArrayList<Zhihu> links = getRecommandQuestions(sourceCode);
		StringBuilder content = new StringBuilder();
		for (Zhihu zhihu : links) {
			System.out.println(zhihu.toString());
			content.append(zhihu.writeToString());
		}
		FileUtils.writeStringToFile(new File("F://zhihu//spider"), content.toString());
	}
	

}
