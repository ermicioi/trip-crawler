package aermicioi.tripcrawler.crawler.com.booking;

import aermicioi.tripcrawler.crawler.PropertyModel;
import com.codeborne.selenide.SelenideElement;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyObject {

    private final static Pattern PRICE_PATTERN = Pattern.compile("^([\\d]+)");

    private final SelenideElement propertyPhoto$;
    private final SelenideElement propertyName$;
    private final SelenideElement propertyLink$;
    private final SelenideElement propertyPrice$;

    public PropertyObject(SelenideElement root$) {
        this.propertyPhoto$ = root$.$(".sr_item_photo .sr_item_photo_link .hotel_image");

        final SelenideElement propertyContent$ = root$.$(".sr_item_content");
        this.propertyName$ = propertyContent$.$(".sr-hotel__name");
        this.propertyLink$ = propertyContent$.$(".hotel_name_link");
        this.propertyPrice$ = propertyContent$.$(".bui-price-display__value");
    }

    public PropertyModel getModel() {
        return PropertyModel.builder()
                .url(propertyLink$.attr("href"))
                .photo(this.propertyPhoto$.attr("src"))
                .name(this.propertyName$.getText())
                .price(readPrice())
                .build();
    }

    private BigDecimal readPrice() {
        final String priceText = propertyPrice$.getText()
                .replaceAll("[\\s,]", "");

        final Matcher matcher = PRICE_PATTERN.matcher(priceText);
        return matcher.find()
                ? BigDecimal.valueOf(Double.parseDouble(matcher.group(1)))
                : BigDecimal.ZERO;
    }

}
