package com.besmartexim.database.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.besmartexim.database.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public User findByEmailAndPassword(String email, String password);

	@Query(nativeQuery = true, value = "SELECT * FROM [CUS_DB].[dbo].[users] WHERE email= :email \r\n"
			+ "and user_type= :userType and password = :password COLLATE SQL_Latin1_General_CP1_CS_AS;")
	public User findByEmailAndUserTypeAndPassword(String email, String userType, String password);

	public User findByEmailAndUserType(String email, String userType);

	public User findByEmailAndUserTypeAndIsDelete(String email, String userType, String isDelete);

	public User findByEmailAndIsDelete(String email, String isDelete);

	public List<User> findAllByIsDeleteOrderByIdDesc(String isDelete);

	public List<User> findAllByIsDeleteAndUplineIdOrderByIdDesc(String string, Long uplineId);

	public List<User> findAllByIsDeleteAndUserTypeOrderByIdDesc(String string, String userType);

	public List<User> findAllByIsActive(String isAcvive);

	// public List<User> findAllByIsdeleteOrderByIdDesc(String isDelete);

	// public User findById(Long id);

	@Query(nativeQuery = true, value = "SELECT u.*, us.account_expire_date FROM user_subscription us, users u where u.id = us.user_id and u.is_delete='N' and us.account_expire_date < GETDATE();")
	public List<User> findAllExpiredUsers();
	
	public Page<User> findByIsDeleteAndUplineIdOrId(String isDelete,Long uplineId, Long id, Pageable pageable);
	
	public Page<User> findByIsDeleteAndUplineId(String isDelete, Long uplineId, Pageable pageable);
	
	public Page<User> findByUplineId(Long uplineId, Pageable pageable);
	
	public Long countByIsDeleteAndUplineIdOrId(String isDelete, Long uplineId, Long id);
	
	public Long countByIsDeleteAndUplineId(String isDelete, Long uplineId);
	
	public Long countByUplineId(Long uplineId);
	
	public List<User> findByUplineIdAndUserType(Long uplineId, String userType);
}