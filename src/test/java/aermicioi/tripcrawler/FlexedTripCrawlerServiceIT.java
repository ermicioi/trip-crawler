package aermicioi.tripcrawler;

import aermicioi.tripcrawler.crawler.CrawlerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlexedTripCrawlerServiceIT {

    @Autowired private FlexedTripCrawlerService service;

    @Resource
    List<CrawlerService> partyCrawlerServices = new ArrayList<>();
    @MockBean private CrawlerService crawlerService;
    @MockBean private TaskExecutor crawlerTaskExecutor;
    @MockBean private FlexedResultsCsvExportService exportService;

    @Test
    public void testSearch() {
        service.search(FlexedTripSearchRequest.builder()
                .location("Kostrena")
                .startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2020, 8, 9))
                .lengthOfStay(7)
                .adultsCount(5)
                .children(List.of(1))
                .roomsCount(3)
                .build());
    }

}