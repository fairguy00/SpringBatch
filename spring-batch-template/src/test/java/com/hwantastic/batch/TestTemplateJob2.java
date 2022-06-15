package com.hwantastic.batch;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/com/hwantastic/batch2/template_job2.xml" })
public class TestTemplateJob2 {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    // AtomicBoolean을 사용해 한 테스트 케이스에 Job이 여러 번 실행되는 걸 방지한다.
    private static AtomicBoolean isLaunched = new AtomicBoolean(false);

    private JobExecution jobExecution;

    @Before
    public void setUp() throws Exception {
        if (!isLaunched.getAndSet(true)) {
            jobExecution = jobLauncherTestUtils.launchJob();
        }
    }

    @Test
    public void testExitCode() {
        // Job이 에러 없이 종료되었는지 확인
        Assert.assertEquals(ExitStatus.COMPLETED.getExitCode(), jobExecution.getExitStatus().getExitCode());
    }
}
