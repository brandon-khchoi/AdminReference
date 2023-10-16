package com.example.adminreference.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_auth_group_permission")
public class TbAuthGroupPermission implements Serializable {

    @EmbeddedId
    private TbAuthGroupPermissionId tbAuthGroupPermissionId;

    @Column(updatable = false, insertable = false)
    private LocalDateTime regDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permissionId")
    @MapsId("permissionId")
    @ToString.Exclude
    private TbAuthPermission tbAuthPermission;

}
