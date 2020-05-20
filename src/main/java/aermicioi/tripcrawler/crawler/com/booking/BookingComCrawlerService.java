package aermicioi.tripcrawler.crawler.com.booking;

public class BookingComCrawlerService {

    private final LandingPage landingPage = new LandingPage();
    private final ResultPage resultPage = new ResultPage();

    public void search(final SearchRequest searchRequest) {
        landingPage.open();
        landingPage.search(searchRequest);

        resultPage.readProperties();
    }

}
