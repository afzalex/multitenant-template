

drop table if exists create_tenant_schema_audit;
create table create_tenant_schema_audit (
	id serial primary key,
	created_on timestamp default current_timestamp,
	audit_level varchar,
	audit_code varchar,
	audit_summary varchar,
	audit_info json
);

CREATE OR REPLACE FUNCTION create_tenant_schema_function() 
   RETURNS TRIGGER 
   LANGUAGE PLPGSQL
AS $$
DECLARE
	TENANT_BASE_NAME constant varchar := 'tenant_base';
    v_sqlstate text;
    v_message text;
    v_context text;
BEGIN
	insert into create_tenant_schema_audit (audit_level, audit_code, audit_summary)
		values('INFO', 'creation_triggered', 'request to create tenant : ' || NEW.tenant_name);
	
	BEGIN
		perform clone_schema(TENANT_BASE_NAME, NEW.tenant_name);
		insert into create_tenant_schema_audit (audit_level, audit_code, audit_summary)
			values('SUCCESS', 'creation_done', 'Tenant is cloned successfully : ' || NEW.tenant_name);
	exception
	when others then
		GET STACKED DIAGNOSTICS
			v_sqlstate = returned_sqlstate,
			v_message = message_text,
			v_context = pg_exception_context;
		insert into create_tenant_schema_audit (audit_level, audit_code, audit_summary, audit_info)
			values('ERROR', 'creation_failed', 'Unable to create tenant schema : ' || NEW.tenant_name, json_build_object(
				'returned_sqlstate', v_sqlstate,
				'message_text', v_message,
				'pg_exception_context', v_context
			));
	END;
	
	RETURN NEW;
END;
$$;


create trigger create_tenant_schema_trigger after insert on datasourceconfig for each row 
	execute procedure create_tenant_schema_function();
