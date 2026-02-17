package net.gopa.mc.whooshwhoosh.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class IteratorUtil {

    public static <T> List<T> toList(Iterator<T> iterator) {
        return StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
                .collect(Collectors.toList());
    }
}
