package aermicioi.tripcrawler;

import aermicioi.tripcrawler.crawler.PartyCrawlerService;
import aermicioi.tripcrawler.crawler.com.booking.BookingComPartyCrawlerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.util.List;

@Configuration
public class TripCrawlerConfiguration {

    @Bean
    public FlexedTripCrawlerService fixedTripCrawlerService() {
        final SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(4);
        simpleAsyncTaskExecutor.setDaemon(true);
        simpleAsyncTaskExecutor.setThreadNamePrefix("CRAWLER");

        return new FlexedTripCrawlerService(
                new SimpleAsyncTaskExecutor(),
                List.of(
                        new BookingComPartyCrawlerService()
                ));
    }

}
