---
name: spring-service
description: Java Spring Boot에서 DAO를 사용하는 Service를 생성하거나 수정할 때 생성자 주입, Entity 기반 입출력, CRUD와 페이징 위임 및 업무 로직 경계를 적용한다. MemberService의 구조를 모든 도메인에 일반화하며 DAO나 컨트롤러만 작성하는 작업에는 적용하지 않는다.
---

# Entity와 DAO 기반 Service

참고 원본은 `src/main/java/com/mycompany/demo/database/service/MemberService.java`이다. 회원 전용 이름과 기본키 타입을 복제하지 말고 다음 구조를 대상 도메인에 적용한다. 원본 파일 없이도 아래 규칙으로 작업할 수 있다.

## 작성 전 확인

- 대상 Entity, DAO 시그니처, 호출 컨트롤러와 기존 서비스, 관련 Mapper를 확인한다. 패키지와 기본키 타입은 실제 프로젝트를 따른다.
- 컨트롤러와 서비스 사이의 계약은 이 프로젝트의 [spring-controller](../spring-controller/SKILL.md) 규칙과 일치시킨다. DAO도 변경할 때는 [mybatis-dao](../mybatis-dao/SKILL.md)를 확인한다.
- 요청된 서비스와 컴파일에 필요한 관련 코드만 수정한다. 스킬 작성이나 일부 서비스 작업을 이유로 전체 서비스를 리팩터링하지 않는다.

## 클래스와 의존성

- 서비스 패키지에 `도메인명Service` 구체 클래스를 작성하고 `@Service`를 선언한다. 별도의 서비스 인터페이스나 구현 클래스를 일괄 생성하지 않는다.
- 필요한 DAO를 `private final` 필드로 선언하고 생성자로 주입한다. 원본처럼 명시적 생성자를 기본으로 하되 기존 클래스가 `@RequiredArgsConstructor`를 사용하면 유지한다. 두 방식을 중복 적용하지 않는다.
- 필요한 DAO만 주입한다. 여러 도메인의 처리가 필요한 업무는 여러 DAO를 사용할 수 있다.
- 원본에는 `@Slf4j`가 있으나 로그 사용 여부는 기존 관례와 필요에 따른다. Entity 전체, 비밀번호 등 민감 정보를 로그에 남기지 않는다.

## 계층과 입출력

- Service는 DAO를 호출하고 업무 규칙과 여러 저장 작업의 흐름을 조정한다. SQL은 DAO/Mapper에 둔다.
- 컨트롤러의 Request DTO는 Entity로 변환된 후 Service에 전달한다. Service는 Entity, Entity 목록, 영향 행 수 등 업무 결과를 반환한다. HTTP 상태 코드, `ResponseEntity`, 응답용 Response DTO, `status`/`data` HTTP 응답 Map 구성은 컨트롤러에 둔다.
- 새 서비스의 단일 조회·삭제도 Entity를 인자로 받고 내부에서 기본키를 꺼내 DAO에 전달한다. 원본의 `getMember(String memberId)`와 `deleteMember(String memberId)`는 분석 대상의 현 상태이며, 프로젝트의 Entity 전달 규칙에 대한 예외로 일반화하지 않는다.
- 기존 메소드 수정 시 호출부와 함께 변경한다. 관련 없는 기존 시그니처는 일괄 변경하지 않는다. 다른 서비스 사이의 내부 호출은 실제 업무 계약을 확인한다.
- `Pager`와 검색용 `Map<String, Object>`는 원본의 목록 조회 계약으로 허용한다. HTTP 요청 DTO와 구분하며 기존 검색 전용 객체가 있으면 그 계약을 따른다.

## CRUD와 페이징 계약

| 서비스 역할 | DAO 위임 | 기본 반환 |
| --- | --- | --- |
| `get도메인` | Entity에서 ID를 꺼내 `selectById(id)` | Entity |
| 등록 업무 | `insert(entity)` | 영향 행 수 `int` |
| `update도메인` | `update(entity)` | 영향 행 수 `int` |
| `delete도메인` | Entity에서 ID를 꺼내 `deleteById(id)` | 영향 행 수 `int` |
| `pagingTotalRows` | `selectCount(where)` | 전체 행 수 `int` |
| `list도메인` | `selectPage(pager, where)` | `List<Entity>` |

