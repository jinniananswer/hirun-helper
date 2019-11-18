truncate table ins.ins_user_func;

/** 家装顾问菜单权限  **/
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
and c.JOB_ROLE = 42
and d.FUNC_ID in (1,3,4,5,17,18,20,22,23);

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

from ins.ins_user a, sys.sys_menu d
where a.USER_ID = 162
and d.FUNC_ID in (1,3,4,5,17,18,20,22,23);

/** 区域经理菜单权限  **/
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
and c.JOB_ROLE = 58
and d.FUNC_ID in (1,2,3,4,5,17,18,20,22,23,13,14,15,16);

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
and b.EMPLOYEE_ID = 246
and a.USER_ID = 246
and d.FUNC_ID in (1,2,3,4,5,17,18,20,22,23,13,14,15,16);

/** 市场部主管权限  **/
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
and b.EMPLOYEE_ID in (244,623,461,542)
and d.FUNC_ID in (1,2,3,6,7,8,9,11,12,13,14,15,16,21);

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
and b.EMPLOYEE_ID in (159)
and d.FUNC_ID in (1,2,3,6,7,8,9,11,12,13,14,15,16,21);

1,2,3,6,7,8,9,11,12,13,14,15,16,21,24,25;

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
from ins.ins_user a, ins.ins_employee b, sys.sys_func d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in  (244,623,461,542)
and d.FUNC_ID in (24);


select * from ins.ins_user_func t where t.USER_ID in (244,623,461,542,159) and t.FUNC_ID = 25;

/** 店面总经理  **/
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
and b.EMPLOYEE_ID in (243,622,460,541, 60)
and d.FUNC_ID in (1,2,3,6,7,8,9,11,12,13,16,21);

/** 店面总经理  **/
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

from ins.ins_user a, ins.ins_employee b, sys.sys_func d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (243,622,460,541, 60)
and d.FUNC_ID in (24,25);

/** 分公司市场部经理  **/
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
and b.EMPLOYEE_ID in (150)
and d.FUNC_ID in (1,2,3,6,7,8,9,11,12,13,16,21);

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

from ins.ins_user a, ins.ins_employee b, sys.sys_func d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (150)
and d.FUNC_ID in (25,27);

/** 分公司总经理  **/
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
and b.EMPLOYEE_ID in (148)
and d.FUNC_ID in (1,2,3,6,7,8,9,11,12,13,16,21);

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

from ins.ins_user a, ins.ins_employee b, sys.sys_func d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (148)
and d.FUNC_ID in (25,27);

/** 家装事业部市场部经理  **/
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
and b.EMPLOYEE_ID in (115)
and d.FUNC_ID in (1,2,3,6,7,8,9,11,12,13,16,21);

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

from ins.ins_user a, ins.ins_employee b, sys.sys_func d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (115)
and d.FUNC_ID in (25,27);

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
and b.EMPLOYEE_ID in (118)
and d.FUNC_ID in (1,2,3,6,7,8,9,11,12,13,16,21);

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

from ins.ins_user a, ins.ins_employee b, sys.sys_func d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (118)
and d.FUNC_ID in (25,27);

/** 集团总部市场总监  **/
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
and b.EMPLOYEE_ID in (73)
and d.FUNC_ID in (1,2,3,6,7,8,9,11,12,13,16,21);

/** 集团总裁  **/
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
and b.EMPLOYEE_ID in (72)
and d.FUNC_ID in (1,2,3,6,7,8,9,11,12,13,16,21);


/**楼盘规划修改**/
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

from ins.ins_user a, ins.ins_employee b, sys.sys_func d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (115)
and d.FUNC_ID in (24);

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

from ins.ins_user a, ins.ins_employee b, sys.sys_func d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (115)
and d.FUNC_ID in (26,27);


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

from ins.ins_user a, ins.ins_employee b, sys.sys_func d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (235)
and d.FUNC_ID in (1,6,7,24,25,26,27);

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

from ins.ins_user a, ins.ins_employee b, sys.sys_func d
where b.USER_ID = a.USER_ID
and b.EMPLOYEE_ID in (235)
and d.FUNC_ID in (28,29,30);