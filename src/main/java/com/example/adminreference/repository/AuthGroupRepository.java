package com.example.adminreference.repository;

import com.example.adminreference.entity.TbAuthGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthGroupRepository extends JpaRepository<TbAuthGroup, Long> {

    boolean existsByAuthGroupId(long authGroupId);

    boolean existsByAuthGroupName(String authGroupName);

    boolean existsByAuthGroupIdNotAndAuthGroupName(long authGroupId, String authGroupName);

}
