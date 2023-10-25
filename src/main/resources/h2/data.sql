INSERT INTO tb_user_info
(admin_id, password, username, department, position, phone_number, email, auth_group_id)
VALUES
    ('master', '$2a$10$w0.sEAcd6CS6Vpf.zurkHeYeLCIPa.u6H2dgVSbrGaaReWvVfxyt6', '브랜든1', '인사팀', '부장', '010-1234-5678','master@gmail.com', 1),
    ('master2', '$2a$10$w0.sEAcd6CS6Vpf.zurkHeYeLCIPa.u6H2dgVSbrGaaReWvVfxyt6', '브랜든2', '인사팀', '부장', '010-1234-5678','master@gmail.com', 1),
    ('master3', '$2a$10$w0.sEAcd6CS6Vpf.zurkHeYeLCIPa.u6H2dgVSbrGaaReWvVfxyt6', '브랜든3', '인사팀', '부장', '010-1234-5678','master@gmail.com', 1),
       ('user1', '$2a$10$.q2Wi.8vjlwuhC/uuXSPsu0yZ3SINZSWriDxZI7gRJWwedsLojY6K', '유저1', '개발팀', '차장', '010-1111-1111', 'user1@gmail.com', 2),
       ('user2', '$2a$10$WZuHR3Tbp/bOpqf0eJbPtu5LkQHZkrhapvOAdJsayg3j/VS7gETzq', '유저2', '기획팀', '매니저', '010-2222-2222', 'user2@gmail.com', 3),
       ('user3', '$2a$10$9EM7FT.fwy6YBQtqqT8weeFESwWehuIAbWXHCZccaDDTuw8owIZ8O', '유저3', '마케팅팀', '매니저', '010-3333-3333', 'user3@gmail.com', 4);


INSERT INTO tb_auth_group
    (auth_group_id, auth_group_name)
VALUES (1, '마스터 관리자'),
       (2, '개발팀'),
       (3, '운영팀'),
       (4, '마케팅팀');

-- 마스터 권한
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
       (1, 12),
       (1, 13),
       (1, 14),
       (1, 15),
       (1, 16),
       (1, 17),
       (1, 18),
       (1, 19),
       (1, 20),
       (1, 21),
       (1, 22),
       (1, 23),
       (1, 24),
       (1, 25),
       (1, 26),
       (1, 27),
       (1, 28)
;

-- 개발팀 권한
INSERT INTO tb_auth_group_permission
    (auth_group_id, permission_id)
VALUES (2, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 7),
       (2, 8),
       (2, 9),
       (2, 10),
       (2, 11),
       (2, 12);

-- 운영팀 권한
INSERT INTO tb_auth_group_permission
    (auth_group_id, permission_id)
VALUES (3, 2),
       (3, 8),
       (3, 9),
       (3, 10),
       (3, 11),
       (3, 12);

-- 마케팅팀 권한
INSERT INTO tb_auth_group_permission
    (auth_group_id, permission_id)
VALUES (4, 1),
       (4, 3),
       (4, 4),
       (4, 5),
       (4, 6),
       (4, 7);


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
       (23, 'EXCEL', 31),
       (24, 'GET', 32),
       (25, 'POST', 32),
       (26, 'PUT', 32),
       (27, 'DELETE', 32),
       (28, 'EXCEL', 32)
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
       (31, 3, '사용자 관리', 'manage', 1, 1),
       (32, 3, '권한 관리', 'auth', 2, 1);
