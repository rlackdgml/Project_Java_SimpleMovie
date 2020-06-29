# :movie_camera:Project_Java_simplemovie

JAVA기반의 한국 영화 박스오피스 1~10위까지의 정보를 파싱 및 크롤링하고 MongoDB에 저장 후 
사용자에게 정보를 출력 해주는 콘솔프로그래밍


## :heavy_check_mark:Developer Enviroment

- Language[:coffe:JAVA1.8]()
- IDE Tool [:computer:Eclipse]()
- Package Manager : [:snake:MavenRepository]()
- Using Package : [jSoup. json-simple . mongo - java -driver]()
- Version Tools : [Github, Sourcetree]()
- Parsing URL : [한국영화진흥위원회](https://www.kofic.or.kr/kofic/business/main/main.do)
- Crawling URL:
+ [NAVER MOVIE] (https://movie.naver.com/)
+ [DAUM MOVIE] (https://movie.daum.net/main/new#slide-1-0)

## :floppy_disk:Repository struture descripton
#### 1. src/ common
-[simpleMovieMain](): 프로그램 시작하는곳 + 콘솔 프로그래밍 view단
-[BoxOfficeParser](): 한국영화 진흥위원회에서 일별 박스오피스 정보 수집(링크, 영화제목, 누적 관객수, 누적 매출액)

### 2.src/ naver
[BoxOfficeNaver](https://movie.naver.com/movie/running/current.nhn): Naver에서 BoxOffcie 1~10위 까지 영화 코드(네이버 고유 영화코드) 수집
[ReplyCrawlerNaver](): Naver에서 해당 영화의 댓글 , 평점, 작성자, 작성일자 수집해서 MongoDB에 저장


### 3.src/daum
[BoxOfficeDaum](http://ticket2.movie.daum.net/Movie/MovieRankList.aspx): Daum에서 BoxOffcie 1~10위 까지 영화 코드(다음 고유 영화코드) 수집
[ReplyCrawlerDaum](): daum에서 해당 영화의 댓글 , 평점, 작성자, 작성일자 수집해서 MongoDB에 저장

### 4.src/presitance
-ReplyDAO: 네이버, 다음에서 수집한 영화 댓글 저장 또는 삭제할때 사용하는 DAO
### 5. src/domain
-ReplyDTO: 네이버, 다음에서 영화 댓글 수집후 MongoDB에 저장할때 사용하는 DTO
### 6.pom.xml
-[pom.xml](): naver에서 build할 library 설정하는 장소

### :speech_ballon:How to use?
1.BoxOfficeParser에서 발급받은 key를 교체한다.
2. ReplyDAO에서 MongoDB를 세팅한다.(connect, DB, collection 등)
3. 메인 프로그램을 실행한다.
4. 1~10위 중 원하는 영화를 선택한다. -1~10의 숫자를 입력
5. Run the Program!

