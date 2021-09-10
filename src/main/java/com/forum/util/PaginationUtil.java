package com.forum.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PaginationUtil {
    public static int getPage(Optional<Integer> page) {
        if (page.isPresent() && page.get() > 0) {
            return page.get();
        }
        return 1;
    }

    public static List<Integer> getPageNumbers(int numberOfPages) {
        if (numberOfPages > 0) {
            return IntStream.rangeClosed(1, numberOfPages)
                    .boxed()
                    .collect(Collectors.toList());
        }

        return null;
    }
}
