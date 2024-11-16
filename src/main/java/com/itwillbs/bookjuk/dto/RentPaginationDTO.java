package com.itwillbs.bookjuk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentPaginationDTO implements PaginationDTO<BookRentInfoDTO>{

    private List<BookRentInfoDTO> items;
    //페이지네이션 관련
    private int totalPages;
    private int currentPage;
    private boolean hasNext;
    private boolean hasPrevious;

    @Override
    public boolean hasNext() {
        return false;
    }
    @Override
    public boolean hasPrevious() {
        return false;
    }
}