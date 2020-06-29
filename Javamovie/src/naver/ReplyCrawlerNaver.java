package naver;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import domain.ReplyDTO;
import persistence.ReplyDAO;

public class ReplyCrawlerNaver {

	int page = 1;
	int cnt = 0;
	int total = 0;
	String prePage = "";
	
	ReplyDAO rDao = new ReplyDAO();

	public HashMap<String,Integer> naverCrawler(String movieNm, String naverCode) throws IOException {
	

		while (true) {
			String url = "https://movie.naver.com/movie/bi/mi/pointWriteFormList.nhn?code="+naverCode+"&type=after&isActualPointWriteExecute=false&isMileageSubscriptionAlready=false&isMileageSubscriptionReject=false&page="
					+ page;
			Document doc = Jsoup.connect(url).get();
			Elements movieList = doc.select("div.score_result > ul > li");
			String nowPage = doc.select("input#page").attr("value");
			// System.out.println(prePage + "," + nowPage);

			if (nowPage.equals(prePage)) {
				break;
			} else {
				prePage = nowPage;
			}

			String content = "";
			int score = 0;
			String writer = "";
			String regdate = "";
			
			
			for (Element movie : movieList) {
				content = movie.select("div.score_reple > p > span").get(0).text();
				score = Integer.parseInt(movie.select("div.star_score > em").get(0).text());
				writer = movie.select("div.score_reple a > span").get(0).text();
				regdate = movie.select("div.score_reple em").get(1).text().substring(0, 10);

				System.out.println(
						"■■■■■ [Naver] ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
				System.out.println("■■■ 내용 :" + content);
				System.out.println("■■■ 영화평점 :" + score);
				System.out.println("■■■작성자 :" + writer);
				System.out.println("■■■ 작성시간 :" + regdate);
				
				// MongoDB 저장
				ReplyDTO rDto = new ReplyDTO(movieNm, content, writer, score, regdate);
				// System.out.println(rDto.toString());
				rDao.addReply(rDto);
				total += score;
				cnt++;

			}
			page++; // page += 1; 해도 됨

		}
		System.out.println("■■〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("■■ NAVER :" + cnt + "건 수집했습니다.");
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("cnt" , cnt);
		map.put("total", total);
		
		return map;

	}

}
