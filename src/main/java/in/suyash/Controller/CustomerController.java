package in.suyash.Controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @GetMapping("/start")
    public void loadData() throws  Exception{

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt" , System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(job , jobParameters);

    }


}
