package by.maksimruksha.mapgeneration.util;

import org.springframework.data.domain.Sort;

public class SortHelper {
    public static Sort.Direction orderDirection(String sortDirection) {
        if (sortDirection.equalsIgnoreCase("DESC")) {
            return Sort.Direction.DESC;
        } else {
            return Sort.Direction.ASC;
        }
    }
}
