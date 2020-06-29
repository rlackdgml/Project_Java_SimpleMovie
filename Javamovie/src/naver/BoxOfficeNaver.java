package naver;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BoxOfficeNaver {
	String url = "https://movie.naver.com/movie/running/current.nhn";
	String title = "";      // 영화 제목
	String score = "";      // 영화 평점
	String bookRate = "0";	// 영화 예매율
	String type = "";		// 영화 장르
	String movieTime = "";	// 영화 상영시간
	String openDt = "";		// 영화 개봉일
	String director = "";	// 영화 감독
	String actor = "";		// 출연진
	String naverCode = "";  // 네이버 영화 코드
	int finalCnt = 0; //  수집을 멈추기 위한 변수(1~10위까지 완료)
	
	
	public String[][] naverMovieRank(String[][] mvRank) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements movieList = doc.select("div.lst_wrap > ul.lst_detail_t1 > li");
		
		for(Element movie : movieList) {
			if(finalCnt == 10) {
				// 1~10위까지의 영화정보 수집 완료! 빠져나가세용
				break;
			}
			// 네이버 영화정보 크롤링
			title = movie.select("dt.tit > a").text();  // 영화제목
			
			int flag = 999;
			for(int i =0; i < mvRank.length; i++) {
				if(mvRank[i][1].equals(title)) {
					// BoxOffice 1~10위 권내의 영화로 판별 크롤링
					flag = i; // 0~9값만 INPUT          //전버전에 다잇고 8부터 라벨을 쓸수잇음
					break;
				}
			}
			
			// 1~10위권 외의 영화 → 크롤링 x
			// flag가 0~9사이의 값이면 크롤링 시작 
			// 그게 아니면 하지 않음
			if(flag == 999) {
				continue;
			}
			
			// 예매율, 감독 ,출연진 초기화
			mvRank[flag][2] = "0"; 
			director = "";
			actor = "";
			

			if (movie.select("span.num").size() == 2) {
				 bookRate = movie.select("span.num ").get(1).text();  // 예매율
			} 
			score = movie.select("span.num").get(0).text(); // 영화평점
			type = movie.select("dd > span.link_txt").get(0).text(); //영화 장르
			
			
			String temp = movie.select("dl.info_txt1 > dd").get(0).text();
			int beginTimeIndex = temp.indexOf("|");
			int endTimeIndex = temp.lastIndexOf("|");
			if(beginTimeIndex ==  endTimeIndex) { // 상영시간
				movieTime = temp.substring(0 , endTimeIndex);
			} else {
				movieTime = temp.substring(beginTimeIndex+2, endTimeIndex);
			}
			
			// System.out.println(movie.select("dt.tit_t2")); // 감독
			// System.out.println(movie.select("dt.tit_t3")); // 출연진
			
			// 0 :무 , 1 :유
			int dCode = 0; // 감독유무 확인
			int aCode = 0;  // 출연진 유무 확인
			
			if(!movie.select("dt.tit_t2").text().equals("")) {
				dCode = 1; // 감독있음!
			}
			
			if(!movie.select("dt.tit_t3").text().equals("")) {
				aCode = 1; // 출연진잇음!
			}
			if(dCode == 1 && aCode == 0) {
				director = movie.select("dd > span.link_txt").get(1).text(); 
			} else if(dCode == 0 && aCode == 1) {
				actor = movie.select("dd > span.link_txt").get(1).text();
			} else if(dCode == 1 && aCode == 1) {
				director = movie.select("dd > span.link_txt").get(1).text(); // 감독
				actor = movie.select("dd > span.link_txt").get(2).text();  // 출연진
			}
			
			
			String naverHref = movie.select("dt.tit > a").attr("href");  // 네이버 영화 URL
			naverCode = naverHref.substring(naverHref.lastIndexOf("=")+1); // 네이버 영화 
			
			// 영화개봉일자
			int openDtTxtIndex = temp.lastIndexOf("개봉");
			openDt = temp.substring(endTimeIndex+2, openDtTxtIndex);
			
			// 수집된 영화정보를 mvRank input
			mvRank[flag][2] = bookRate;
			mvRank[flag][3] = type;
			mvRank[flag][4] = movieTime.trim();
			mvRank[flag][5] = openDt.trim();
			mvRank[flag][6] = director;
			mvRank[flag][7] = actor;
			mvRank[flag][10] = naverCode;
			finalCnt += 1;
		}	
		return mvRank;
		
		
		
	}

}
