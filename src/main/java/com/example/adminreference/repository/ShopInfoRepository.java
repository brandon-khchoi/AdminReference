package com.example.adminreference.repository;

import com.example.adminreference.entity.SysShopInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopInfoRepository extends JpaRepository<SysShopInfo, Integer> {

    boolean existsByShopIdNotAndCorpRegNo(int shopId, String corpRegNo);

    boolean existsByCorpRegNo(String corpRegNo);

}
