create table sample_data(
	id serial primary key,
	s_name varchar(255),
	s_description varchar(255),
	created_on timestamp default current_timestamp
);

insert into sample_data(s_name, s_description) values ('Sample Data Name', 'Sample Data Description');