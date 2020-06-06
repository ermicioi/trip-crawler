package aermicioi.tripcrawler.crawler.com.booking;

import aermicioi.tripcrawler.crawler.CrawlerService;
import aermicioi.tripcrawler.crawler.SearchRequest;
import aermicioi.tripcrawler.crawler.SearchResult;

public class BookingComCrawlerService implements CrawlerService {

    private final LandingPage landingPage = new LandingPage();
    private final ResultsPage resultsPage = new ResultsPage();

    @Override
    public SearchResult search(final SearchRequest searchRequest) {
        landingPage.open();
        landingPage.search(searchRequest);

        return new SearchResult(
                searchRequest.getCheckInDate(),
                searchRequest.getCheckOutDate(),
                resultsPage.readProperties());
    }

}
