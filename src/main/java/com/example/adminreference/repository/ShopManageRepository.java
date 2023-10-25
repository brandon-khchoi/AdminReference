package com.example.adminreference.repository;

import com.example.adminreference.entity.SysShopMgt;
import com.example.adminreference.enumeration.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ShopManageRepository extends JpaRepository<SysShopMgt, Integer> {

    long countByShopIdNotAndStatusCd(int shopId, ServiceStatus statusCd);

    boolean existsByMemberId(String memberId);

    boolean existsByShopIdNotAndMemberId(int shopId, String memberId);

    @Modifying
    @Query("update SysShopMgt ssm SET ssm.shopSort = null, ssm.updDt = ssm.updDt")
    int resetAllShopSort();

}
