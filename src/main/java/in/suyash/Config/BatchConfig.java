package in.suyash.Config;

import in.suyash.Repository.CustomerRepository;
import in.suyash.entity.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public FlatFileItemReader<Customer> customerFlatFileItemReader() {
        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("D:\\JRTP\\10_Spring_Batch\\src\\main\\resources\\customers(1).csv"));
        itemReader.setName("Customers-item-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());

        return itemReader;
    }

    private LineMapper<Customer> lineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");

        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class); // Set the target type here

        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.setLineTokenizer(lineTokenizer);

        return lineMapper;
    }

    @Bean
    public CustomerProcessoor processoor() {
        return new CustomerProcessoor();
    }

    @Bean
    public RepositoryItemWriter<Customer> customerRepositoryItemWriter() {
        RepositoryItemWriter<Customer> itemWriter = new RepositoryItemWriter<>();
        itemWriter.setRepository(customerRepository);
        itemWriter.setMethodName("save");

        return itemWriter;
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step-1")
                .<Customer, Customer>chunk(10)
                .reader(customerFlatFileItemReader())
                .processor(processoor())
                .writer(customerRepositoryItemWriter())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("customer-import")
                .flow(step())
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }
}
