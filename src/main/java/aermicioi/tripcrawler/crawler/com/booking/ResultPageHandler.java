package aermicioi.tripcrawler.crawler.com.booking;

import aermicioi.tripcrawler.crawler.PropertyModel;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ResultPageHandler {

    private final PropertyObjectHandler propertyObjectHandler;

    public List<PropertyModel> readProperties() {
        waitWhileLoading();

        final List<PropertyModel> properties = new ArrayList<>();

        final SelenideElement resultsTable$ = Selenide.$("#search_results_table");
        final SelenideElement nextAnchor$ = resultsTable$.$("a.paging-next");
        do {
            properties.addAll(resultsTable$.$$(".sr_item").stream()
                    .map(propertyObjectHandler::read)
                    .collect(Collectors.toList()));

            nextAnchor$.scrollIntoView(true);
            nextAnchor$.click();
            waitWhileLoading();
        } while (nextAnchor$.exists());

        return properties;
    }

    private void waitWhileLoading() {
        Selenide.sleep(5000);

        final SelenideElement loading$ = Selenide.$(".sr-usp-overlay__loading");
        while(loading$.exists() && loading$.isDisplayed()) {
            Selenide.sleep(500);
        }
    }

}
