package com.besmartexim.database.repository;

import java.util.List;

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
	
	public User findByEmailAndIsDelete(String email, String isDelete);

	public List<User> findAllByIsDeleteOrderByIdDesc(String isDelete);

	public List<User> findAllByIsDeleteAndUplineIdOrderByIdDesc(String string, Long uplineId);

	public List<User> findAllByIsDeleteAndUserTypeOrderByIdDesc(String string, String userType);
	
	public List<User> findAllByIsActive(String isAcvive);
	
	//public List<User> findAllByIsdeleteOrderByIdDesc(String isDelete);
	
	//public User findById(Long id);
	
	@Query(nativeQuery = true, value="SELECT u.*, us.account_expire_date FROM user_subscription us, users u where u.id = us.user_id and u.is_delete='N' and us.account_expire_date < GETDATE();")
	public List<User> findAllExpiredUsers();
}