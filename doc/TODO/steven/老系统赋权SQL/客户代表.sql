truncate table ins.ins_user_func;

/** 客户代表、组长、首席客户经理、客户经理菜单权限  **/
insert into ins.ins_user_func(
user_id,
func_id,
start_date,
end_date,
status,
create_user_id,
create_date,
update_user_id,
update_time
)
select
  a.USER_ID,
  d.FUNC_ID,
  sysdate(),
  '3000-12-31 23:59:59',
  '0',
  1,
  sysdate(),
  1,
  sysdate()

from ins.ins_user a, ins.ins_employee b, ins.ins_employee_job_role c, sys.sys_menu d
where c.EMPLOYEE_ID = b.EMPLOYEE_ID
and b.USER_ID = a.USER_ID
and c.JOB_ROLE in(46,69,119,118)
and d.FUNC_ID in (1,2,52,53,54);




/** 客户部主管,客户经理权限  **/
insert into ins.ins_user_func(
user_id,
func_id,
start_date,
end_date,
status,
create_user_id,
create_date,
update_user_id,
update_time
)
select
  a.USER_ID,
  d.FUNC_ID,
  sysdate(),
  '3000-12-31 23:59:59',
  '0',
  1,
  sysdate(),
  1,
  sysdate()

from ins.ins_user a, ins.ins_employee b, sys.sys_menu d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (472,546,773,922,920,901,993,1139,1145,755,1311,1255,778,911,542,952,65,201)
and d.FUNC_ID in (1,2,3,53,54,55,56,57);


/** 店面经理权限  **/
insert into ins.ins_user_func(
user_id,
func_id,
start_date,
end_date,
status,
create_user_id,
create_date,
update_user_id,
update_time
)
select
  a.USER_ID,
  d.FUNC_ID,
  sysdate(),
  '3000-12-31 23:59:59',
  '0',
  1,
  sysdate(),
  1,
  sysdate()

from ins.ins_user a, ins.ins_employee b, sys.sys_menu d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (472,546,773,922,920,901,993,1139,1145,755,1311,1255,778,911,542,952,65)
and d.FUNC_ID in (2,53,54);

/** 店面经理权限  **/
insert into ins.ins_user_func(
user_id,
func_id,
start_date,
end_date,
status,
create_user_id,
create_date,
update_user_id,
update_time
)
select
  a.USER_ID,
  d.FUNC_ID,
  sysdate(),
  '3000-12-31 23:59:59',
  '0',
  1,
  sysdate(),
  1,
  sysdate()

from ins.ins_user a, ins.ins_employee b, sys.sys_menu d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (694 469 1197 817 1042 1197,243,460,541,622,710,768,776,784,777,779,345
,895,1131,1199,1251,1285,1198,1040,200,171,129,1061,60,1197,1578,769,1548,1478
,1324,623,1147,948,1764,1871,1888,1870,1138,60,115,148,460,767,1196,1478,948)
and d.FUNC_ID in (2,53,54);


/** 总经理权限  **/
insert into ins.ins_user_func(
user_id,
func_id,
start_date,
end_date,
status,
create_user_id,
create_date,
update_user_id,
update_time
)
select
  a.USER_ID,
  d.FUNC_ID,
  sysdate(),
  '3000-12-31 23:59:59',
  '0',
  1,
  sysdate(),
  1,
  sysdate()

from ins.ins_user a, ins.ins_employee b, sys.sys_menu d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (150,118,767,345,1196,148,129)
and d.FUNC_ID in (2,53,54);


/** 客户部专员权限  **/
insert into ins.ins_user_func(
user_id,
func_id,
start_date,
end_date,
status,
create_user_id,
create_date,
update_user_id,
update_time
)
select
  a.USER_ID,
  d.FUNC_ID,
  sysdate(),
  '3000-12-31 23:59:59',
  '0',
  1,
  sysdate(),
  1,
  sysdate()

from ins.ins_user a, ins.ins_employee b, sys.sys_menu d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (361)
and d.FUNC_ID in (2,53,54);