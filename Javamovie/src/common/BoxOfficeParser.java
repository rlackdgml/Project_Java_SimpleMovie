package common;

import java.io.BufferedInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class BoxOfficeParser {
	String key = "30c7cba34a78181d36ef237ddf4f371f";
	String today  = "";
	String[][] mvRank = new String[10][12];
	String url = "";
	
	public BoxOfficeParser() {
		this.url = makeURL();
		//System.out.println("=======================================");
		//System.out.println("LOG: Parsing URL Completed >>>>"+ url);
		//System.out.println("=======================================");
		
	}
	
	
	public String makeURL() {
		// 오늘 날짜 구하기
		
		Calendar cal =Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		
		//System.out.println("포맷 전: "+ cal.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		today = sdf.format(cal.getTime());
		
	//	System.out.println("포맷 후: " + today);
		
		String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/"
					+"searchDailyBoxOfficeList.json"
					+"?key=" + key
					+ "&targetDt=" + today;
				
		return url;
	}
	// 2. web상의 URL주소 JSON데이터를 읽음
	private String readUrl(String preUrl) throws Exception {
		BufferedInputStream reader = null;
		
		try {
			URL url = new URL(preUrl);
			reader = new BufferedInputStream(url.openStream());
			StringBuffer buffer = new StringBuffer();
			int i;
			byte[] b = new byte[4096];
			while((i = reader.read(b)) != - 1) {
				buffer.append(new String(b, 0 ,i));
			}
			return buffer.toString();
		}finally {
			if(reader != null) {
				reader.close();
			}
		}
	}
	
	// 3. Data Parsing
	public String[][] getParser() throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject)parser.parse(readUrl(url));
		JSONObject json = (JSONObject)obj.get("boxOfficeResult");
		JSONArray array =(JSONArray)json.get("dailyBoxOfficeList");
		
		for(int i = 0; i < array.size(); i++) {
			JSONObject entity = (JSONObject)array.get(i);
			String rank = (String) entity.get("rank");  		// 순위
			String movieNm = (String) entity.get("movieNm"); 	// 영상제목
			String audiAcc = (String) entity.get("audiAcc"); 	//누적관객수
			String salesAcc = (String) entity.get("salesAcc");	//누적매출액
			mvRank[i][0] = rank;
			mvRank[i][1] = movieNm;
			mvRank[i][8] = audiAcc;
			mvRank[i][9] = salesAcc;
			
		}
		return mvRank;
	}

	 

}
