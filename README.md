# noticeboard

[스프링 부트와 AWS로 혼자 구현하는 웹 서비스](https://search.shopping.naver.com/book/catalog/32436253723?cat_id=50010881&frm=PBOKPRO&query=%EC%8A%A4%ED%94%84%EB%A7%81+%EB%B6%80%ED%8A%B8%EC%99%80+AWS%EB%A1%9C+%ED%98%BC%EC%9E%90+%EA%B5%AC%ED%98%84%ED%95%98%EB%8A%94+%EC%9B%B9+%EC%84%9C%EB%B9%84%EC%8A%A4&NaPm=ct%3Dl8i36ucw%7Cci%3D3cc5b084564919fd49e8c19271c6a2eb0913bde7%7Ctr%3Dboknx%7Csn%3D95694%7Chk%3D4ecf6ab297f11212c60be174a939fdf4173b316e)를 통해 학습한 내용을 정리한 문서입니다.

### 01장 인텔리제이로 스프링 부트 시작하기

- 인텔리제이 설정
- build.gradle 추가
- github 등록

Spring Initializr로 프로젝트 생성

---

### 02장 스프링 부트에서 테스트 코드를 작성하자

견고한 서비스를 만들고 싶다면 테스트 코드를 작성하자.

**`TDD`**: 테스트가 주도하는 개발, 테스트 코드를 먼적 작성

`**단위 테스트**`: 기능 단위의 테스트 코드를 작성, 순수하게 테스트 코드만 작성

<aside>
💡 패키지명을 숫자로 시작하거나 특수문자가 들어가는 등 규약을 어기면 자바 클래스나 패키지 등을 생성하지 못한다. 이것은 패키지를 만들었을때 **패키지 모양**으로 판단할 수 있다.

![image](https://user-images.githubusercontent.com/106286686/192176724-192a91b8-7b88-494b-928b-a568a36ddf4c.png)

</aside>

`@SpringBootApplication` 로 인해 스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성이 자동으로 설정됨. 그래서 이 위치는 항상 프로젝트의 최상단에 위치 해야 한다. 또 이로 인해 내장 WAS(웹 애플리케이션 서버)를 실행한다.

내장 WAS의 장점

언제 어디서나 **같은 환경**에서 스프링 부트를 배포 가능

롬복 추가

---

### 03장 스프링 부트에서 JPA로 데이터베이스 다뤄보자

**JPA 소개**

관계형 데이터 베이스와 객체지향 프로그래밍의 **패러다임 불일치**

→ 이를 해결하기 위해 자바 표준 ORM(Object Relational Mapping) 등장  

**Spring Data JPA**

`JPA ← Hibername ← Spring Data JPA`  

장점

- 구현체 교체의 용이성
- 저장소 교체의 용이성  

**요구사항 분석**

- 게시판 기능 (게시글 조회, 게시글 등록, 게시글 수정, 게시글 삭제)
- 회원 기능 (구글/네이버 로그인, 로그인한 사용자 글 작성 권한, 본인 작성 글에 대한 권한 관리)  

**Posts** **도메인 생성**

💡어노테이션 순서는 주요 어노테이션을 클래스에 가깝게 둔다. 이유는 코틀린 등의 새 언어 전환으로 롬복이 더이상 필요 없을 경우 쉽게 삭제 가능.

![image](https://user-images.githubusercontent.com/106286686/192176758-d528439a-f6d2-43c7-bd83-8c50a5c750b7.png)  

@Builder

- 해당 클래스의 빌더 패턴 클래스를 생성
- 생성자 상단에 선언 시 생서자에 포함된 필드만 빌더에 포함

`Entity`에는 절대 `setter`를 만들지 않는다.

그렇다면 setter가 없이 어떻게 DB에 데이터를 삽입할까?

생성자 또는 Builder 패턴

이 책에서는 Builder 패턴을 사용한다. Builder 패턴의 장점으로는 어떤 필드에 어떤 값을 채워야 할지 명확하게 인지할 수 있다.  

**생성자**

```java
public Example(String a, String b){
	this.a = a;
	this.b = b;
}
```

**빌더 패턴**

```java
Example.builder()
		.a(a)
		.b(b)
		.build();
```

**PostsRepository 생성**

MyBatis에서는 Dao라고 불리는 DB Layer 접근자다.

JPA에선 Repository라고 부른다.

<aside>
📎 **의문점**

책에서는 Entity와 Repository가 같은 패키지에 존재해야 작동한다고 나온다. (p.95)

하지만 최근 JPA를 공부할때 서로 다른 패키지로 분리해서 설계했던 기억이 있다.

</aside>

**PostsRepository 테스트**

`@AfterEach` 단위 테스트 끝날 때마다 수행된느 메소드를 지정

책에서는 `@After` 사용. 과거 버전으로 추정

**쿼리문 확인하기**

`[a](http://application.properties)pplication.properties` 에 `spring.jpa.show_sql=true` 입력

**등록/수정/조회 API 만들기**

- Rquest 데이터를 받을 Dto
- API 요청을 받을 Controller
- 트랜잭션, 도메인 기능 간의 순서를 보장하는 Service

**Bean 주입 방식**

- @Autowired
- setter
- 생성자

**추천 방식은 생성자 주입**

Entity 클래스는 데이터베이스와 맞닿은 핵심 클래스이다. 절대로 Request/Response 클래스로 사용하면 안된다.

**JPA Auditing으로 생성시간/수정시간 자동화하기**

BaseTimeEntity 클래스를 상위 클래스로 만들어 모든 Entity들의 생성 시간, 수정 시간을 자동으로 관리하는 역할을 한다.

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
```

메인 메서드가 있는 클래스에 `@EnableJpaAuditing` 를 잊지말자.

---

### 04장 머스테치로 화면 구성하기

**서버 템플릿 엔진**: `JSP`, `Freemarker`

**클라이언트 템플릿 엔진**: `리액트`, `뷰`

**부트스트랩**

외부 CDN 사용, 직접 라이브러리 다운

위처럼 2가지 방식으로 사용가능.

실제 서비스에서는 외부 CDN을 사용하지 말자. CDN 서비스에 문제가 생기면 프로젝트도 같이 문제가 생길 수 있다.

**페이지 로딩속도를 높이기 위해 css는 header에, js는 footer에 두었다.**

**{{> }}: 현재 머스테치 파일을 기준으로 다른 파일 가져오기**

`{{>layout/header}}`

`{{>layout/footer}}`

**자바스크립트로 버튼 기능 구현**

추후에 공부가 필요해 보인다.

**머스테치 문법**

{{#posts}}

- posts라는 List를 순횐한다.
- Java의 for문과 동일

{{id}} 등의 {{변수명}}

- List에서 뽑아낸 객체의 필드를 사용

{{post.id}}

- 머스테치는 객체의 필드 접근 시 점(Dot)으로 구분
- 즉, Post 클래스의 id에 대한 접근은 post.id로 사용할 수 있다.

readonly

- Input 태그에 읽기 가능만 허용하는 속성
- id와 author는 수정할 수 없도록 읽기만 허용

> 규모가 있는 프로젝트에서의 데이터 조회는 FK의 조인, 복잡한 조건 등으로 인해 이런 Entity 클래스만으로 처리하기 어려워 조회용 프레임워크를 추가로 사용. Querydsl, jooq, MyBatis 등
> 

**람다식**

```java
@Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
```

.map(PostsListResponseDto::new)와

.map(posts → new PostsListResponseDto(posts))는 같은 코드다.

---

### 05장 스프링 시큐리티와 OAuth 2.0으로 로그인 기능 구현하기

**스프링 시큐리티란?**

막강한 인증과 인가(혹은 권한 부여) 기능을 가진 프레임워크

실제로 대부분의 서비스에서는 id/password 방식의 로그인보다 소셜 로그인(구글, 네이버, 카카오 등) 기능을 사용한다. **배보다 배꼽이 커지는 경우가 많기 때문(회원과 관련된 모든 기능들을 구현해야 한다.)**

스프링 시큐리티에서는 권한 코드에 항상 **ROLE_**이 앞에 있어야만 한다.

```java
@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;

}
```

**SecuriyConfig클래스 코드 설명**

**@EnableWebSecurity**

Spring Security 설정들을 활성화 시켜줌

**csrf().disable().headers().frameOptions().disable()**

h2-console 화면을 사용하기 위해 해당 옵션들을 disable 함

**authorizeRequests**

URL별 권한 권리를 설정하는 옵션의 시작점

authorizeRequests가 선언되어야만 antMatchers 옵션을 사용 가능

**antMatchers**

권한 관리 대상을 지정하는 옵션

URL, HTTP 메소드별로 관리가 가능

“/” 등 지정된 URL들은 permitAll() 옵션을 통해 전체 열람 권한을 줌

“/api/v1/**” 주소를 가진 API는 USER 권한을 가진 사람만 가능하도록 함.

**anyRequest**

설정된 값들 이외 나머지 URL들을 나타냄

여기서는 authenticated()을 추가항 나머지 URL들은 모두 인증된 사용자들에게만 허용

인증된 사용자 즉, 로그인한 사용자들을 이야기<br/>

**logout().logoutSuccessUrl(”/”)**

로그아웃 기능에 대한 여러 설정의 진입점

로그아웃 성공 시 / 주소로 이동한다

**oauth2Login**

OAuth 2 로그인 기능에 대한 여러 설정의 진입점

**userInfoEndpoint**

OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당

**userService**

소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록

리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시 가능

**CustomOAuth2UserService클래스 코드 설명**

**registrationId**

현재 로그인 진행 중인 서비스를 구분하는 코드

**userNameAttributeName**

OAuth 로그인 진행 시 키가 되는 필드값. PK와 같은 의미

**OAuthAttributes**

OAuth2UserService를 통해 가져온 OAuth2user의 attribute를 담은 클래스

**SessionUser**

세션에 사용자 정보를 저장하기 위한 Dto

**OAuthAttributes클래스 코드 설명**

**of()**

OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에, 값 하나 하나를 변환해야만 한다<br/>

**어노테이션 기반으로 개선하기**

**LoginUserArgumentResolver클래스 코드 설명**

**supportsParameter()**

컨트롤러 메서드의 특정 파라미터를 지원하는지 판단<br/>

**resolveArgument()**

파라미터에 전달할 객체를 생성

여기서는 세션에서 객체를 가져옴<br/>

**세션 저장소로 데이터베이스 사용하기**

현재는 세션이 내장 톰캣의 메모리에 저장되어 애플리케이션을 재실행하면 로그인이 풀린다.

세션 저장소로 데이터베이스를 선택해보자.

build.gradle에

`implementation 'org.springframework.session:spring-session-jdbc'`

application.properties에

`spring.session.store-type=jdbc`

---

### **06장 AWS 서버 환경을 만들어보자 - AWS EC2**

24시간 작동하는 서버에는 3가지 선택지가 있다.

- 집에 PC를 24시간 구동시킨다.
- 호스팅 서비스(Cafe 24, 코리아호스팅 등)을 이용한다.
- 클라우드 서비스(AWS, AZURE, GCP 등)을 이용한다.

**EC2**

EC2는 AWS에서 제공하는 성능, 용량 등을 유동적으로 사용할 수 있는 서버

**인스턴스**

EC2 서비스에 생성된 가상머신

**크레딧**

일종의 CPU를 사용할 수 있는 포인트 개념

인스턴스 크기에 따라 정해진 비율로 CPU 크레딧을 계속 받게 되며, 사용하지 않을 때는 크레딧을 축적, 사용할 때 이 크레딧 사용

모두 사용하면 EC2 사용 못함

인스턴스도 하나의 서버이기 때문에 IP가 존재한다. 인스턴스 생성 시에 항상 새 IP를 할당하는데, 인스턴스를 중지하고 다시 시작하면 새 IP가 할당된다. 이는 번거로우니 IP가 매번 변경되지 않게 IP를 고정하자.

**탄력적 IP 생성후 EC2에 연결**

**주의할점**

탄력적 IP는 생성하고 EC2 서버에 연결하지 않으면 비용이 발생.

즉, 생성한 탄력적 IP는 무조건 EC2에 바로 연결

더는 사용할 인스턴스가 없을 때도 탄력적 IP를 삭제 해야함.

**EC2서버에 접속하기**

윈도우에서는 ssh에 접속하기 불편하기 때문에 PuTTY를 다운받음

책에서는 EC2에 자바8을 설치하지만 난 자바11을 사용했다. 설치시 유의하자. (sudo su 명령어로 root 권한을 얻어야 자바를 설치할 수 있다.)

책에서 EC2는 리눅스1을 기반이지만 리눅스2로 변경됨에 따라 약간의 차이가 있다.

- Host 이름 변경

---

### 07장 AWS에 데이터베이스 환경을 만들어보자 - AWS RDS

**RDS**

AWS에서 지원하는 클라우드 기반 관계형 데이터 베이스

보안그룹 설정에서 막혀서 조금 오래 걸림.

---

### 08장 EC2 서버에 프로젝트를 배포해 보자

gradle실행시 gradle 권한을 얻어야 실행할 수 있다.

`chmod +x gradlew`
