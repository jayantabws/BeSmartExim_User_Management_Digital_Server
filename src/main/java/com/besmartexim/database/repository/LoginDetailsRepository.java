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

}
