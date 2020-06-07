package aermicioi.tripcrawler.crawler.com.booking;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookingComConfiguration {

    @Bean
    public BookingComCrawlerService bookingComCrawlerService() {
        return new BookingComCrawlerService(landingPageHandler(), resultPageHandler());
    }

    @Bean
    public LandingPageHandler landingPageHandler() {
        return new LandingPageHandler();
    }

    @Bean
    public ResultPageHandler resultPageHandler() {
        return new ResultPageHandler(propertyObjectHandler());
    }

    @Bean
    public PropertyObjectHandler propertyObjectHandler() {
        return new PropertyObjectHandler();
    }

}
