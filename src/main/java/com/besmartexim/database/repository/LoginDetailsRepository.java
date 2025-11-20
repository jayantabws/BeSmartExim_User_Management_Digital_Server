package com.besmartexim.database.repository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.besmartexim.database.entity.LoginDetails;

@Repository
public interface LoginDetailsRepository extends JpaRepository<LoginDetails, Long> {

	List<LoginDetails> findByUserId(Long userId, Pageable pageable);
	Long countByUserId(Long userId);

	Page<LoginDetails> findAll(Pageable pageable);

	@Query(nativeQuery = true, value="SELECT * FROM login_details where user_id = :uplineId or user_id in (select id from users where upline_id = :uplineId) order by id desc offset :page rows fetch next :size rows only")
	List<LoginDetails> findByUplineIdOrderByIdDesc(Long uplineId, int page, int size);
	@Query(nativeQuery = true, value="SELECT count(*) FROM login_details where user_id = :uplineId or user_id in (select id from users where upline_id = :uplineId)")
	Long countByUplineIdCustom(Long uplineId);
	
	@Query(nativeQuery = true, value="SELECT max(login_time) FROM login_details where user_id = :uplineId or user_id in (select id from users where upline_id = :uplineId)")
	Date findByUplineIdMaxLoginDate(Long uplineId);
	
	@Query(nativeQuery = true, value="SELECT * FROM login_details where user_id = :userId and logout_time is NULL")
	List<LoginDetails> findByLogoutTimeNULL(Long userId);
	
	
	//Date
	@Query(nativeQuery = true, value="SELECT * FROM login_details where user_id = :userId and login_time between :fromDate and :toDate order by id desc offset :page rows fetch next :size rows only")
	List<LoginDetails> findByUserIdAndDateRange(Long userId, int page, int size, Date fromDate, Date toDate);//D1
	
	
	@Query(nativeQuery = true, value="SELECT * FROM login_details where user_id = :uplineId or user_id in (select id from users where upline_id = :uplineId) and login_time between :fromDate and :toDate order by id desc offset :page rows fetch next :size rows only")
	List<LoginDetails> findByUplineIdAndDateRangeOrderByIdDesc(Long uplineId, int page, int size, Date fromDate, Date toDate);//D2
	
	
	@Query(nativeQuery = true, value="SELECT * FROM login_details where login_time between :fromDate and :toDate order by id desc offset :page rows fetch next :size rows only")
	List<LoginDetails> findAllByDateRange(int page, int size, Date fromDate, Date toDate);//D3
	
	
	//Date Count
	@Query(nativeQuery = true, value="SELECT count(*) FROM login_details where user_id = :userId and login_time between :fromDate and :toDate")
	Long countByUserIdAndDateRange(Long userId, Date fromDate, Date toDate);//DC1
	
	@Query(nativeQuery = true, value="SELECT count(*) FROM login_details where user_id = :uplineId or user_id in (select id from users where upline_id = :uplineId) and login_time between :fromDate and :toDate")
	Long countByUplineIdAndDateRange(Long uplineId, Date fromDate, Date toDate);//DC2
	
	@Query(nativeQuery = true, value="SELECT count(*) FROM login_details where login_time between :fromDate and :toDate")
	Long countByDateRange(Date fromDate, Date toDate);//DC3

	@Query(nativeQuery = true, value = "select * from login_details where login_time between :fromDate and :toDate and logout_time is null order by login_time desc")
	List<LoginDetails> allLoginUsers(Date fromDate, Date toDate);
	
	
	@Query(nativeQuery = true, value = "select count(*) from login_details where login_time between :fromDate and :toDate and logout_time is null")
	Long countAllLoginUsers(Date fromDate, Date toDate);
}
