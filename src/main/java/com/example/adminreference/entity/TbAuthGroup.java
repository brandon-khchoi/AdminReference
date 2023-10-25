package com.example.adminreference.entity;

import com.example.adminreference.config.security.GrantType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@Table(name = "tb_auth_group")
public class TbAuthGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authGroupId;
    private String authGroupName;

    @Column(updatable = false, insertable = false)
    private LocalDateTime regDt;
    @Column(updatable = false, insertable = false)
    private LocalDateTime updDt;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tbAuthGroup")
    private List<TbUserInfo> tbUserInfoList;

    public static TbAuthGroup newInstance(String authGroupName) {
        TbAuthGroup tbAuthGroup = new TbAuthGroup();
        tbAuthGroup.authGroupName = authGroupName;
        return tbAuthGroup;
    }

    public TbAuthGroup(Long authGroupId,
                       String authGroupName,
                       Set<TbAuthPermission> tbAuthPermissionList) {
        this.authGroupId = authGroupId;
        this.authGroupName = authGroupName;
        this.regDt = LocalDateTime.now();
        this.updDt = LocalDateTime.now();
        this.tbAuthPermissionList = tbAuthPermissionList;
    }

    public void changeAuthGroupName(String authGroupName) {
        this.authGroupName = authGroupName;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "tb_auth_group_permission", joinColumns = @JoinColumn(name = "authGroupId"), inverseJoinColumns = @JoinColumn(name = "permissionId"))
    @ToString.Exclude
    Set<TbAuthPermission> tbAuthPermissionList;

    public int removeAuthPermission(long menuId, GrantType grantType) {
        return this.tbAuthPermissionList.removeIf(tbAuthPermission -> tbAuthPermission.getTbMenuInfo().getMenuId() == menuId && tbAuthPermission.getPermissionName().equals(grantType)) ? 1 : 0;
    }

    public int addAuthPermission(TbAuthPermission newAuthPermission) {
        return this.tbAuthPermissionList.add(newAuthPermission) ? 1 : 0;
    }

}
