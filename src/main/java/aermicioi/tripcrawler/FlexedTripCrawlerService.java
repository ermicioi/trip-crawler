package aermicioi.tripcrawler;

import aermicioi.tripcrawler.crawler.PartyCrawlerService;
import aermicioi.tripcrawler.crawler.SearchRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
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
    private final List<PartyCrawlerService> partyCrawlerServices;

    public void search(@Valid final FlexedTripSearchRequest searchRequest) {
        doSearch(searchRequest);
    }

    private void doSearch(final FlexedTripSearchRequest searchRequest) {
        final List<Pair<LocalDate, LocalDate>> periods = this.computeSearchPeriods(
                searchRequest.getStartDate(),
                searchRequest.getEndDate(),
                searchRequest.getLengthOfStay());
        log.debug("Computed {} periods", periods.size());

        final CountDownLatch countDownLatch = new CountDownLatch(periods.size() * partyCrawlerServices.size());
        periods.forEach(period -> {
            final SearchRequest localSearchRequest = SearchRequest.builder()
                    .checkInDate(period.getLeft())
                    .checkOutDate(period.getRight())
                    .location(searchRequest.location)
                    .adultsCount(searchRequest.getAdultsCount())
                    .children(searchRequest.getChildren())
                    .roomsCount(searchRequest.getRoomsCount())
                    .build();

            partyCrawlerServices.forEach(
                    service -> crawlerTaskExecutor.execute(() -> {
                        log.debug("Searching at party {} for dates {} - {}",
                                service.getClass().getSimpleName(),
                                localSearchRequest.getCheckInDate(),
                                localSearchRequest.getCheckOutDate());

                        try {
                            service.search(localSearchRequest);
                        } finally {
                            countDownLatch.countDown();
                        }
                    }));
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("Execution interruption", e);
        }
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

}
