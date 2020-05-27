package aermicioi.tripcrawler.crawler.com.booking;

import aermicioi.tripcrawler.crawler.PropertyModel;
import com.codeborne.selenide.SelenideElement;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyObject {

    private final static Pattern PRICE_PATTERN = Pattern.compile("^([\\d]+)");

    private final SelenideElement root$;
    private final SelenideElement photo$;
    private final SelenideElement contentContainer$;
    private final SelenideElement propertyName$;

    public PropertyObject(SelenideElement root$) {
        this.root$ = root$;
        this.photo$ = root$.$(".sr_item_photo .sr_item_photo_link .hotel_image");

        this.contentContainer$ = root$.$(".sr_item_content");
        this.propertyName$ = contentContainer$.$(".sr-hotel__name");
    }

    public PropertyModel readProperty() {
        final PropertyModel.PropertyModelBuilder builder = PropertyModel.builder();

        builder.photo(this.photo$.attr("src"))
                .name(this.propertyName$.getText())
                .price(readPrice());

        return builder.build();
    }

    private BigDecimal readPrice() {
        final String priceText = contentContainer$.$(".bui-price-display__value")
                .getText()
                .replaceAll("[\\s,]", "");

        final Matcher matcher = PRICE_PATTERN.matcher(priceText);
        return matcher.find()
                ? BigDecimal.valueOf(Double.parseDouble(matcher.group(1)))
                : BigDecimal.ZERO;
    }

}
