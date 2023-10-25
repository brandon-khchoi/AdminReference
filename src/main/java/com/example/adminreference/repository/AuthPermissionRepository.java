package com.example.adminreference.repository;

import com.example.adminreference.config.security.GrantType;
import com.example.adminreference.entity.TbAuthPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthPermissionRepository extends JpaRepository<TbAuthPermission, Long> {

    TbAuthPermission findByTbMenuInfoMenuIdAndPermissionName(long menuId, GrantType permissionName);

}
