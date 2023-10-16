DROP TABLE IF EXISTS tb_user_info;
DROP TABLE IF EXISTS tb_auth_group;
DROP TABLE IF EXISTS tb_auth_group_permission;
DROP TABLE IF EXISTS tb_auth_permission;
DROP TABLE IF EXISTS tb_menu_info;

CREATE TABLE `tb_user_info`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT COMMENT '사용자 정보 ID',
    `admin_id`      varchar(10)  NOT NULL DEFAULT NULL COMMENT '어드민 계정',
    `password`      varchar(100) NOT NULL COMMENT '어드민 계정 비밀번호',
    `username`     varchar(20)  NOT NULL COMMENT '루나 영어 이름',
    `phone_number`  varchar(20)  NOT NULL COMMENT '핸드폰 번호',
    `email`         varchar(50)  NOT NULL COMMENT '이메일 주소',
    `auth_group_id` bigint                DEFAULT NULL COMMENT '권한 그룹 ID',
    `last_login_dt` datetime              DEFAULT NULL COMMENT '최근 접속 일시',
    `reg_dt`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
    `upd_dt`        datetime              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    PRIMARY KEY (`admin_id`)
);

CREATE TABLE `tb_auth_group`
(
    `auth_group_id`   bigint       NOT NULL AUTO_INCREMENT COMMENT '권한 그룹 ID',
    `auth_group_name` varchar(100) NOT NULL COMMENT '권한 그룹 명',
    `reg_dt`          datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
    `upd_dt`          datetime              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    PRIMARY KEY (`auth_group_id`)
);

CREATE TABLE `tb_auth_group_permission`
(
    `auth_group_id` bigint   NOT NULL COMMENT '권한 그룹 ID',
    `permission_id` bigint   NOT NULL COMMENT '권한 ID',
    `reg_dt`        datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
    PRIMARY KEY (`auth_group_id`, `permission_id`)
);


CREATE TABLE `tb_auth_permission`
(
    `permission_id`   bigint      NOT NULL AUTO_INCREMENT COMMENT '권한 ID',
    `permission_name` varchar(20) NOT NULL COMMENT '권한명',
    `menu_id`         bigint      NOT NULL COMMENT '메뉴 ID',
    `reg_dt`          datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
    `upd_dt`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    PRIMARY KEY (`permission_id`)
);

CREATE TABLE `tb_menu_info`
(
    `menu_id`        bigint       NOT NULL AUTO_INCREMENT COMMENT '메뉴 ID',
    `parent_menu_id` bigint                DEFAULT '0' COMMENT '상위 메뉴 ID',
    `menu_name`      varchar(100) NOT NULL COMMENT '메뉴명',
    `menu_url`       varchar(100) NOT NULL COMMENT '메뉴 URL',
    `display_sort`   tinyint               DEFAULT NULL COMMENT '메뉴 노출 순서',
    `is_menu_use`    tinyint      NOT NULL DEFAULT '0' COMMENT '메뉴 사용 여부 (미사용:N. 사용:Y)',
    `reg_dt`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
    `upd_dt`         datetime              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    PRIMARY KEY (`menu_id`)
);
