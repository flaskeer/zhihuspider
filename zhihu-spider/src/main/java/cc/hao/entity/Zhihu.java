package cc.hao.entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cc.hao.spider.ZhihuSpider;

public class Zhihu {
	private String question;
	private String questionDescription;
	private ArrayList<String> answer;
	private static final String prefix = "http://www.zhihu.com/question/";
	private String zhihuUrl;
	public Zhihu(String url) throws IOException {
		question = "";
		questionDescription = "";
		zhihuUrl = "";
		answer = new ArrayList<>();
		getRealUrl(url);
		System.out.println("spider --" + zhihuUrl + " ... ");
		String content = ZhihuSpider.getSourceCode(zhihuUrl);
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile("zh-question-title.+?<h2.+?>(.+?)</h2>");
		matcher = pattern.matcher(content);
		if(matcher.find()){
			question = matcher.group(1);
		}
		pattern = Pattern.compile("zh-question-detail.+?<div.+?>(.+?)</div>");
		matcher = pattern.matcher(content);
		if(matcher.find()){
			questionDescription = matcher.group(1);
		}
		pattern = Pattern.compile("/answer/content.+?<div class=\"zm-editable-content clearfix\">(.+?)</div>");
		matcher = pattern.matcher(content);
		while(matcher.find()){
//			System.out.println(matcher.group(1));
			answer.add(matcher.group(1));
		}
		
	}
	private void getRealUrl(String url) {
		Pattern pattern = Pattern.compile("question/(.*?)/");
		Matcher matcher = pattern.matcher(url);
		if(matcher.find()){
			zhihuUrl = prefix + matcher.group(1);
//			System.out.println("url " + zhihuUrl);
		}
		
	}
	
	public String writeToString(){
		StringBuilder result = new StringBuilder();
		result.append("问题：").append(question).append("\r\n");
		result.append("问题描述：").append(questionDescription).append("\r\n");
		result.append("连接：").append(zhihuUrl).append("\r\n\r\n");
		for (int i = 0; i < answer.size(); i++) {
			result.append("回答").append(i).append(":").append(answer.get(i)).append("\r\n\r\n\r\n");
		}
		result = new StringBuilder(result.toString().replaceAll("<br>", "\r\n"));
		result = new StringBuilder(result.toString().replaceAll("<.*?>", ""));
		return result.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("问题：").append(question).append("\n");
		result.append("问题描述：").append(questionDescription).append("\n");
		result.append("连接：").append(zhihuUrl).append("\n");
		result.append("回答：").append(answer.size()).append("\n");
		return result.toString();
	}
	
	
}
