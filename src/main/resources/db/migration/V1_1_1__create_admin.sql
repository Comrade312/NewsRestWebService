create extension if not exists pgcrypto;

insert into app_user (id, username, password, active)

values (1, 'admin', crypt('123', gen_salt('bf', 8)), true);

insert into user_role (user_id, roles)
values (1, 'ADMIN');