package aermicioi.tripcrawler.crawler.com.booking;

import aermicioi.tripcrawler.crawler.CrawlerService;
import aermicioi.tripcrawler.crawler.SearchRequest;
import aermicioi.tripcrawler.crawler.SearchResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookingComCrawlerService implements CrawlerService {

    private final LandingPageHandler landingPageHandler;
    private final ResultPageHandler resultPageHandler;

    @Override
    public SearchResult search(final SearchRequest searchRequest) {
        landingPageHandler.open();
        landingPageHandler.search(searchRequest);

        return new SearchResult(
                searchRequest.getCheckInDate(),
                searchRequest.getCheckOutDate(),
                resultPageHandler.readProperties());
    }

}
