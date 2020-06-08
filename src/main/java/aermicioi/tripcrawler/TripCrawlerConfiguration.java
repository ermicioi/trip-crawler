package aermicioi.tripcrawler;

import aermicioi.tripcrawler.crawler.CrawlerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.List;

@Configuration
public class TripCrawlerConfiguration {

    @Bean
    public FlexedTripCrawlerService flexedTripCrawlerService(final List<CrawlerService> crawlerServices) {
        final SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(4);
        simpleAsyncTaskExecutor.setDaemon(true);
        simpleAsyncTaskExecutor.setThreadNamePrefix("CRAWLER");

        return new FlexedTripCrawlerService(
                new SimpleAsyncTaskExecutor(),
                crawlerServices,
                flexedResultsCsvExportService());
    }

    @Bean
    public FlexedResultsCsvExportService flexedResultsCsvExportService() {
        return new FlexedResultsCsvExportService();
    }

}
