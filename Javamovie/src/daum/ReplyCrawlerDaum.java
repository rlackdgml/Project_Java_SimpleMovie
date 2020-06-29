package daum;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import domain.ReplyDTO;
import persistence.ReplyDAO;

public class ReplyCrawlerDaum {

	int page = 1;
	int cnt = 0;
	int total = 0;
	String prePage = "";

	ReplyDAO rDao = new ReplyDAO();

	public HashMap<String, Integer> daumCrawler(String movieNm, String daumCode) throws IOException {

		while (true) {
			String url = "https://movie.daum.net/moviedb/grade?movieId=" + daumCode + "&type=netizen&page=" + page;
			Document doc = Jsoup.connect(url).get();
			Elements replyList = doc.select("div.review_info");

			if (replyList.size() == 0) {
				break;
			}

			String content = "";
			String writer = "";
			int score = 0;
			String regdate = "";

			for (Element movie : replyList) {

				content = movie.select("p.desc_review").text();
				score = Integer.parseInt(movie.select("em.emph_grade").text());
				writer = movie.select("em.link_profile").text();
				regdate = movie.select("span.info_append").text().substring(0, 10);

				System.out.println(
						"[DAUM] ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
				System.out.println("■■■ 내용 :" + content);
				System.out.println("■■■ 영화평점 :" + score);
				System.out.println("■■■ 작성자 :" + writer);
				System.out.println("■■■ 작성일자 :" + regdate);

				// MongoDB에 저장 (댓글 1건)
				ReplyDTO rDto = new ReplyDTO(movieNm, content, writer, score, regdate);
				rDao.addReply(rDto);
				total += score;
				cnt++;

			}
			page++;

		}
		System.out.println("■■〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("총" + cnt + "페이지 수집했습니다.");
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("cnt", cnt);
		map.put("total", total);

		return map;

	}

}
