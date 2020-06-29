package domain;

public class ReplyDTO {
	// 1.변수

	private String movieNm; // 영화제목
	private String content; // 댓글내용
	private String writer; // 댓글 작성자
	private double score; // 댓글 평점
	private String regdate; // 댓글 작성일자
    // 2. 생성자
	public ReplyDTO() {
	} // 디폴트 생성자

	public ReplyDTO(String movieNm, String content, String writer, double score, String regdate) { // 메서드 오버로딩
		super();
		this.movieNm = movieNm;
		this.content = content;
		this.writer = writer;
		this.score = score;
		this.regdate = regdate;
	}

	// 3.getter. setter
	public String getMovieNm() {
		return movieNm;
	}

	public void setMovieNm(String movieNm) {
		this.movieNm = movieNm;
	}

	public String getcontent() {
		return content;
	}

	public void setcontent(String content) {
		this.content = content;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}
	// 4.to string()

	@Override
	public String toString() {
		return "ReplyDTO [movieNm=" + movieNm + ", content=" + content + ", writer=" + writer + ", score=" + score
				+ ", regdate=" + regdate + "]";
	}

}
