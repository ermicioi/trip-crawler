package aermicioi.tripcrawler;

import aermicioi.tripcrawler.crawler.CrawlerService;
import aermicioi.tripcrawler.crawler.SearchRequest;
import aermicioi.tripcrawler.crawler.SearchResult;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.task.TaskExecutor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Slf4j
@Validated
public class FlexedTripCrawlerService {

    @Builder
    @Getter
    @ToString
    public static class FlexedTripSearchRequest {
        @NotBlank
        private final String location;

        @NotNull
        @FutureOrPresent
        private final LocalDate startDate;

        @NotNull
        @FutureOrPresent
        private final LocalDate endDate;

        @Min(1)
        private final int lengthOfStay;

        @Min(1)
        private final int adultsCount;

        @NotNull
        private final Collection<@Min(0) Integer> children;

        @Min(1)
        private final int roomsCount;
    }

    private final TaskExecutor crawlerTaskExecutor;
    private final List<CrawlerService> partyCrawlerServices;
    private final FlexedResultsCsvExportService exportService;

    @SneakyThrows(IOException.class)
    public void search(@Valid @NotNull final FlexedTripSearchRequest searchRequest) {
        final List<Pair<LocalDate, LocalDate>> periods = computeSearchPeriods(
                searchRequest.getStartDate(),
                searchRequest.getEndDate(),
                searchRequest.getLengthOfStay());
        log.debug("Computed {} periods", periods.size());

        final int permutations = periods.size() * partyCrawlerServices.size();
        log.debug("Computed permutations: {}", permutations);

        final List<SearchResult> results = new ArrayList<>(permutations);
        final CountDownLatch countDownLatch = new CountDownLatch(permutations);
        periods.forEach(period -> {
            final SearchRequest periodSearchRequest = SearchRequest.builder()
                    .checkInDate(period.getLeft())
                    .checkOutDate(period.getRight())
                    .location(searchRequest.getLocation())
                    .adultsCount(searchRequest.getAdultsCount())
                    .children(searchRequest.getChildren())
                    .roomsCount(searchRequest.getRoomsCount())
                    .build();

            search(periodSearchRequest, searchResult -> {
                results.add(searchResult);
                countDownLatch.countDown();
            });
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("Execution interruption", e);
        }

        exportService.export(results, new FileWriter(String.format(
                "tripcrawler-%s.csv",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    private List<Pair<LocalDate, LocalDate>> computeSearchPeriods(
            final LocalDate startDate,
            final LocalDate endDate,
            final int lengthOfStay) {
        final int searchPeriodDaysCount = Period.between(startDate, endDate).getDays();
        log.debug("Days between start/end period {}", searchPeriodDaysCount);

        if (searchPeriodDaysCount < lengthOfStay) {
            throw new IllegalStateException("The count of days is less then length of stay.");
        }

        final int periodsCount = searchPeriodDaysCount - lengthOfStay + 1;
        log.debug("Periods count {}", periodsCount);

        return IntStream.range(0, periodsCount)
                .mapToObj(i -> new ImmutablePair<>(startDate.plusDays(i), startDate.plusDays(i + lengthOfStay)))
                .collect(Collectors.toUnmodifiableList());
    }

    private void search(final SearchRequest searchRequest, final Consumer<SearchResult> callback) {
        partyCrawlerServices.forEach(
                service -> crawlerTaskExecutor.execute(() -> {
                    log.debug("Searching at party {} for request {}",
                            service.getClass().getSimpleName(),
                            searchRequest);
                    callback.accept(service.search(searchRequest));
                }));
    }

}
