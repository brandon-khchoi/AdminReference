package com.example.adminreference.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@ToString
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TbAuthGroupPermissionId implements Serializable {

    @Column(insertable = false, updatable = false)
    private long permissionId;
    private long authGroupId;

}
