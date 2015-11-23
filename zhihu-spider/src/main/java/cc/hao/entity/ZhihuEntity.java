package cc.hao.entity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cc.hao.spider.Spider;

public class ZhihuEntity {
	
	public String question;
	public String questionDescription;
	private String zhihuUrl;
	private ArrayList<String> answers;
	private String prefix = "http://www.zhihu.com/question/";
	
	public ZhihuEntity(String url) {
		question = "";
		questionDescription = "";
		zhihuUrl = "";
		answers = new ArrayList<>();
		getRealUrl(url);
		String sourceCode = Spider.getSourceCode(zhihuUrl);
		if(sourceCode != null){
			Document document = Jsoup.parse(sourceCode);
			question = document.title();
			Element despElem = document.getElementById("zh-question-detail");
			if(despElem != null){
				questionDescription = despElem.text();
			}
			Elements ansElem = document.getElementsByClass("zm-item-answer");
			for (Element item : ansElem) {
				Element textElem = item.getElementsByClass("zm-editable-content").first();
				answers.add(textElem.text());
			}
		}
		
		
	}
	
	private void getRealUrl(String url) {
		Pattern pattern = Pattern.compile("question/(.*?)/");
		Matcher matcher = pattern.matcher(url);
		if(matcher.find()){
			zhihuUrl = prefix + matcher.group(1);
		}
		
	}
	
	public String writeToString(){
		StringBuilder result = new StringBuilder();
		result.append("���⣺").append(question).append("\r\n");
		result.append("����������").append(questionDescription).append("\r\n");
		result.append("���ӣ�").append(zhihuUrl).append("\r\n\r\n");
		for (int i = 0; i < answers.size(); i++) {
			result.append("�ش�").append(i).append(":").append(answers.get(i)).append("\r\n\r\n\r\n");
		}
		result = new StringBuilder(result.toString().replaceAll("<br>", "\r\n"));
		result = new StringBuilder(result.toString().replaceAll("<.*?>", ""));
		return result.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("���⣺").append(question).append("\n");
		result.append("����������").append(questionDescription).append("\n");
		result.append("���ӣ�").append(zhihuUrl).append("\n");
		result.append("�ش�").append(answers.size()).append("\n");
		return result.toString();
	}
	
}
