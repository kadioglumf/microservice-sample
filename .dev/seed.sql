INSERT INTO public.credentials
(id, create_by_user_id, creation_date, is_deleted, last_update_date, update_by_user_id, created_by_ip_addr, origin, origin_ip_addr, updated_by_ip_addr, user_agent, activity, email, gender, mobile, name, nationality, password, provider, surname)
VALUES
    ( nextval('seq_credentials_id'), null, now(), false, now(), null, null, null, null, null, null, 'ACTIVE',  'admin@mail.com', 'male', null, 'admin', null, '{bcrypt}$2a$10$U0Jtt9gcX7yg.uEtStQrUeUORUaNmbMNBSx8v0N/CPH0RLbWy9Nga', 'LOCAL', null),
    ( nextval('seq_credentials_id'), null, now(), false, now(), null, null, null, null, null, null, 'ACTIVE',  'user@mail.com', 'male', null, 'user', null, '{bcrypt}$2a$10$Vw4DzvSRPq7uEs/uV4pH3uF3UIvxH.p0Aq0HtQpM1V/YOp0/5fxyK', 'LOCAL', null),
    ( nextval('seq_credentials_id'), null, now(), false, now(), null, null, null, null, null, null, 'ACTIVE',  'user2@mail.com', 'male', null, 'user2', null, '{bcrypt}$2a$10$Vw4DzvSRPq7uEs/uV4pH3uF3UIvxH.p0Aq0HtQpM1V/YOp0/5fxyK', 'LOCAL', null),
    ( nextval('seq_credentials_id'), null, now(), false, now(), null, null, null, null, null, null, 'ACTIVE',  'user3@mail.com', 'male', null, 'user3', null, '{bcrypt}$2a$10$Vw4DzvSRPq7uEs/uV4pH3uF3UIvxH.p0Aq0HtQpM1V/YOp0/5fxyK', 'LOCAL', null),
    ( nextval('seq_credentials_id'), null, now(), false, now(), null, null, null, null, null, null, 'ACTIVE',  'api-communication', 'male', null, 'api-communication', null, null, 'LOCAL', null),
( nextval('seq_credentials_id'), null, now(), false, now(), null, null, null, null, null, null, 'ACTIVE',  'mod@mail.com', 'male', null, 'mod', null, '{bcrypt}$2a$10$Vw4DzvSRPq7uEs/uV4pH3uF3UIvxH.p0Aq0HtQpM1V/YOp0/5fxyK', 'LOCAL', null);


INSERT INTO public.credentials_roles(credential_id, role) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.credentials_roles(credential_id, role) VALUES (1, 'ROLE_MODERATOR');
INSERT INTO public.credentials_roles(credential_id, role) VALUES (1, 'ROLE_USER');
INSERT INTO public.credentials_roles(credential_id, role) VALUES (2, 'ROLE_USER');
INSERT INTO public.credentials_roles(credential_id, role) VALUES (3, 'ROLE_USER');
INSERT INTO public.credentials_roles(credential_id, role) VALUES (4, 'ROLE_USER');
INSERT INTO public.credentials_roles(credential_id, role) VALUES (5, 'ROLE_SYSTEM');
INSERT INTO public.credentials_roles(credential_id, role) VALUES (6, 'ROLE_MODERATOR');

INSERT INTO public.credentials_users(id, create_by_user_id, creation_date, is_deleted, last_update_date, update_by_user_id, created_by_ip_addr, origin, origin_ip_addr, updated_by_ip_addr, user_agent, birth_day, user_address_id, credential_id)
VALUES
    ( nextval('seq_credentials_users_id'), null, now(), false, now(), null, null, null, null, null, null, null,  null, 2);

INSERT INTO public.credentials_users(id, create_by_user_id, creation_date, is_deleted, last_update_date, update_by_user_id, created_by_ip_addr, origin, origin_ip_addr, updated_by_ip_addr, user_agent, birth_day, user_address_id, credential_id)
VALUES
    ( nextval('seq_credentials_users_id'), null, now(), false, now(), null, null, null, null, null, null, null,  null, 3);

INSERT INTO public.credentials_users(id, create_by_user_id, creation_date, is_deleted, last_update_date, update_by_user_id, created_by_ip_addr, origin, origin_ip_addr, updated_by_ip_addr, user_agent, birth_day, user_address_id, credential_id)
VALUES
    ( nextval('seq_credentials_users_id'), null, now(), false, now(), null, null, null, null, null, null, null,  null, 4);



INSERT INTO public.permissions (id, create_by_user_id, creation_date, is_deleted, last_update_date, update_by_user_id, created_by_ip_addr, origin, origin_ip_addr, updated_by_ip_addr, user_agent, authorization_name, permission)
VALUES
    (1, null, now(), false, now(), null, null, null, null, null, null, 'Contact Us Read Yetkisi', 'data.contact-us.read'),
    (2, null, now(), false, now(), null, null, null, null, null, null, 'Contact Us Write Yetkisi', 'data.contact-us.write'),
    (3, null, now(), false, now(), null, null, null, null, null, null, 'Contact Us Tüm Yetkiler', 'data.contact-us.all');


INSERT INTO public.credentials_permissions (credential_id, permission_id) VALUES (2, 1);
INSERT INTO public.credentials_permissions (credential_id, permission_id) VALUES (2, 2);
INSERT INTO public.credentials_permissions (credential_id, permission_id) VALUES (2, 3);





-- websocket-service

INSERT INTO public.channel (id, name, creation_date, last_update_date) VALUES (1, 'test-admin-channel', now(), now());
INSERT INTO public.channel (id, name, creation_date, last_update_date) VALUES (2, 'test-user-channel', now(), now());

INSERT INTO public.channel_roles (channel_id, roles) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.channel_roles (channel_id, roles) VALUES (2, 'ROLE_USER');