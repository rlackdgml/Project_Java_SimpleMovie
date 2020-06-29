package common;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import daum.BoxOfficeDaum;
import daum.ReplyCrawlerDaum;
import naver.BoxOfficeNaver;
import naver.ReplyCrawlerNaver;
import persistence.ReplyDAO;

public class SimpleMovieMain {

	public static void main(String[] args) throws Exception {

		BoxOfficeParser bParser = new BoxOfficeParser();
		BoxOfficeNaver bon = new BoxOfficeNaver();
		BoxOfficeDaum don = new BoxOfficeDaum();
		ReplyCrawlerNaver nCrawler = new ReplyCrawlerNaver();
		ReplyCrawlerDaum dCrawler = new ReplyCrawlerDaum();
		ReplyDAO rDao = new ReplyDAO();

		// 1. 박스오피스 정보 + 네이버 영화 정보 + 다음 영화 정보(1~18위)

		// 순위, 영화제목, 예매율, 장르, 상영시간. 개봉일자 ,감독
		// 출연진, 누적관객수, 누적매출액, 네이버코드, 다음코드 총 12개
		String[][] mvRank = new String[10][12];

		// 1. 박스오피스 정보 + 네이버 영화 정보 + 다음 영화 정보(1~18위)

		// 1-1. BoxOffcie Parsing
		mvRank = bParser.getParser();

		// 1-2. NaverBoxOffice Crawling:)
		mvRank = bon.naverMovieRank(mvRank);

		// 1-3. DaumBoxOffice Crawling:)
		mvRank = don.daumMovieRank(mvRank);
		// 2.view단 실행
		// 2-2 user 사용자가 입력한 영화번호 순위()
		int userVal = userInterface(mvRank);

		// 3. 사용자가 선택한 여화의 네이버 다음 댓글 정보를 수집 및 분석
		// 3-1. MongoDB 데이터 삭제
		// 수집하는 댓글의 영화가 MongoDB에 저장되어 있는 영화라면
		// 해당 영화 댓글 우선 삭제 후 새로운 댓글 저장
		rDao.deleteReply(mvRank[userVal-1][1]);
		
		// 3-2. NAVER 댓글 수집 + mongoDB 저장
		HashMap<String, Integer> nMap = nCrawler.naverCrawler(mvRank[userVal-1][1], mvRank[userVal-1][10]);
		// 3-2. DAUM 댓글 수집 + mongoDB 저장
		HashMap<String, Integer> dMap = dCrawler.daumCrawler(mvRank[userVal-1][1], mvRank[userVal-1][11]);
		System.out.println(mvRank[userVal - 1][11]);

		// 4. 사용자가 결과 출력

		double nTotal = nMap.get("total"); // round 함수는 반올림을 뜻함
		double avgNaver = nTotal/nMap.get("cnt");
		double dTotal = dMap.get("total"); // round 함수는 반올림을 뜻함
		double avgDaum = dTotal/dMap.get("cnt");
		DecimalFormat dropDot = new DecimalFormat(".#");
		DecimalFormat threeDot = new DecimalFormat("###,###");
		BigInteger money = new BigInteger(mvRank[userVal - 1][9]); // String 형만 가능 숫자형을 문자열로 바꿈""
		BigInteger view = new BigInteger(mvRank[userVal-1][8]);
		
		System.out.println(
				"■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ Description of \"" + mvRank[userVal - 1][1] + "\"");
		System.out.println("■■〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("■■ 장르: " + mvRank[userVal - 1][3] + ", 예매율: " + mvRank[userVal - 1][2]);
		System.out.println("■■ 상영시간:" + mvRank[userVal - 1][4] + ", 개봉일자:" + mvRank[userVal - 1][5]);
		System.out.println("■■ 감독:" + mvRank[userVal - 1][6]);
		System.out.println("■■ 출연진:" + mvRank[userVal - 1][7]);
		System.out.println("■■ 누적 → [관객수:" + threeDot.format(Integer.parseInt(mvRank[userVal - 1][8])) + "명] [매출액:"
				+ threeDot.format(money) + "원]");
		System.out.println("■■ 네이버 → [댓글수:" + nMap.get("cnt") + "건] [평균: " + dropDot.format(avgNaver) + "점]");
		System.out.println("■■ 다음 → [댓글수:" + dMap.get("cnt") + "건] [평균: " + dropDot.format(avgDaum) + "점]");
		System.out.println(
				"■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");

	}

	// VIEW: 프로그램 시작 인터페이스 + 사용자 값 입력
	public static int userInterface(String[][] mvRank) {
		Scanner sc = new Scanner(System.in); // 사용자한테 값입력받을때 말그대로 스캔한다
		int userVal = 0;
		// 2.View 단
		// 2-1. 유저에게 BoxOffice 예매율 1~10위까지의 정보 제공

		// 현재 날짜 계산하기
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH:mm:ss");
		String today = sdf.format(cal.getTime());
		Date date = new Date();
		SimpleDateFormat engSdf = new SimpleDateFormat("MM월dd일", Locale.KOREAN);
		String engDay = engSdf.format(cal.getTime());
		System.out.println(
				"■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ SimplMovie ver1.2");
		System.out.println(
				"■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ >> Developer : HeonJun Kim(redwhale)");
		System.out.println(
				"■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■ >> TODAY: " + today);
		System.out.println(
				"■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("■■BoxOffice Rank(" + (cal.get(Calendar.MONTH) + 1) + "월" + cal.get(Calendar.DATE) + "일)");
		
		String noneCode = "";
		for (int i = 0; i < mvRank.length; i++) {
			if (mvRank[i][10] == null) {
				
				noneCode = "(정보없음)";
				break;
			}
			System.out.println("■■ >> " + mvRank[i][0] + "위" + mvRank[i][1] + noneCode);
		}

		// 2-2. 사용자가 입력하는 부분
		while (true) {
			System.out.println(
					"■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
			System.out.println("■■ >> 보고싶은 영화 번호(순위)를 입력하세요");
			System.out.print("■■ >> 번호: ");
			userVal = sc.nextInt();

			if (userVal < 0 || userVal > 10) {
				System.out.println("■■■ >> [Warning] 1~10사이의 숫자를 입력하세요 :(");
				// 잘못된값
				continue;

			} else if (mvRank[userVal - 1][10] == null) {
				// 사용자가 입력한 번호의 영화가 정보가 있는지 없는지 체크
				System.out.println("■■ >>[Warning] 해당 영화는 상영정보가 없습니다. 다른영화를 선택해 주세요~");
				continue;
			} else {
				// 통과 : 사용자의 값이 0~10
				sc.close();
				break;
			}

		}

		// 유효성 체크
		// >> 1~10까지의 값(정상)
		// 1. 1~10이외의 숫자를 넣엇을때
		// 2. 정보없는 영화 선택햇을때
		System.out.println(
				"■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		return userVal;

		// id
		// 1) NULL값 체크
		// 2) 길이 체크
		// 3) 공백값 체크
		// 4) 정규식 체크(대소문자,숫자 8~15자 특수문자 사용 혹은 미사용)
		// 5) 길이 체크(id의 길이 체크)

	}

	// mvRank 출력하는 코드
	public static void printArr(String[][] mvRank) {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		for (int i = 0; i < mvRank.length; i++) {
			System.out.print(mvRank[i][0] + "\t");
			System.out.print(mvRank[i][1] + "\t");
			System.out.print(mvRank[i][2] + "\t");
			System.out.print(mvRank[i][3] + "\t");
			System.out.print(mvRank[i][4] + "\t");
			System.out.print(mvRank[i][5] + "\t");
			System.out.print(mvRank[i][6] + "\t");
			System.out.print(mvRank[i][7] + "\t");
			System.out.print(mvRank[i][8] + "\t");
			System.out.print(mvRank[i][9] + "\t");
			System.out.print(mvRank[i][10] + "\t");
			System.out.println(mvRank[i][11]);

		}
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");

	}
}