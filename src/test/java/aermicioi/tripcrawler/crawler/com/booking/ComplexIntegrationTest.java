package aermicioi.tripcrawler.crawler.com.booking;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.time.LocalDate;

public class ComplexIntegrationTest {

    @Test
    public void doTest() {
        final BookingComCrawlerService service = new BookingComCrawlerService();
        service.search(SearchRequest.builder()
                .location("Kostrena")
                .checkInDate(LocalDate.of(2020, 8, 1))
                .checkOutDate(LocalDate.of(2020, 8, 14))
                .adultsCount(5)
                .children(Lists.newArrayList(1))
                .roomsCount(3)
                .build());
    }

}