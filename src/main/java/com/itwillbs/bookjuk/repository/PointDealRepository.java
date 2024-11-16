package com.itwillbs.bookjuk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.bookjuk.entity.pay.PointDealEntity;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface PointDealRepository extends JpaRepository<PointDealEntity, Long> {


    // dashboard에서 사용
    @Query("SELECT SUM(p.pointPrice) FROM PointDealEntity p WHERE p.reqDate BETWEEN :startOfDay AND :endOfDay")
    Long sumAmountByReqDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);


    //userInfo 에서 사용
    Page<PointDealEntity> findByUserContentEntity_MemberNum(Long memberNum, Pageable pageable);
}
