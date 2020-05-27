package aermicioi.tripcrawler;

import aermicioi.tripcrawler.FlexedTripCrawlerService.FlexedTripSearchRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TripCrawlerAppTest {

    @Autowired
    private FlexedTripCrawlerService flexedTripCrawlerService;

    @Test
    public void testFlexedSearch() {
        flexedTripCrawlerService.search(FlexedTripSearchRequest.builder()
                .location("Kostrena")
                .startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 8, 9))
                .lengthOfStay(7)
                .adultsCount(5)
                .children(List.of(1))
                .roomsCount(3)
                .build());
        System.out.println("test");
    }

}