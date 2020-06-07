package aermicioi.tripcrawler.crawler.com.booking;

import aermicioi.tripcrawler.crawler.PropertyModel;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResultsPage {

    private final SelenideElement loading$ = Selenide.$(".sr-usp-overlay__loading");
    private final SelenideElement resultsTable$ = Selenide.$("#search_results_table");
    private final List<SelenideElement> properties$ = resultsTable$.$$(".sr_item");

    public List<PropertyModel> readProperties() {
        waitWhileLoading();

        final List<PropertyModel> properties = new ArrayList<>();
        final SelenideElement nextAnchor$ = resultsTable$.$("a.paging-next");
        do {
            properties.addAll(properties$.stream()
                    .map(PropertyObject::new)
                    .map(PropertyObject::getModel)
                    .collect(Collectors.toList()));

            nextAnchor$.scrollIntoView(true);
            nextAnchor$.click();
            waitWhileLoading();
        } while (nextAnchor$.exists());

        return properties;
    }

    private void waitWhileLoading() {
        Selenide.sleep(5000);
        while(loading$.exists() && loading$.isDisplayed()) {
            Selenide.sleep(500);
        }
    }
}
