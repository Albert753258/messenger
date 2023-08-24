# EasyChat
Мессенджер для android и сервер для пк
# Для создания БД
create database messenger;
\c messenger;
create table accounts(username text, passhash text, sessionhash text, email text, verified bool);
