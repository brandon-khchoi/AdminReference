package com.example.adminreference.entity;

import com.example.adminreference.common.converter.UseYnConverter;
import com.example.adminreference.enumeration.UseYn;
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
@Table(name = "tb_menu_info")
public class TbMenuInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;
    private Long parentMenuId;
    private String menuName;
    private String menuUrl;
    private int displaySort;
    @Convert(converter = UseYnConverter.class)
    private UseYn isMenuUse;
    @Column(updatable = false, insertable = false)
    private LocalDateTime regDt;
    @Column(updatable = false, insertable = false)
    private LocalDateTime updDt;

    public TbMenuInfo(Long menuId,
                      Long parentMenuId,
                      String menuName,
                      String menuUrl,
                      int displaySort,
                      UseYn isMenuUse) {
        this.menuId = menuId;
        this.parentMenuId = parentMenuId;
        this.menuName = menuName;
        this.menuUrl = menuUrl;
        this.displaySort = displaySort;
        this.isMenuUse = isMenuUse;
        this.regDt = LocalDateTime.now();
        this.updDt = LocalDateTime.now();
    }
}
