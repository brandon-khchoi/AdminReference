package com.example.adminreference.entity;

import com.example.adminreference.config.security.GrantType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_auth_permission")
public class TbAuthPermission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long permissionId;
    @Enumerated(EnumType.STRING)
    private GrantType permissionName;
    @Column(updatable = false, insertable = false)
    private LocalDateTime regDt;
    @Column(updatable = false, insertable = false)
    private LocalDateTime updDt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menuId")
    @ToString.Exclude
    private TbMenuInfo tbMenuInfo;

    @Builder
    public TbAuthPermission(GrantType permissionName, TbMenuInfo tbMenuInfo) {
        this.permissionName = permissionName;
        this.tbMenuInfo = tbMenuInfo;
    }
}
