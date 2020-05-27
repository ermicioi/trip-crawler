package aermicioi.tripcrawler.crawler.com.booking;

import aermicioi.tripcrawler.crawler.PartyCrawlerService;
import aermicioi.tripcrawler.crawler.SearchRequest;

public class BookingComPartyCrawlerService implements PartyCrawlerService {

    private final LandingPage landingPage = new LandingPage();
    private final ResultsPage resultsPage = new ResultsPage();

    @Override
    public void search(final SearchRequest searchRequest) {
        landingPage.open();
        landingPage.search(searchRequest);

        resultsPage.readProperties();
    }

}
