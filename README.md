### SpringBatch

# 스프링 배치 Job의 구성

![spring-batch-reference-model](https://user-images.githubusercontent.com/19945223/173719948-f60ffed1-3d03-4641-938c-1db2e006d377.png)

# Job
Job = 배치 작업이다. 혹은 Flow라고도 부른다.  최소 하나의 Step을 가져야 하며 엄청나게 복잡한 Job이 아닌 이상 2-10개의 Step을 권장한다 (솔직히 10개도 너무 많다). 만약 Step 개수가 10개 이상이면 일반적인 코드 리팩토링처럼 여러 개의 Job으로 쪼개서 하나의 Job에 너무 많은 책임들(Responsibilities)이 몰리게 하지 말자. 프로그램을 만드는 것도 중요하지만 유지보수와 관리, 모니터링도 그만큼 중요하니까.


# Step
Step은 읽기 -> 가공하기 -> 쓰기의 묶음이다. 이 묶음을 Chunk processing이라고 부르는데 하나의 트랜잭션으로 이해하면 된다. 바로 이 Chunk processing이 위에 언급한 재시작의 핵심이다.

다음은 스프링 공식 문서에서 가져온 Chunk processing의 컨셉트를 보여주는 예제이다. 한꺼번에 다 읽어서 쓰는 게 아닌 commitInterval 만큼 읽고 쓰기 때문에 재시작이 가능한 것이다.

<pre>
List items = new Arraylist();

for(int i = 0; i < commitInterval; i++){

    Object item = itemReader.read()

    Object processedItem = itemProcessor.process(item);

    items.add(processedItem);

}

itemWriter.write(items);
</pre>
