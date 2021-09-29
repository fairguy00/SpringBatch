package com.hwantastic.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class TemplateTasklet implements Tasklet {
	
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("작업 시작...");
        // 원하는 작업을 여기에서 할 수 있다.
        System.out.println("작업 완료!");
        return null;
	}//execute 메소드가 null을 반환하면 RepeatStatus.FINISHED 를 반환하는 것과 같은 의미를 가진다. = 반복 없이 한 번만 실행
	
}
