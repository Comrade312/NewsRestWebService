insert into app_user (id, username, password, active)
values (1, 'admin', '123', true),
       (2, 'adminForDelete', '123', true),
       (3, 'adminForEdit', '123', true),
       (4, 'journo1', '123', true),
       (5, 'journo2', '123', true),
       (6, 'sub1', '123', true),
       (7, 'sub2', '123', true);

insert into user_role (user_id, roles)
values (1, 'ADMIN'),
       (2, 'ADMIN'),
       (3, 'ADMIN'),
       (4, 'JOURNALIST'),
       (5, 'JOURNALIST'),
       (6, 'SUBSCRIBER'),
       (7, 'SUBSCRIBER');