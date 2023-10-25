package com.example.adminreference.common;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

public class PagingUtils {

    public static HttpHeaders getListHeader(Page<?> page) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Cellook-Paging-Page-No", String.valueOf(page.getPageable().getPageNumber() + 1));
        httpHeaders.add("X-Cellook-Paging-Page-Size", String.valueOf(page.getSize()));
        httpHeaders.add("X-Cellook-Paging-Page-Count", String.valueOf(page.getTotalPages()));
        httpHeaders.add("X-Cellook-Paging-Total-Record-Count", String.valueOf(page.getTotalElements()));

        return httpHeaders;
    }

}
