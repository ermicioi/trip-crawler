package aermicioi.tripcrawler.crawler;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collection;

@Builder
@Getter
@ToString
public class SearchRequest {
    private final String location;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final int adultsCount;
    private final Collection<Integer> children;
    private final int roomsCount;
}
