select * from ins_employee where name like '%洪慧%';
select * from ins_user where user_id = 107;

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
and b.status = '0'
and d.FUNC_ID in (40,43,48,49);/*人资及主管*/

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
and b.NAME in ('郑梦',
'廖勇',
'彭敏俐',
'陈小姣',
'郭绢',
'蔡千',
'易旭能',
'易双英',
'谢云',
'涂可',
'邱向红',
'谢莉莎',
'莫金花',
'唐家鸿',
'吴斌',
'黄金娥',
'徐佳',
'蒋丹',
'周承华',
'何雨婷',
'覃敏',
'骆萌',
'唐月',
'方小琴',
'朱建勇',
'贺玲',
'曾银辉',
'刘慧慧',
'黄翠红',
'姚静',
'郎晓晖'
)
  and b.status = '0'
and d.FUNC_ID in (28,29,30,33,35,42,46);/*人资及主管*/

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
and b.NAME in ('洪慧')
  and b.status = '0'
and d.FUNC_ID in (28,29,30,33,35,42,45,47);/*集团人资*/

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
and b.NAME in ('洪慧')
  and b.status = '0'
and d.FUNC_ID in (26,50);/*集团人资*/

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
and b.NAME in ('翟伟')
  and b.status = '0'
and d.FUNC_ID in (37,38,39,41,44);/*学院*/

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
and b.NAME in ('翟伟')
  and b.status = '0'
and d.FUNC_ID in (26,50);/*学院*/