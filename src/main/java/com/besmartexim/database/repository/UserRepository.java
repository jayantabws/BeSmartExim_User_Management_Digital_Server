package com.besmartexim.database.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.besmartexim.database.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	public User findByEmailAndPassword(String email, String password);
	
	public User findByEmailAndPasswordAndUserType(String email, String password, String userType);

	public User findByEmailAndPasswordAndUserTypeAndIsDelete(String email, String password, String userType, String isDelete);
	
	public User findByEmailAndUserType(String email, String userType);
	
	public User findByEmailAndUserTypeAndIsDelete(String email, String userType, String isDelete);
	
	public User findByEmailAndIsDelete(String email, String isDelete);

	public Page<User> findAllByIsDelete(String isDelete, Pageable pageable);

	public Page<User> findAllByIsDeleteAndUplineId(String string, Long uplineId, Pageable pageable);

	public Page<User> findAllByIsDeleteAndUserType(String string, String userType, Pageable pageable);
	
	public Page<User> findAllByIsActive(String isAcvive, Pageable pageable);
	
	//public List<User> findAllByIsdeleteOrderByIdDesc(String isDelete);
	
	//public User findById(Long id);
	
	@Query(nativeQuery = true, value="SELECT u.*, us.account_expire_date FROM user_subscription us, users u where u.id = us.user_id and u.is_delete='N' and us.account_expire_date < GETDATE() order by u.id desc offset :pageNumber rows fetch next :pageSize rows only ;")
	public List<User> findAllExpiredUsers(Integer pageNumber, Integer pageSize);
	
	public Long countByIsDeleteAndUplineId(String string, Long uplineId);
	
	public Long countByIsDeleteAndUserType(String string, String userType);
	
	@Query(nativeQuery = true, value="SELECT count(*) FROM user_subscription us, users u where u.id = us.user_id and u.is_delete='N' and us.account_expire_date < GETDATE();")
	public Long countAllExpiredUsers();
	
	public Long countByIsActive(String isAcvive);
	
	public Long countByIsDelete(String isDelete);
}