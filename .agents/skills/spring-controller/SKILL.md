---
name: spring-controller
description: Java Spring Boot REST Controller를 생성하거나 수정할 때 MemberController를 일반화한 생성자 주입, HTTP 요청 바인딩, Request DTO와 Entity 변환, Response DTO, CRUD와 페이징 응답 규칙을 적용한다. 모든 도메인에 사용하며 HTML 뷰를 반환하는 MVC Controller에는 REST 응답 구조를 강제하지 않는다.
---

# REST Controller 작성과 DTO·Entity 분리

`src/main/java/com/mycompany/demo/database/controller/MemberController.java` 전체를 참고한 공통 규칙이다. 회원 전용 경로, 필드, 메소드명과 페이지 크기는 각 도메인에 맞춘다.

## 클래스와 HTTP 경계

- 컨트롤러 패키지에 `도메인명Controller`를 만들고 JSON API에는 `@RestController`를 사용한다. 클래스의 `@RequestMapping`과 메소드별 매핑으로 경로를 구성한다. 원본의 `/database`는 이 프로젝트의 기존 경로이며 모든 프로젝트에 강제하지 않는다.
- 필요한 Service만 `private final` 필드로 선언하고 생성자 주입한다. 기존 Lombok 생성자 방식을 사용해도 된다. DAO를 직접 주입하지 않는다.
- 단건·목록 조회는 GET, 등록은 POST, 수정은 기존 API 계약에 맞는 PUT/PATCH, 삭제는 DELETE로 매핑한다. 기존 경로를 유지하면서 다른 컨트롤러와 HTTP 메소드·경로 조합이 중복되지 않게 한다.
- 컨트롤러는 요청 바인딩과 검증, DTO 변환, Service 호출, HTTP 응답 구성을 담당한다. SQL, 여러 저장 작업의 트랜잭션과 핵심 업무 규칙은 Service/DAO에 둔다.
- `@Slf4j`는 프로젝트 관례와 로그 필요에 따라 사용한다. 요청·Entity 전체나 비밀번호를 로그에 출력하지 않는다.

## 적용 규칙

- 컨트롤러가 받는 요청 데이터는 이름이 `Request`로 끝나는 DTO로 정의한다. Entity를 HTTP 요청에 직접 바인딩하지 않는다.
- 컨트롤러가 반환하는 응답 데이터는 이름이 `Response`로 끝나는 DTO로 정의한다. Entity를 그대로 반환하지 않는다.
- 컨트롤러는 요청 DTO를 해당 도메인의 Entity로 변환한 뒤 서비스에 전달한다. 서비스에 요청 DTO를 전달하지 않는다.
- 서비스에서 받은 Entity는 컨트롤러에서 응답 DTO로 변환한다. 기존 매퍼가 있으면 이를 재사용하되 HTTP 경계에서 변환하는 책임을 유지한다.
- Member, Board 등 특정 도메인에 한정하지 않고 같은 규칙을 적용한다. DTO 이름은 `CreateBoardRequest`, `BoardResponse`처럼 작업과 도메인을 드러내도록 정한다.

## 작업 방법

1. 대상 컨트롤러, DTO, Entity, 서비스 선언 및 호출부를 확인한다. 기존 패키지, 생성자, Lombok, 매핑 방식을 따른다.
2. 요청에 필요한 필드만 `*Request`에 정의한다. JSON 본문은 `@RequestBody`, 쿼리/폼은 `@ModelAttribute` 등 기존 요청 형식에 맞게 바인딩한다. 경로 변수는 기존 라우팅을 유지하면서 요청 DTO에 담은 후 Entity로 변환한다. 검증이 있다면 요청 DTO와 적절한 `@Valid` 사용으로 유지한다.
3. 요청 DTO에서 필요한 필드만 Entity로 복사한다. 단일 ID 조회·삭제도 요청 DTO에서 ID를 Entity에 담아 서비스에 전달한다. 이를 위해 필요한 서비스 시그니처, 구현 및 호출부를 함께 조정한다. DAO/Mapper의 단일 ID 인자는 서비스에서 Entity의 ID를 꺼내 전달할 수 있다.
4. 응답에 공개할 필드만 `*Response`로 옮긴다. 비밀번호 같은 민감 필드를 Entity에 있다는 이유로 응답에 포함하지 않는다. 목록은 `List<도메인Response>`, 페이지 응답은 목록과 페이징 정보를 가진 `*Response`를 사용한다. `ResponseEntity<도메인Response>` 같은 HTTP 래퍼를 사용할 수 있다. 본문 없는 응답에는 불필요한 DTO를 만들지 않는다.
5. 기존 URL, HTTP 메서드, 상태 코드, JSON 필드, 조회·저장 동작을 유지하고, 수정한 시그니처의 호출부와 프로젝트 빌드 및 관련 기존 테스트를 확인한다.

전체 적용을 요청받으면 모든 대상 컨트롤러를 조사한다. 특정 컨트롤러나 메서드만 요청받으면 그 범위와 컴파일에 필요한 관련 코드만 수정한다. 스킬 생성 요청만으로 애플리케이션 전체를 리팩터링하지 않는다.

## 참고 코드와 일반화

원본 참고: `C:/gen-ai-course/projects/full-stack/back-end/src/main/java/com/mycompany/demo/database/controller/MemberController.java`의 `getMember()`.

