package aermicioi.tripcrawler.crawler.com.booking;

public class BookingComCrawlerService {

    private final LandingPage landingPage = new LandingPage();

    public void search(final SearchRequest searchRequest) {
        landingPage.open();
        landingPage.search(searchRequest);
    }

}
