package com.example.adminreference.repository;

import com.example.adminreference.entity.SysShopStatusHist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopStatusHistoryRepository extends JpaRepository<SysShopStatusHist, Long> {
}
