package aermicioi.tripcrawler;

import aermicioi.tripcrawler.crawler.com.booking.BookingComCrawlerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.List;

@Configuration
public class TripCrawlerConfiguration {

    @Bean
    public FlexedTripCrawlerService flexedTripCrawlerService() {
        final SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(4);
        simpleAsyncTaskExecutor.setDaemon(true);
        simpleAsyncTaskExecutor.setThreadNamePrefix("CRAWLER");

        return new FlexedTripCrawlerService(
                new SimpleAsyncTaskExecutor(),
                List.of(
                        new BookingComCrawlerService()
                ),
                new FlexedResultsCsvExportService());
    }

    @Bean
    public FlexedResultsCsvExportService flexedResultsCsvExportService() {
        return new FlexedResultsCsvExportService();
    }

}