- `join`은 회원 가입이라는 업무 이름이다. 다른 도메인은 `createProduct`, `placeOrder`처럼 실제 업무에 맞는 이름을 사용한다. 원본의 메소드 이름을 모든 도메인에 강제하지 않는다.
- insert, update, delete 작업에 해당하는 서비스 메소드의 반환 타입은 모두 `int`로 선언하고 영향받은 행 수를 반환한다. 등록도 `void`, `boolean`, Entity 등으로 반환하지 않는다. 기존 메소드를 이 규칙에 맞게 변경할 때는 필요한 호출부 호환성을 확인한다.
- DAO의 `insert` 반환값은 영향 행 수이며 생성 ID가 아니다. 생성 키가 필요하면 프로젝트의 키 생성 방식에 따라 Entity에 반영하되 서비스 반환 타입은 `int`를 유지한다.
- 등록·수정·삭제의 영향 행 수를 임의로 성공으로 바꾸거나 버리지 않는다. 여러 DAO 쓰기를 조합하는 업무는 어떤 작업의 행 수 또는 합계를 반환할지 업무 계약에 명시한다. 미조회 null, 영향 행 수 0, 예외에 대한 기존 정책을 확인하고 의미를 보존한다. 근거 없이 예외를 삼키거나 빈 Entity를 반환하지 않는다.
- 건수와 목록은 동일한 검색 조건을 사용한다. 원본처럼 `Pager`를 인자로 받아 DAO에 전달하며 페이징 계산을 중복 구현하지 않는다. 페이지 응답 조합이 요청되지 않았다면 새 래퍼 객체를 만들지 않는다.
- 단순 DAO 위임도 허용한다. 중간 지역변수는 설명에 도움이 될 때 사용하며 불필요한 추상화나 업무 규칙을 만들어 넣지 않는다.

## 트랜잭션

원본에는 `@Transactional`이 없다. 이를 모든 서비스에 트랜잭션이 불필요하다는 규칙으로 해석하지 않는다. 여러 쓰기가 하나의 업무로 함께 성공·실패해야 하면 해당 public 업무 메소드에 프로젝트의 트랜잭션 정책을 적용한다. 예외와 롤백 조건, 같은 클래스 내부 호출 시 프록시 적용 여부를 확인한다. 단순 위임 메소드에 일괄로 어노테이션을 추가하지 않는다.

## 일반화 예시

아래 `Item`, `ItemDao`, `Pager`는 대상 프로젝트의 실제 타입으로 바꾸고 import와 패키지를 추가한다. 기본키 타입은 DAO가 정의한다.

```java
@Service
public class ItemService {
    private final ItemDao itemDao;

    public ItemService(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    public Item getItem(Item criteria) {
        return itemDao.selectById(criteria.getItemId());
    }

    public int createItem(Item item) {
        return itemDao.insert(item);
    }

    public int updateItem(Item item) {
        return itemDao.update(item);
    }

    public int deleteItem(Item criteria) {
        return itemDao.deleteById(criteria.getItemId());
    }

    public int pagingTotalRows(Map<String, Object> where) {
        return itemDao.selectCount(where);
    }

    public List<Item> listItem(Pager pager, Map<String, Object> where) {
        return itemDao.selectPage(pager, where);
    }
}
```

예시는 일반적인 CRUD와 페이징을 보여준다. 실제 요청에 필요한 메소드만 작성하고 검증, 인증, 예외 처리는 대상 업무와 기존 정책에 맞춘다.

## 완료 확인

Service 인자·반환 타입을 DAO 및 모든 변경된 호출부와 대조한다. 코드 변경 시 프로젝트의 빌드와 관련 기존 테스트를 실행하고, 새 업무 로직이나 여러 쓰기의 원자성이 추가되었다면 해당 동작을 검증한다. 단순 위임만 확인하려는 구현 복제 테스트는 추가하지 않는다. DB 연동 등 실행하지 못한 검증은 결과에서 구분한다.
