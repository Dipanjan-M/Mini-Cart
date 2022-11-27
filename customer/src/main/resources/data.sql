insert into roles(role_name) values('ADMIN');
insert into roles(role_name) values('CUSTOMER');

insert into customers(first_name, last_name, email, password, role_id) values('System', 'Admin', 'admin@email.com', '$2a$12$9a1dEoLCp5LK4pbKnJsUUe4.VYqt7uCZeqgLZCKTgKIXNrEmDTEkG', 1);
insert into customers(first_name, last_name, email, password, role_id) values('Test', 'User', 'test-user@email.com', '$2a$12$EE/FXVs9LDZ.Uu52Hll2Ae/8Lf3lXJ4kW6druInHhi/NjygoRaTOa', 2);
