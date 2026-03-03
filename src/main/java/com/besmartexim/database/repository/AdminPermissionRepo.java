package com.besmartexim.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.besmartexim.database.entity.AdminPermission;

public interface AdminPermissionRepo extends JpaRepository<AdminPermission, Long>{

	public AdminPermission findByUserId(Long userId);
}
