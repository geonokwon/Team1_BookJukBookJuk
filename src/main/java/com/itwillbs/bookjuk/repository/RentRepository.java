package com.itwillbs.bookjuk.repository;

import com.itwillbs.bookjuk.entity.RentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface RentRepository extends JpaRepository<RentEntity, Long> {
	
	//자정마다 연체상태 업데이트
	List<RentEntity> findByReturnDateIsNullAndRentDateBefore(Timestamp date);
	
	//검색
	Page<RentEntity> findByUserNameContaining(String keyword, Pageable pageable);
    Page<RentEntity> findByUserIdContaining(String keyword, Pageable pageable);
    Page<RentEntity> findByBookNameContaining(String keyword, Pageable pageable);

    long countByRentDateBeforeAndReturnDateAfterAndReturnInfo(Timestamp now, Timestamp now1, String info);

	long countByReturnDateBeforeAndReturnInfo(Timestamp returnDay, String info);
}
