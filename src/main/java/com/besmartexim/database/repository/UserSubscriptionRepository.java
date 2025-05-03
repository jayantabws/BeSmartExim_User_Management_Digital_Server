package com.besmartexim.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.besmartexim.database.entity.UserSubscription;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

	List<UserSubscription> findAllByUserIdOrderByIdDesc(Long userId);

	List<UserSubscription> findAllByUserIdAndIsActiveOrderByIdDesc(Long userId, String string);

}
