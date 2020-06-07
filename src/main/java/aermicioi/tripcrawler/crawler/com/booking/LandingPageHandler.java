package aermicioi.tripcrawler.crawler.com.booking;

import aermicioi.tripcrawler.crawler.SearchRequest;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LandingPageHandler {

    private static final int ADULT_BASE_COUNT = 2;
    private static final int CHILD_BASE_COUNT = 0;
    private static final int ROOM_BASE_COUNT = 1;
    public static final String PAGE_URL = "https://www.booking.com/";

    public void open() {
        log.debug("Open page {}", PAGE_URL);
        Selenide.open(PAGE_URL);
    }

    public void search(final SearchRequest request) {
        log.debug("Searching based on request {}", request);
        acceptCookie();

        final SelenideElement $form = Selenide.$("#frm");
        $form.scrollIntoView(true);

        $form.$("input[name='ss']").setValue(request.getLocation());

        $form.$("div[data-mode='checkin']").click();

        final SelenideElement $calendar = Selenide.$("div[class='bui-calendar']");

        selectDate($calendar, request.getCheckInDate());
        selectDate($calendar, request.getCheckOutDate());

        $form.$("div.xp__guests").click();
        setGuestCountValues($form.$("div.sb-group__field-adults"), ADULT_BASE_COUNT, request.getAdultsCount());
        setGuestCountValues($form.$("div.sb-group-children"), CHILD_BASE_COUNT, request.getChildren().size());

        int k = 0;
        final ElementsCollection groupChildrenSelects = $form.$("div.sb-group__children__field").$$("select[name='age']");
        for (int age : request.getChildren()) {
            groupChildrenSelects.get(k++).$(String.format("option[value='%d']", age)).click();
        }

        setGuestCountValues($form.$("div.sb-group__field-rooms"), ROOM_BASE_COUNT, request.getRoomsCount());

        $form.submit();
    }

    private void acceptCookie() {
        final SelenideElement cookieWarning = Selenide.$("#cookie_warning");
        if (cookieWarning.exists()) {
            cookieWarning.$("button[for='cookie-warning-consent-flag']").click();
        }
    }

    private void setGuestCountValues(final SelenideElement root, int baseCount, int count) {
        int diff = count - baseCount;
        final SelenideElement el = diff > 0
                ? root.$("button.bui-stepper__add-button")
                : root.$("button.bui-stepper__subtract-button");
        for (int i = 0; i < Math.abs(diff); i++) {
            el.click();
        }
    }

    private void selectDate(final SelenideElement calendar, final LocalDate date) {
        final SelenideElement calNextButton = calendar.$("div[data-bui-ref='calendar-next']");

        final String dateString = date.format(DateTimeFormatter.ISO_DATE);
        final SelenideElement dateEl = calendar.$(String.format("td[data-date='%s']", dateString));

        int k = 12;

        while (!dateEl.exists() && k > 0) {
            calNextButton.click();
            Selenide.sleep(500);
            k--;
        }

        if (k < 0) {
            throw new IllegalStateException(String.format("Was not able to find the date %s", dateString));
        }

        dateEl.click();
    }

}
