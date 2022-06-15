### SpringBatch

 [스프링 배치 공식 문서]: (https://docs.spring.io/spring-batch/trunk/reference/html/index.html)

## 스프링 배치 Job의 구성

![spring-batch-reference-model](https://user-images.githubusercontent.com/19945223/173719948-f60ffed1-3d03-4641-938c-1db2e006d377.png)

## Job
Job = 배치 작업이다. 혹은 Flow라고도 부른다.  최소 하나의 Step을 가져야 하며 엄청나게 복잡한 Job이 아닌 이상 2-10개의 Step을 권장한다 (솔직히 10개도 너무 많다). 만약 Step 개수가 10개 이상이면 일반적인 코드 리팩토링처럼 여러 개의 Job으로 쪼개서 하나의 Job에 너무 많은 책임들(Responsibilities)이 몰리게 하지 말자. 프로그램을 만드는 것도 중요하지만 유지보수와 관리, 모니터링도 그만큼 중요하니까.


## Step
Step은 읽기 -> 가공하기 -> 쓰기의 묶음이다. 이 묶음을 Chunk processing이라고 부르는데 하나의 트랜잭션으로 이해하면 된다. 바로 이 Chunk processing이 위에 언급한 재시작의 핵심이다.

다음은 스프링 공식 문서에서 가져온 Chunk processing의 컨셉트를 보여주는 예제이다. 한꺼번에 다 읽어서 쓰는 게 아닌 commitInterval 만큼 읽고 쓰기 때문에 재시작이 가능한 것이다.

```java
List items = new Arraylist();

for(int i = 0; i < commitInterval; i++){

    Object item = itemReader.read()

    Object processedItem = itemProcessor.process(item);

    items.add(processedItem);

}

itemWriter.write(items);
```

더 나아가서 필요에 따라 여러 개의 ItemReader를 거쳐 여러 개의 ItemProcessor를 거쳐 여러 개의 ItemWriter를 사용하게 할 수도 있다.


## ItemReader
ItemReader는 말 그대로 데이터 읽기를 담당한다. 정확히는 인터페이스이며 T read() 메소드를 가지고 있다.

# ItemReader 정의
public interface ItemReader<T>

공식 페이지에서 T read() 메소드의 Java Doc을 보자.

Reads a piece of input data and advance to the next one. Implementations must return null at the end of the input data set. In a transactional setting, caller might get the same item twice from successive calls (or otherwise), if the first call was in a transaction that rolled back.

요약하면, 읽어드린 데이터를 반환한다. 만약 null을 리턴 할 경우 읽어야 하는 총 데이터의 마지막임을 나타낸다. DB로 치면 하나의 row를, 파일로 치면 한 line을, JSON Array로 치면 하나의 element를 반환한다고 생각하면 쉽다. ItemReader 구현 방식에 따라 훨씬 복잡한 구조의 데이터도 처리 할 수 있다.

read 메소드의 반환 값은 ItemProcessor의 Input으로 사용된다.

아래는 스프링 배치가 제공하는 자주 쓰이는 ItemReader 구현체들인데 굳이 직접 만들지 말고 왠만하면 out of the box 부품들을 사용하도록 하자.

    FlatFileItemReader
    HibernateCursorItemReader
    JdbcCursorItemReader
    JsonItemReader
    MongoItemReader


[ItemReader 공식 문서](https://docs.spring.io/spring-batch/docs/current/api/org/springframework/batch/item/ItemReader.html)
    

## ItemProcessor

ItemProcessor는 ItemReader에게서 Object를 넘겨받아 원하는 방식으로 가공 후에 ItemWriter에게 넘겨주는 역할을 하며, 한 번에 하나의 아이템을 처리한다.

# ItemProcessor 정의
public interface ItemProcessor<I,O>

ItemReader와 마찬가지로 인터페이스이며 Input과 Output을 지정해주어야 한다. Input은 ItemReader에게서 넘겨받는 타입이고 Output은 ItemWriter에게 넘겨줄 타입이다.

한마디로 ItemReader<T>의 T와 ItemProcessor<I,O>의 I는 같은 타입이어야 하고

ItemProcessor<I,O>의 O와 ItemWriter<T>의 T가 같은 타입이어야 한다.


O process(I item)
process 메소드로 넘겨받은 item을 가공하여 수정된 item을 그대로 넘기거나 새로운 객체를 만들어 넘길 수 있다.

참고로 ItemProcessor가 꼭 필요한 건 아니다. 데이터를 가공할 필요가 없는 경우엔 ItemReader -> ItemWriter로 바로 넘겨도 상관없다.


[ItemProcessor 공식 문서]: (https://docs.spring.io/spring-batch/docs/current/api/org/springframework/batch/item/ItemProcessor.html)
    

## ItemWriter

# ItemWriter 정의
    
```java    
public interface ItemWriter<T>

void write(java.util.List<? extends T> items)
```

ItemReader 혹은 ItemProcessor가 ItemWriter로 데이터를 넘겨주면 리스트에 차곡차곡 쌓아놓는다. 이때 commit-interval 프로퍼티의 정의된 개수만큼 데이터가 모이면 write 메소드를 실행하게 된다. commit-interval은 Step에 설정할 수 있다. (정확히는 Step 안에 Chunk에)

ItemReader와 마찬가지로 스프링 배치에서 제공해주는 자주 쓰이는 ItemWriter 구현체들이다.
<pre>
    CompositeItemWriter 
    FlatFileItemWriter
    HibernateItemWriter
    JdbcBatchItemWriter
    JsonFileItemWriter
    MongoItemWriter
</pre>

[ItemWriter 공식 문서]: (https://docs.spring.io/spring-batch/docs/current/api/org/springframework/batch/item/ItemWriter.html)
