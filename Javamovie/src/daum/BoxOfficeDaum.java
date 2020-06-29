package daum;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BoxOfficeDaum {
	String baseurl = "http://ticket2.movie.daum.net/Movie/MovieRankList.aspx";
	int finalCnt = 0;
	
	public String[][] daumMovieRank(String[][] mvRank)throws IOException {
			Document doc = Jsoup.connect(baseurl).get();
			Elements movieList = doc.select("div.desc_boxthumb > strong.tit_join > a");
			
			for(Element movie : movieList) {
				if(finalCnt == 10) {
					break;
				}

				String title = movie.text();
				int flag = 999;
				for(int i =0; i < mvRank.length; i++) {
					if(mvRank[i][1].equals(title)) {
						// BoxOffice 1~10위 권내의 영화로 판별 크롤링
						flag = i; // 0~9값만 INPUT          //전버전에 다잇고 8부터 라벨을 쓸수잇음
						break;
					}
				}
				
				if(flag == 999) {
					continue;
				}
				
				
				
				
				String url = movie.attr("href");
				Document movieDoc = Jsoup.connect(url).get();
				
				// 상세 영화 페이지가 없는 영화는 제거
				if(movieDoc.select("span_txt_name").size() == 0) {
					continue;
				}
					
				
				
				
				
				String daumHref = movieDoc.select("a.area_poster").get(0).attr("href");
				String daumCode = daumHref.substring(daumHref.lastIndexOf("=")+1,daumHref.lastIndexOf("#"));
				
				// 수집된 영화정보를 mvRank input
				mvRank[flag][11] = daumCode;
				finalCnt += 1;
				
				
				
				
				
			}
			
			return mvRank;
	

	}
}	
