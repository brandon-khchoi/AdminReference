INSERT INTO tb_user_info
       (admin_id, password, username, phone_number, email,  auth_group_id)
VALUES ('master', '$2a$10$w0.sEAcd6CS6Vpf.zurkHeYeLCIPa.u6H2dgVSbrGaaReWvVfxyt6', '마스터', '010-1234-5678', 'master@gmail.com', 0),
       ('user1', '$2a$10$.q2Wi.8vjlwuhC/uuXSPsu0yZ3SINZSWriDxZI7gRJWwedsLojY6K', '유저1', '010-1111-1111', 'user1@gmail.com', 1),
       ('user2', '$2a$10$WZuHR3Tbp/bOpqf0eJbPtu5LkQHZkrhapvOAdJsayg3j/VS7gETzq', '유저2', '010-2222-2222', 'user2@gmail.com', 2),
       ('user3', '$2a$10$9EM7FT.fwy6YBQtqqT8weeFESwWehuIAbWXHCZccaDDTuw8owIZ8O', '유저3', '010-3333-3333', 'user3@gmail.com', 3);


INSERT INTO tb_auth_group
    (auth_group_id, auth_group_name)
VALUES (0, '마스터 관리자'),
       (1, '개발팀'),
       (2, '운영팀'),
       (3, '마케팅팀');

-- 마스터 권한
INSERT INTO tb_auth_group_permission
    (auth_group_id, permission_id)
VALUES (0, 1),
       (0, 2),
       (0, 3),
       (0, 4),
       (0, 5),
       (0, 6),
       (0, 7),
       (0, 8),
       (0, 9),
       (0, 10),
       (0, 11),
       (0, 12),
       (0, 13),
       (0, 14),
       (0, 15),
       (0, 16),
       (0, 17),
       (0, 18),
       (0, 19),
       (0, 20),
       (0, 21),
       (0, 22),
       (0, 23)
;

-- 개발팀 권한
INSERT INTO tb_auth_group_permission
    (auth_group_id, permission_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12);

-- 운영팀 권한
INSERT INTO tb_auth_group_permission
    (auth_group_id, permission_id)
VALUES (2, 2),
       (2, 8),
       (2, 9),
       (2, 10),
       (2, 11),
       (2, 12);

-- 마케팅팀 권한
INSERT INTO tb_auth_group_permission
    (auth_group_id, permission_id)
VALUES (3, 1),
       (3, 3),
       (3, 4),
       (3, 5),
       (3, 6),
       (3, 7);


INSERT INTO tb_auth_permission
    (permission_id, permission_name, menu_id)
VALUES (1, 'GET', 1),
       (2, 'GET', 2),
       (3, 'GET', 11),
       (4, 'POST', 11),
       (5, 'PUT', 11),
       (6, 'DELETE', 11),
       (7, 'EXCEL', 11),
       (8, 'GET', 21),
       (9, 'POST', 21),
       (10, 'PUT', 21),
       (11, 'DELETE', 21),
       (12, 'EXCEL', 21),
       (13, 'GET', 22),
       (14, 'POST', 22),
       (15, 'PUT', 22),
       (16, 'DELETE', 22),
       (17, 'EXCEL', 22),
       (18, 'GET', 3),
       (19, 'GET', 31),
       (20, 'POST', 31),
       (21, 'PUT', 31),
       (22, 'DELETE', 31),
       (23, 'EXCEL', 31)
;

-- 대메뉴
INSERT INTO tb_menu_info
       (menu_id, parent_menu_id, menu_name, menu_url, display_sort, is_menu_use)
VALUES (1, NULL, '대메뉴1', 'menu1', 1, 1),
       (2, NULL, '대메뉴2', 'menu2', 2, 1),
       (3, NULL, '관리', 'admin', 3, 1);


-- 중메뉴
INSERT INTO tb_menu_info
       (menu_id, parent_menu_id, menu_name, menu_url, display_sort, is_menu_use)
VALUES (11, 1, '중메뉴 1-1', 'submenu1', 1, 1),
       (21, 2, '중메뉴 2-1', 'submenu2', 1, 1),
       (22, 2, '중메뉴 2-2', 'submenu3', 2, 1),
       (31, 3, '계정 관리', 'manage', 1, 1);
