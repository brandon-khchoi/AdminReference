DROP TABLE IF EXISTS tb_user_info;
DROP TABLE IF EXISTS tb_auth_group;
DROP TABLE IF EXISTS tb_auth_group_permission;
DROP TABLE IF EXISTS tb_auth_permission;
DROP TABLE IF EXISTS tb_menu_info;
DROP TABLE IF EXISTS sys_shop_mgt;

CREATE TABLE `tb_user_info`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT COMMENT '사용자 정보 ID',
    `admin_id`      varchar(10)  NOT NULL COMMENT '어드민 계정',
    `password`      varchar(100) NOT NULL COMMENT '어드민 계정 비밀번호',
    `username`      varchar(20)  NOT NULL COMMENT '사용자 명',
    `department`    varchar(30)  NOT NULL COMMENT '소속 명',
    `position`      varchar(20)  NOT NULL COMMENT '직급 명',
    `phone_number`  varchar(20)  NOT NULL COMMENT '핸드폰 번호',
    `email`         varchar(50)  NOT NULL COMMENT '이메일 주소',
    `auth_group_id` bigint                DEFAULT NULL COMMENT '권한 그룹 ID',
    `last_login_dt` datetime              DEFAULT NULL COMMENT '최근 접속 일시',
    `reg_dt`        datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
    `upd_dt`        datetime              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    PRIMARY KEY (`id`)
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

CREATE TABLE `sys_shop_mgt`
(
    `shop_id`                  int         NOT NULL AUTO_INCREMENT COMMENT '쇼핑몰(스토어) ID',
    `shop_nm`                  varchar(40) NOT NULL COMMENT '쇼핑몰 명',
    `member_id`                varchar(50) NOT NULL COMMENT '회원 아이디',
    `description`              varchar(100)         DEFAULT NULL COMMENT '쇼핑몰 설명',
    `pc_url`                   varchar(100)         DEFAULT NULL COMMENT 'PC URL',
    `mo_url`                   varchar(100)         DEFAULT NULL COMMENT 'Mobile URL',
    `main_img_s3_path`         varchar(200)         DEFAULT NULL COMMENT '스토어홈 메인 이미지 S3 버킷 경로',
    `logo_img_s3_path`         varchar(200)         DEFAULT NULL COMMENT '로고 이미지 S3 버킷 경로',
    `share_img_s3_path`        varchar(200)         DEFAULT NULL COMMENT '공유하기 이미지 S3 버킷 경로',
    `status_cd`                smallint    NOT NULL DEFAULT '1001' COMMENT '서비스 상태 코드 (code_grp_id:102)',
    `svc_open_dt`              date                 DEFAULT NULL COMMENT '서비스 오픈 일자',
    `tags`                     varchar(100)         DEFAULT NULL COMMENT '추천 그룹명 ( 개인화 )',
    `smart_store_shop_nm`      varchar(50)          DEFAULT NULL COMMENT '스마트스토어 스토어 명',
    `smart_store_prd_base_url` varchar(200)         DEFAULT NULL COMMENT '스마트 스토어 상품 기본 URL',
    `shop_sort`                int                  DEFAULT NULL COMMENT '스토어 정렬 순서',
    `reg_id`                   int         NOT NULL COMMENT '등록자 ID',
    `reg_dt`                   datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일시',
    `upd_id`                   int                  DEFAULT NULL COMMENT '수정자 ID',
    `upd_dt`                   datetime             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    PRIMARY KEY (`shop_id`)
);

