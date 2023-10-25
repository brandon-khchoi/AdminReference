package com.example.adminreference.repository;

import com.example.adminreference.entity.TbUserInfo;
import com.example.adminreference.enumeration.AccountState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<TbUserInfo, Long> {


    boolean existsByAdminId(String id);

    Optional<TbUserInfo> findByAdminId(String id);

    List<TbUserInfo> findByAuthGroupId(long authGroupId);

    List<TbUserInfo> findAllByAuthGroupIdAndIdIn(long authGroupId, List<Long> lunaUserNo);


}