이 메서드는 서비스에서 받은 `Member`의 `memberId`, `memberName`, `email`, `joinedAt`을 `MemberResponse` 생성자로 전달한다. 이처럼 응답 필드를 명시적으로 선택하는 방식을 다른 도메인에도 적용한다. 원본의 문자열 요청 인자와 문자열 서비스 인자는 요청 DTO 및 Entity 전달 규칙의 예외로 복사하지 않는다. 참고 파일이 없는 프로젝트에서도 위 규칙을 독립적으로 적용한다.

개념적인 조회 흐름(타입명과 바인딩 방식은 프로젝트에 맞춘다):

```java
@GetMapping("/get-member")
public MemberResponse getMember(@ModelAttribute GetMemberRequest request) {
    Member criteria = new Member();
    criteria.setMemberId(request.getMemberId());
    Member member = memberService.getMember(criteria);
    return new MemberResponse(
        member.getMemberId(), member.getMemberName(),
        member.getEmail(), member.getJoinedAt()
    );
}
```

예제에 없는 null 처리, 오류 응답, 검증 및 인증은 해당 프로젝트의 기존 정책을 유지한다.

## CRUD 응답 흐름

| 작업 | 원본에서 추출한 흐름 | 일반화할 때 확인할 점 |
| --- | --- | --- |
| 단건 조회 | 요청 키 → Service 조회 → Response 변환 | DTO의 키를 Entity에 담아 전달하고 미조회 정책 확인 |
| 등록 | Request → Entity → Service 등록 → 저장 결과 Response | DB 기본값이나 생성 키가 응답에 필요할 때만 재조회 |
| 수정 | Request → Entity → Service 수정 → 영향 행 수 확인 → 결과 응답 | 단건 수정은 원본처럼 1행을 성공으로 판단하되 실제 업무 계약 우선 |
| 삭제 | 요청 키 → Service 삭제 → 영향 행 수 확인 → 결과 응답 | DTO/Entity 경계를 유지하고 0행 처리 정책 보존 |
| 목록 | 검색 조건 → 전체 건수 → Pager → 목록 조회 → Response 목록 | 건수와 목록의 조건을 일치시키고 페이지 메타데이터 반환 |

- 원본의 수정·삭제 응답은 `status: success/fail`, 수정 성공 시 `data`를 포함하는 Map이다. 새 코드에서는 같은 JSON 필드를 필요한 `*Response`로 표현한다. 기존 응답을 변경할 때는 필드명, 값과 상태 코드를 유지한다. 공통 응답 래퍼가 있다면 재사용한다.
- 원본처럼 등록·수정 후 재조회할 때는 Service 계약에 맞는 키 Entity를 전달한다. 생성 키는 Service/DAO의 실제 키 반환 방식을 확인하고 입력 키가 항상 존재한다고 가정하지 않는다. Service가 필요한 저장 결과를 이미 반환하면 재조회를 반복하지 않는다.
- 수정 DTO에서 누락된 필드를 무조건 null로 덮어쓰지 않는다. 전체 수정인지 부분 수정인지, Service/Mapper가 어떤 필드를 갱신하는지 확인한다. HTTP PUT/PATCH 변경을 임의로 수행하지 않는다.
- 단건 조회의 null을 바로 역참조하지 않는다. 프로젝트의 예외 처리기와 미조회 응답 정책을 따른다. DB 예외를 무조건 `fail`로 감추지 않는다.

## 검색과 페이징

1. `List도메인Request` 등 요청 DTO에 페이지 번호와 허용 검색 필드를 정의한다. 원본의 `pageNo` 기본값은 1이다. `@ModelAttribute` DTO로 옮겨도 기존 기본값을 보존하며, 명시된 검증 정책에 따라 1 미만의 페이지 번호 등을 처리한다.
2. DTO의 검색 필드를 허용된 키만 가진 `Map<String, Object>`로 옮기거나 기존 검색 객체를 사용한다. 이는 검색 전용 계약이며 HTTP 요청 DTO를 Service에 직접 전달하지 않는다. 단건·저장 요청은 앞의 Entity 변환 규칙을 따른다.
3. 동일한 조건으로 `pagingTotalRows(where)`를 호출하고 프로젝트의 `Pager`를 만든 뒤 `list도메인(pager, where)`를 호출한다. 원본의 `new Pager(10, 5, totalRows, pageNo)`에서 10과 5는 현재 설정 예시다. 기존 설정과 API 요구사항을 따른다.
4. 반환된 Entity 각각을 공개 필드만 포함한 Response DTO로 변환한다. `pager`, `data` 필드를 가진 페이지 Response DTO로 묶는다. Entity 목록을 그대로 노출하지 않는다.

검색 조건이 없으면 기존 Service/DAO 계약에 따라 null 또는 빈 조건을 사용한다. 페이징 SQL과 정렬은 DAO/Mapper에 맡긴다.

## 관련 계층과 완료 확인

Service 시그니처도 변경하면 [spring-service](../spring-service/SKILL.md)를 확인한다. DTO 필드, Entity 접근자, Service 인자·반환 타입과 변경한 호출부가 일치하는지 점검한다. 기존 URL, HTTP 메소드, JSON 구조와 검증·오류 정책이 보존되었는지 확인한다.

코드를 변경했다면 프로젝트 빌드와 관련 기존 테스트를 실행한다. 새 HTTP 동작이 추가되면 성공·미조회·검증 실패 및 페이지 응답처럼 실제 변경된 동작을 확인한다. 문서만 변경한 경우 애플리케이션 빌드를 요구하지 않는다. 실행하지 못한 검증은 결과에 명시한다.
