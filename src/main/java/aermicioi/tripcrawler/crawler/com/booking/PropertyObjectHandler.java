package aermicioi.tripcrawler.crawler.com.booking;

import aermicioi.tripcrawler.crawler.PropertyModel;
import com.codeborne.selenide.SelenideElement;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyObjectHandler {

    private final static Pattern PRICE_PATTERN = Pattern.compile("^([\\d]+)");

    public PropertyModel read(final SelenideElement root$) {
        root$.scrollIntoView(true);

        final SelenideElement content$ = root$.$(".sr_item_content");
        return PropertyModel.builder()
                .photo(readImage(root$))
                .url(readUrl(content$))
                .name(readName(content$))
                .price(readPrice(content$))
                .build();
    }

    private String readImage(final SelenideElement root$) {
        return root$.$(".sr_item_photo .sr_item_photo_link .hotel_image")
                .attr("src");
    }

    private String readUrl(final SelenideElement content$) {
        return content$.$(".hotel_name_link")
                .attr("href");
    }

    private String readName(final SelenideElement content$) {
        return content$.$(".sr-hotel__name").getText();
    }

    private BigDecimal readPrice(final SelenideElement content$) {
        final String priceText = content$.$(".bui-price-display__value").getText()
                .replaceAll("[\\s,]", "");

        final Matcher matcher = PRICE_PATTERN.matcher(priceText);
        return matcher.find()
                ? BigDecimal.valueOf(Double.parseDouble(matcher.group(1)))
                : BigDecimal.ZERO;
    }

}
