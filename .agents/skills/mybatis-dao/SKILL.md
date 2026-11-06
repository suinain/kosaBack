---
name: mybatis-dao
description: Java Spring Boot에서 MyBatis DAO를 생성하거나 수정할 때 Entity 기반 CRUD, Pager와 검색 조건을 이용한 페이징, Mapper XML 연동 규칙을 적용한다. MemberDao 패턴을 도메인별로 일반화하며 JPA Repository나 JdbcTemplate 구현에는 적용하지 않는다.
---

# MyBatis DAO 작성

`MemberDao`에서 추출한 인터페이스 중심 패턴을 모든 도메인의 MyBatis DAO 작성에 적용한다. 패키지와 Entity, 기본키 타입은 대상 프로젝트에 맞춘다.

## 작성 전 확인

- 대상 Entity의 필드, 기본키와 Java 타입, 테이블 구조를 확인한다. 회원 기본키가 String이라는 이유로 다른 도메인에도 String을 사용하지 않는다. 복합키는 실제 키 구조에 맞게 설계한다.
- 인접 DAO, 대응 Mapper XML, 호출 Service, MyBatis 설정을 확인한다. 기존 메소드의 변경은 호출부와 XML에 미치는 영향을 함께 확인한다.
- 프로젝트의 Pager를 재사용한다. 원본 프로젝트에서는 `database.dto.Pager`를 사용하며 `rowsPerPage`와 `startRowIndex`가 LIMIT/OFFSET에 대응한다. 다른 프로젝트에서는 실제 속성과 DB 방언을 확인한다.

## 인터페이스 규칙

- DAO 패키지에 도메인 이름을 붙인 `도메인명Dao` 인터페이스를 만들고 `@Mapper`를 선언한다. SQL 구현은 대응 Mapper XML에 둔다.
- 저장·수정 인자는 Entity를 사용한다. HTTP Request/Response DTO와 비즈니스 로직은 DAO에 두지 않는다. Pager는 페이징 전달 객체로 사용한다.
- 일반적인 CRUD와 페이징의 기본 계약은 다음과 같다. 요청에 필요한 메소드를 구현하고 기존 DAO를 수정할 때는 관련 없는 CRUD를 강제로 추가하지 않는다.

| 메소드 | 인자 | 반환 |
| --- | --- | --- |
| `insert` | 도메인 Entity | 영향받은 행 수 `int` |
| `selectById` | 실제 기본키 | 도메인 Entity, 미조회 시 기본적으로 null |
| `selectCount` | `@Param("where") Map<String, Object> where` | 검색 조건에 해당하는 전체 행 수 `int` |
| `selectPage` | `@Param("pager") Pager pager`, `@Param("where") Map<String, Object> where` | `List<도메인 Entity>` |
| `update` | 도메인 Entity | 영향받은 행 수 `int` |
| `deleteById` | 실제 기본키 | 영향받은 행 수 `int` |

- `insert`의 반환값은 생성된 ID가 아니다. 생성 키가 필요하면 프로젝트의 키 생성 방식과 MyBatis 매핑에 맞춰 Entity에 반영한다.
- 여러 인자는 `@Param`으로 이름을 명시하고 XML과 일치시킨다. 새로 작성하는 기본키 인자에도 `@Param("실제키이름")`을 명시해 바인딩을 분명히 한다. 원본의 `deleteById(String memberId)`는 어노테이션이 없지만 이를 필수 관례로 복제하지 않는다.
- Entity 하나를 직접 받는 `insert`와 `update`는 원본처럼 `@Param` 없이 전달하고 XML에서 `#{필드명}`으로 접근한다. 기존 방식 변경 시 XML의 접근 경로도 함께 조정한다.
- 원본의 `Map<String, Object>` 검색 조건 방식을 기본으로 사용하되 도메인별 허용 키, 값 타입, 일치/부분 검색 의미를 XML 또는 가까운 주석에 명시한다. 기존 프로젝트가 검색 전용 객체를 사용하면 그 계약을 유지한다.

## Mapper XML과의 계약

- Mapper `namespace`는 DAO의 전체 클래스명, SQL 문장 `id`는 인터페이스의 메소드명과 일치시킨다. 메소드 오버로딩으로 동일한 statement id가 충돌하지 않게 한다.
- Entity 필드와 컬럼의 매핑은 실제 `resultMap` 또는 프로젝트의 밑줄-카멜 매핑 설정을 확인한다. `resultType` 별칭을 사용하려면 등록 여부를 확인한다.
- `selectCount`와 `selectPage`는 동일한 검색 조건을 적용한다. 전체 건수에는 페이지 제한을 넣지 않는다. 같은 XML을 작성할 때는 필요에 따라 `<sql>`/`<include>`로 조건을 공유한다.
- 검색 조건 Map이 null이거나 선택 조건이 비어 있는 경우를 처리한다. 문자열의 빈 값 검사를 숫자·날짜 등 다른 타입에 무조건 적용하지 않는다.
- `selectPage`에는 재현 가능한 정렬을 지정한다. 정렬값이 중복될 수 있으면 기본키 등으로 순서를 확정한다. 페이징 SQL은 사용 DB와 Pager 속성에 맞춘다.
- 값은 `#{...}`로 바인딩한다. 검색 Map의 키나 값을 `${...}`로 SQL에 직접 삽입하지 않는다. 동적 컬럼/정렬이 필요하면 허용 목록에 따라 선택한다.
- 기본키 수정·삭제 SQL에는 정확한 키 조건을 포함한다. 복합키라면 구성 키 전체를 사용한다.
- DAO만 요청받았을 때 XML과 서비스가 이미 있으면 호환성을 확인하고 필요한 부분만 수정한다. XML이 없다면 실행 가능한 연동까지 요청된 범위인지 판단하고, 생성하지 않은 매핑은 결과에 명시한다.

## 일반화 예시

아래는 `Long` 기본키 `itemId`를 사용하는 도메인의 예시다. `Item`, `Pager`, 패키지 경로와 키 이름은 실제 프로젝트에 맞게 교체한다.

```java
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
// 프로젝트의 Item Entity와 Pager import를 추가한다.

@Mapper
public interface ItemDao {
    int insert(Item item);

    Item selectById(@Param("itemId") Long itemId);

    int selectCount(@Param("where") Map<String, Object> where);

    List<Item> selectPage(@Param("pager") Pager pager,
                         @Param("where") Map<String, Object> where);

    int update(Item item);

    int deleteById(@Param("itemId") Long itemId);
}
```

이 예시의 XML은 기본키를 `#{itemId}`, 검색 조건을 `#{where.조건키}`, 페이징을 `#{pager.실제속성}`으로 참조한다.

## 완료 확인

메소드 시그니처와 Service 호출, XML namespace/id, 파라미터 경로, Entity 매핑을 대조한다. 코드 변경 시 프로젝트의 사용 가능한 빌드 검사와 관련 테스트를 수행한다. DB 연동 검증이 가능하면 CRUD의 영향 행 수, 미조회 결과, 검색 건수와 페이지 결과의 일관성을 확인한다. 실행하지 못한 검증은 완료 보고에서 구분한다.
