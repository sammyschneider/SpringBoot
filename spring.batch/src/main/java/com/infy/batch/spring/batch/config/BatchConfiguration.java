package com.infy.batch.spring.batch.config;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.infy.batch.spring.batch.listener.JobCompletionNotificationListener;
import com.infy.batch.spring.batch.model.Client;
import com.infy.batch.spring.batch.model.Person;
import com.infy.batch.spring.batch.processor.ClientItemProcessor;
import com.infy.batch.spring.batch.processor.PersonItemProcessor;



@Configuration
@EnableBatchProcessing // Provides standalone, production-ready, spring-based apps. 
						//Triggers auto-configuration and spring component scanning

public class BatchConfiguration {

	
	// Spring Batch is a framework designed to enable the development of robust batch apps vital for enterprises
	// A STEP LOADS DATA FROM FILE INTO DATABASE
	// A JOB CONSISTS OF MANY STEPS
	// A JOB CAN HAVE MORE THAN 1 STEP -- AND EVERY STEP FOLLOWS THE SEQUENCE OF READING DATA, PROCESSING, AND WRITING IT
    // BY DEFAULT, SPRING BATCH EXECUTES ALL JOBS IT CAN FIND. (YOU CAN CHANGE THAT IN APP.PROPERTIES)
	// @ENABLESCHEDULING ANNOTATION FOR DELAYS IN SCHEDULING
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;


    @Bean
    public FlatFileItemReader<com.infy.batch.spring.batch.model.Person> reader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
        reader.setResource(new ClassPathResource("persons.csv"));
        reader.setLineMapper(new DefaultLineMapper<Person>() {{ //
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "firstName", "lastName","email","age" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }});
        }});
        return reader;
    }
    
    @Bean
    public FlatFileItemReader<com.infy.batch.spring.batch.model.Client> reader2() {
        FlatFileItemReader<Client> reader2 = new FlatFileItemReader<Client>();
        reader2.setResource(new ClassPathResource("client.csv"));
        reader2.setLineMapper(new DefaultLineMapper<Client>() {{ //
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "clientName" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Client>() {{
                setTargetType(Client.class);
            }});
        }});
        return reader2;
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }
    @Bean
    public ClientItemProcessor processor2() {
        return new ClientItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer() {
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
        writer.setSql("INSERT INTO person (first_name, last_name,email,age) VALUES (:firstName, :lastName,:email,:age)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public JdbcBatchItemWriter<Client> writer2() {
        JdbcBatchItemWriter<Client> writer2 = new JdbcBatchItemWriter<Client>();
        writer2.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Client>());
        writer2.setSql("INSERT INTO client (client_name) VALUES (:clientName)");
        writer2.setDataSource(dataSource);
        return writer2;
    }
    @Bean
    public Job job1(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .next(step2())
                .end()
                .build();
    }
    

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .<Client, Client> chunk(10)
                .reader(reader2())
                .processor(processor2())
                .writer(writer2())
                .build();
    }
}
