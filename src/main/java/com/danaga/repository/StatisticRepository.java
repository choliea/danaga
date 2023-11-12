package com.danaga.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.danaga.entity.Statistic;


public interface StatisticRepository extends JpaRepository<Statistic, String>{
	// N일 총 판매수량
	@Query("select count(o) from Order o where FUNCTION('DATE_FORMAT', o.createDate, '%Y%m%d') = :findDate")
	Long countTotSalesOn(@Param("findDate") String findDate);

	// N일 총 판매수익
	@Query("select coalesce(sum(o.price), 0) from Order o where FUNCTION('DATE_FORMAT', o.createDate, '%Y%m%d') = :findDate")
	Long countTotRevenueOn(@Param("findDate") String findDate);

	// N일 가입한 신규회원 수
	@Query("select count(m) from Member m where FUNCTION('DATE_FORMAT', m.joinDate, '%Y%m%d') = :findDate")
	Long countNewMembersOn(@Param("findDate") String findDate);

	// N일 작성된 게시글 수 완료
	@Query("select count(b) from Board b where FUNCTION('DATE_FORMAT', b.createTime, '%Y%m%d') = :findDate")
	Long countNewBoardsOn(@Param("findDate") String findDate);

	// 최근 7일간의 통계 기록
	List<Statistic> findTop7ByOrderByCreateDateDesc();

	// YYYYMM월의 기록
	List<Statistic> findByIdStartingWith(String id);

	// M월 총 주문 건수
	@Query("select count(o) from Order o where FUNCTION('DATE_FORMAT', o.createDate, '%Y%m') = :findMonth")
	Long countTotSalesThisMonth(@Param("findMonth") String findMonth);

	// M월 배송중+입금대기 건수
	@Query("select count(o) from Order o where FUNCTION('DATE_FORMAT', o.createDate, '%Y%m') = :findMonth and (o.statement = '배송중' or o.statement = '입금대기중')")
	Long countToSalesThisMonth(@Param("findMonth") String findMonth);

	// 환불대기+환불완료+취소 건수
	@Query("select count(o) from Order o where FUNCTION('DATE_FORMAT', o.createDate, '%Y%m') = :findMonth and (o.statement = '환불대기중' or o.statement = '환불완료' or o.statement = '취소')")
	Long countFailSalesThisMonth(@Param("findMonth") String findMonth);

}
