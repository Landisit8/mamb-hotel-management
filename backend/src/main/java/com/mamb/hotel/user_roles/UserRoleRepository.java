package com.mamb.hotel.user_roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    boolean existsByUser_IdAndRole_Id(Long userId, Long roleId);

    List<UserRole> findByUser_Id(Long userId);

    List<UserRole> findByRole_Id(Long roleId);
}