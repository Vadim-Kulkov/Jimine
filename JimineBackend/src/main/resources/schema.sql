-- Dictionaries
create table if not exists user_project_role
(
    user_project_role_id   serial primary key,
    user_project_role_name varchar
);
-- create table if not exists user_sex
-- (
--     user_sex_id   serial primary key,
--     user_sex_name varchar
-- );
create table if not exists user_task_role
(
    user_task_role_id   serial primary key,
    user_task_role_name varchar
);
create table if not exists project_status
(
    project_status_id   serial primary key,
    project_status_name varchar
);
create table if not exists task_priority
(
    task_priority_id   serial primary key,
    task_priority_name varchar
);
create table if not exists task_status
(
    task_status_id   serial primary key,
    task_status_name varchar
);
create table if not exists task_type
(
    task_type_id   serial primary key,
    task_type_name varchar
);
create table if not exists role
(
    role_id   serial primary key,
    role_name varchar
);
-- Essential tables
create table if not exists user_entity
(
    user_id       serial primary key,
    user_username varchar,
    user_email    varchar,
    user_password varchar,
    created_at    timestamp default now(),
    updated_at    timestamp default now(),
    deleted_at    timestamp default null
);
create table if not exists user_info
(
    user_id             int8 primary key,
    user_info_firstname varchar,
    user_info_lastname  varchar,
    user_info_surname   varchar,
    user_sex            varchar,
    foreign key (user_id)
        references user_entity (user_id)
--     foreign key (user_sex_id)
--         references user_sex (user_sex_id)
);
create table if not exists project
(
    project_id          serial primary key,
    project_name        varchar,
    created_at          timestamp default now(),
    updated_at          timestamp default now(),
    deleted_at          timestamp default null,
    project_description varchar,
    project_status_id   int8,
    foreign key (project_status_id)
        references project_status (project_status_id)
);
create table if not exists user_project
(
    user_id              int8,
    project_id           int8,
    user_project_role_id int8,
    primary key (user_id, project_id, user_project_role_id),
    foreign key (user_id)
        references user_entity (user_id),
    foreign key (project_id)
        references project (project_id),
    foreign key (user_project_role_id)
        references user_project_role (user_project_role_id)
);
create table if not exists user_role
(
    user_id int8,
    role_id int8,
    primary key (user_id, role_id),
    foreign key (user_id)
        references user_entity (user_id),
    foreign key (role_id)
        references role (role_id)
);
create table if not exists task
(
    task_id          serial primary key,
    task_name        varchar,
    task_description varchar,
    created_at       timestamp default now(),
    updated_at       timestamp default now(),
    deleted_at       timestamp default null,
    task_status_id   int8,
    task_type_id     int8,
    task_priority_id int8,
    project_id       int8,
    foreign key (task_status_id)
        references task_status (task_status_id),
    foreign key (task_type_id)
        references task_type (task_type_id),
    foreign key (task_priority_id)
        references task_priority (task_priority_id),
    foreign key (project_id)
        references project (project_id)
);
create table if not exists user_task
(
    user_id           int8,
    task_id           int8,
    user_task_role_id int8,
    primary key (user_id, task_id, user_task_role_id),
    foreign key (user_id)
        references user_entity (user_id),
    foreign key (task_id)
        references task (task_id),
    foreign key (user_task_role_id)
        references user_task_role (user_task_role_id)
);
create table if not exists task_comment
(
    task_comment_id      serial primary key,
    task_comment_name    varchar,
    task_comment_content varchar,
    created_at           timestamp default now(),
    updated_at           timestamp default now(),
    deleted_at           timestamp default null,
    creator_id           int8,
    task_id              int8,
    foreign key (creator_id)
        references user_entity (user_id),
    foreign key (task_id)
        references task (task_id)
);