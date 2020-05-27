package aermicioi.tripcrawler.crawler.com.booking;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

public class ResultsPage {

    private SelenideElement loading$ = Selenide.$(".sr-usp-overlay__loading");
    private SelenideElement resultsTable$ = Selenide.$("#search_results_table");
    private List<SelenideElement> properties$ = resultsTable$.$$(".sr_item");

    public void readProperties() {
        waitWhileLoading();

        final SelenideElement nextAnchor$ = resultsTable$.$("a.paging-next");
        do {
            properties$.stream()
                    .map(PropertyObject::new)
                    .map(PropertyObject::readProperty)
                    .forEach(System.out::println);

            nextAnchor$.scrollIntoView(true);
            nextAnchor$.click();
            waitWhileLoading();
        } while(nextAnchor$.exists());
    }

    private void waitWhileLoading() {
        Selenide.sleep(5000);
        while(loading$.exists() && loading$.isDisplayed()) {
            Selenide.sleep(500);
        }
    }
}
