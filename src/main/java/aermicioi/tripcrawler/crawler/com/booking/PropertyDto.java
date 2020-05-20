package aermicioi.tripcrawler.crawler.com.booking;

import lombok.Builder;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@ToString
public class PropertyDto {

    private final String photo;
    private final String name;
    private final BigDecimal price;

}
