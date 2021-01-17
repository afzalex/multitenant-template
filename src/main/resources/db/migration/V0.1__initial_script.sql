CREATE TABLE IF NOT EXISTS public.datasourceconfig (
	id serial primary key,
	driverclassname varchar(255) NULL,
	tenant_name varchar(255) NOT NULL unique,
	url varchar(255) NOT NULL,
	username varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
	is_abstract bool NOT NULL default false,
	is_active bool NOT NULL default true,
	is_local bool NOT NULL default true
);


CREATE SCHEMA IF NOT EXISTS tenant_base;


INSERT INTO datasourceconfig (driverclassname, url, tenant_name, username, password, is_abstract, is_active) VALUES 
	('org.postgresql.Driver', 'jdbc:postgresql://localhost:5432/postgres?currentSchema=tenant_base', 'tenant_base', 'postgres', '123456', true, false);
