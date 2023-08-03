insert into users
(user_no, user_id, password, name, join_date, user_group, is_deleted, is_activated)
values
-- password = 1234
('100000', 'admin', '$2a$10$A9mBA/wq/FfzzuLD/xQxnOXZSJVP1bhVWL.GsKwyY8q8iJua3sO/S', 'adminName', '1689890094', 'Admin', 'false', 'true'),

('100001', 'member', '$2a$10$A1HZn/uVytR0m98dYokGNOWHxAuQiMwSGLJi5HHdXh1fQJZ4UVcZu', 'memberName', '1689987600', 'Member', 'false', 'true'),
('100009', 'member2', '$2a$10$A1HZn/uVytR0m98dYokGNOWHxAuQiMwSGLJi5HHdXh1fQJZ4UVcZu', 'memberName', '1689987600', 'Member', 'false', 'true'),

('100002', 'shelter', '$2a$10$A9mBA/wq/FfzzuLD/xQxnOXZSJVP1bhVWL.GsKwyY8q8iJua3sO/S', 'shelterName', '1689890094', 'Shelter', 'false', 'true'),
('100003', 'shelter1', '$2a$10$A9mBA/wq/FfzzuLD/xQxnOXZSJVP1bhVWL.GsKwyY8q8iJua3sO/S', 'shelterName1', '1689890094', 'Shelter', 'false', 'true'),
('100004', 'shelter2', '$2a$10$A9mBA/wq/FfzzuLD/xQxnOXZSJVP1bhVWL.GsKwyY8q8iJua3sO/S', 'shelterName11', '1689890094', 'Shelter', 'false', 'true'),
('100005', 'shelter3', '$2a$10$A9mBA/wq/FfzzuLD/xQxnOXZSJVP1bhVWL.GsKwyY8q8iJua3sO/S', 'shelterName2', '1689890094', 'Shelter', 'false', 'true'),
('100006', 'shelter4', '$2a$10$A9mBA/wq/FfzzuLD/xQxnOXZSJVP1bhVWL.GsKwyY8q8iJua3sO/S', 'shelterName23', '1689890094', 'Shelter', 'false', 'true'),
('100007', 'shelter5', '$2a$10$A9mBA/wq/FfzzuLD/xQxnOXZSJVP1bhVWL.GsKwyY8q8iJua3sO/S', 'shelterName33', '1689890094', 'Shelter', 'false', 'true'),
('100008', 'shelter6', '$2a$10$A9mBA/wq/FfzzuLD/xQxnOXZSJVP1bhVWL.GsKwyY8q8iJua3sO/S', 'shelterName34', '1689890094', 'Shelter', 'false', 'true');

insert into admin
(user_no)
values
('100000');

insert into member
(user_no, holding_token, adopted)
values
('100001', '2', 'false'),
('100009', '2', 'true');

insert into shelter
(user_no, regist_image_path, on_broadcast_title, location)
values
('100002', '/image/path.jpg', 'testTitle', 'test'),
('100003', '/image/path.jpg', null, 'loca'),
('100004', '/image/path.jpg', null, 'locat'),
('100005', '/image/path.jpg', null, 'locati'),
('100006', '/image/path.jpg', null, null),
('100007', '/image/path.jpg', null, null),
('100008', '/image/path.jpg', null, null);

insert into charge
(charge_no, member_no, tid, charge_value, charge_time)
values
('100000', '100001', 't1231231234', '10000', '16849213');

insert into chat
(chat_no, content, shelter_no, user_no, created_time)
values
('100000', '강아지가 정말 이뻐요', '100002', '100001', '16849213');

insert into dog
(dog_no, shelter_no, name, dog_size, gender, weight, age, protection_start_date, adoption_availability, is_deleted)
values
('100000', '100002', '댕댕이1', 'SMALL_SIZE', 'FEMALE', '13', '3', '16849213', 'ADOPT_POSSIBLE', false),
('100001', '100004', '댕댕이2', 'SMALL_SIZE', 'MALE', '17', '5', '16849213', 'ADOPT_POSSIBLE', false),
('100002', '100002', '댕댕이3', 'BIG_SIZE', 'FEMALE', '30', '1', '16849213', 'ADOPT_POSSIBLE', false),
('100003', '100003', '댕댕이4', 'SMALL_SIZE', 'FEMALE', '30', '1', '16849213', 'ADOPT_POSSIBLE', true);

insert into board
(board_no, member_no, title, content, created_time)
values
('100', '100009', '입양후기 제목1', '입양후기 내용1', '16849213'),
('200', '100009', '입양후기 제목2', '입양후기 내용2', '16849213');

insert into reply
(reply_no, user_no, board_no, content, created_time)
values
('100', '100001', '100', '입양후기 댓글입니다.', '16849213'),
('101', '100002', '100', '보호소의 입양후기 댓글입니다.', '16849213'),
('102', '100001', '200', '200번글의 댓글입니다.', '16849213');

