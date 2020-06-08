package aermicioi.tripcrawler;

import aermicioi.tripcrawler.FlexedTripSearchRequest.FlexedTripSearchRequestBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@RequiredArgsConstructor
public class TripCrawlerApp implements CommandLineRunner {

    private final FlexedTripCrawlerService flexedTripCrawlerService;

    public static void main(String[] args) {
        SpringApplication.run(TripCrawlerApp.class, args);
    }

    @Override
    public void run(final String... args) {
        final FlexedTripSearchRequestBuilder searchRequestBuilder = FlexedTripSearchRequest.builder()
                .location(args[0])
                .startDate(LocalDate.parse(args[1], DateTimeFormatter.ISO_LOCAL_DATE))
                .endDate(LocalDate.parse(args[2], DateTimeFormatter.ISO_LOCAL_DATE))
                .lengthOfStay(Integer.parseInt(args[3]))
                .roomsCount(Integer.parseInt(args[4]))
                .adultsCount(Integer.parseInt(args[5]));

        if (args.length > 6) {
            for (int i = 6; i < args.length; i++) {
                searchRequestBuilder.child(Integer.parseInt(args[i]));
            }
        }

        flexedTripCrawlerService.search(searchRequestBuilder.build());
    }
}
