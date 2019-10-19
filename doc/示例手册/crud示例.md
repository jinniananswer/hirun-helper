# 查询

#### 全表查询 
```SQL
SELECT ... FROM user
```
```Java
public void selectAll() {
    List<User> userList = userMapper.selectList(null);
    userList.forEach(System.out::println);
}
```

#### 根据主键 ID 查询
```SQL
SELECT ... FROM user WHERE id = 123123131231
```

```Java
public void selectById() {
    User user = userMapper.selectById(123123131231L);
}
```

#### 根据多个 ID 查询
```SQL
SELECT ... FROM user WHERE id IN (1111111, 222222, 3333333)
```

```Java
public void selectByIds() {
    List<Long> idsList = Arrays.asList(1111111L, 222222L, 3333333L);
    List<User> userList = userMapper.selectBatchIds(idsList);
    userList.forEach(System.out::println);
}
```

#### 通过 Map 传入查询条件参数
```SQL
SELECT id, name, age, email, manager_id, create_time 
  FROM user 
 WHERE name = '林子' AND age = 29
```
```Java
public void selectByMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", "林子"); // 注意: key 是数据库中的列名，不是实体类的属性名!
    map.put("age", 29);
    List<User> userList = userMapper.selectByMap(map);
}
```

#### 通过条件构造器
```SQL
SELECT id, name, age, email, manager_id, create_time 
  FROM user 
 WHERE name like %周% AND age < 40
```

```Java
public void selectByWrapper1() {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.like("name", "周").lt("age", 40);
    List<User> userList = userMapper.selectList(queryWrapper);
}

public void selectByWrapper2() {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.like("name", "周").lt("age", 40);
    List<User> userList = userMapper.selectList(queryWrapper);
}
```

#### 通过条件构造器
```SQL
SELECT id, name, age, email, manager_id, create_time 
  FROM user 
 WHERE name like %周% AND age BETWEEN 20 AND 40 AND email IS NOT NULL
```
```Java
public void selectByWrapper2() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.like("name", "周").between("age", 20, 40).isNotNull("email");
	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```
#### 通过条件构造器
```SQL
SELECT id, name, age, email, manager_id, create_time 
  FROM user 
 WHERE name like 王% OR age >= 25 ORDER BY age DESC, id ASC
```

```Java
public void selectByWrapper3() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.likeRight("name", "王").or().ge("age", 25).orderByDesc("age").orderByAsc("id");
	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 通过条件构造器
```SQL 
SELECT id, name, age, email, manager_id, create_time
 FROM user
WHERE date_format(create_time, '%Y-%m-%d') = '2019-02-14'
  AND manager_id IN (SELECT id FROM user WHERE name LIKE '王%')
```

```Java
public void selectByWrapper4() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	// {0} 可以防止 SQL 注入风险。
	queryWrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", "2019-02-14").inSql("manager_id", "SELECT id FROM user WHERE name LIKE '王%'");
	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 通过条件构造器
```SQL
SELECT id, name, age, email, manager_id, create_time FROM user
 WHERE name LIKE '王%' AND (age < 40 OR email IS NOT NULL)
```

```Java
public void selectByWrapper5() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.likeRight("name", "王").and(qw -> qw.lt("age", 40).or().isNotNull("email"));
	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 通过条件构造器
```SQL
SELECT id, name, age, email, manager_id, create_time FROM user
 WHERE name LIKE '王%' OR (age < 40 AND age >20 AND email IS NOT NULL)
```

```Java
public void selectByWrapper6() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.likeRight("name", "王").or(qw -> qw.lt("age", 40).gt("age", 20).isNotNull("email"));
	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 通过条件构造器
```SQL
SELECT id, name, age, email, manager_id, create_time FROM user
 WHERE (age < 40 OR email IS NOT NULL)
   AND name LIKE '王%'
```
```Java
public void selectByWrapper7() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.nested(qw -> qw.lt("age", 40).or().isNotNull("email")).likeRight("name", "王");
	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 通过条件构造器
```SQL
SELECT id, name, age, email, manager_id, create_time FROM user WHERE age IN (30,31,34,35)
```

```Java
public void selectByWrapper8() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.in("age", Arrays.asList(30, 31, 34, 35));
	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 通过条件构造器，限定查询数据条数 LIMIT，注意：这里要慎重使用，有 SQL 注入风险！
```SQL
SELECT id, name, age, email, manager_id, create_time FROM user WHERE age IN (30,31,34,35) LIMIT 1
```

```Java
public void selectByWrapper9() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.in("age", Arrays.asList(30, 31, 34, 35)).last("LIMIT 1");
	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 通过条件构造器，指定需要的列
```SQL
SELECT id, name FROM user WHERE name LIKE '雨%' age < 40
```

```Java
public void selectByWrapperWithColumn() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.select("id", "name").likeRight("name", "雨").lt("age", 40);
	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 通过条件构造器，排除特定的列
```SQL
SELECT id, name, age, email FROM user WHERE name LIKE '雨%' age < 40
```

```Java
public void selectByWrapperWithoutColumn() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.select(User.class, info -> !info.getColumn().equals("create_time") && !info.getColumn().equals("manager_id"))
		.likeRight("name", "雨").lt("age", 40);
	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 动态条件，类似于 SQLParser
```Java
public void selectByCondition(String name, String email) {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.eq(StringUtils.isNotEmpty(name), "name", name);    // 当 name 有值时才会加到 查询条件中。
	queryWrapper.eq(StringUtils.isNotEmpty(email), "email", email); // 当 email 有值时才会加到 查询条件中。
	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 实体作为条件构造器，类似于 SQLParser
```SQL
SELECT id, name, age, email, manager_id, create_time FROM user WHERE name = '刘玉红' AND age = 32
```

```Java
public void selectByWrapperEntity() {
	User whereUser = new User();
	whereUser.setName("刘玉红");
	whereUser.setAge(32);

	QueryWrapper<User> queryWrapper = new QueryWrapper<>(whereUser);
	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### Map 作为条件构造器
```SQL
SELECT id, name, age, email, manager_id, create_time FROM user WHERE name = '刘玉红' AND age = 25 AND email IS NULL
```

```Java
public void selectByWrapperAllEq() {
	Map<String, Object> params = new HashMap<>();
	params.put("name", "刘玉红");
	params.put("age", 25);
	params.put("email", null);

	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.allEq(params);

	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### Map 作为条件构造器，null 值自动忽略
```SQL
SELECT id, name, age, email, manager_id, create_time 
  FROM user WHERE name = '刘玉红' AND age = 25
```

```Java
public void selectByWrapperAllEq() {
	Map<String, Object> params = new HashMap<>();
	params.put("name", "刘玉红");
	params.put("age", 25);
	params.put("email", null);

	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.allEq(params, false);

	List<User> list = userMapper.selectList(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 返回 List<Map> 结果集
##### 应用场景: 当表字段特别多的时候，你只需要查询其中的少数几列，没必要返回泛型为实体的类，因为绝大部分的字段都是 null，这样不优雅。
```SQL
SELECT id,name FROM user WHERE name like '王%' AND age < 50
```

```Java
public void selectByWrapperMaps() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.select("id", "name").likeRight("name", "王").lt("age", 50);
	List<Map<String, Object>> list = userMapper.selectMaps(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 返回 List<Map> 结果集
##### 应用场景: 当查询的不是表里的某一条记录，而是一个统计数据时。
```SQL
SELECT avg(age) avg_age, min(age) min_age, max(age) max_age FROM user GROUP BY sum(age) < {0} HAVING sum(age) < 500
```

```Java
public void selectByWrapperMaps() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.select("avg(age) avg_age", "min(age) min_age", "max(age) max_age")
		.groupBy("manager_id")
		.having("sum(age) < {0}", 500);
	List<Map<String, Object>> list = userMapper.selectMaps(queryWrapper);
	list.forEach(System.out::println);
}
```

#### 返回 List<Map> 结果集
##### 应用场景: 不论 select 语句选择了几列，返回的 Object 只有第一列的数据。只返回一列的时候可以考虑用它。
```SQL
SELECT id,name FROM user WHERE name like '王%' AND age < 50
```

```Java
public void selectByWrapperObjs() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.select("id", "name").likeRight("name", "王").lt("age", 50);
	List<Object> list = userMapper.selectObjs(queryWrapper);
	list.forEach(System.out::println);
}
```

```SQL
SELECT COUNT(1) FROM user WHERE name like '王%' AND age < 50
```

```Java
public void selectByWrapperCount() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.likeRight("name", "王").lt("age", 50);
	Integer count = userMapper.selectCount(queryWrapper);
	System.out.println("记录数: " + count);
}
```

#### 如果查询出多条会直接报错: TooManyResultsException
```SQL
SELECT id, name, age, email, manager_id, create_time FROM user WHERE name = '刘玉红' AND age = 50
```

```Java
public void selectByWrapperOne() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.likeRight("name", "刘玉红").lt("age", 50);
	User user = userMapper.selectOne(queryWrapper);
	System.out.println(user);
}
```

#### 基于 Lambda 表达式的条件构造器，这是 MP 最出彩的部分，应重点掌握！
#### 优势: 可以防止列名误写。
```SQL
SELECT id, name, age, email, manager_id, create_time FROM user WHERE name LIKE '刘玉红%' AND age < 25
```

```Java
public void selectLambda() {
	LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery();
	lambdaQueryWrapper.likeRight(User::getName, "刘玉红").lt(User::getAge, 25);
	List<User> users = userMapper.selectList(lambdaQueryWrapper);
	users.forEach(System.out::println);
}
```

#### 基于 Lambda 表达式的条件构造器，带括号的
#### 优势: 可以防止列名误写。
```SQL
SELECT id, name, age, email, manager_id, create_time FROM user WHERE name LIKE '%雨%' AND (age < 40 OR email IS NOT NULL)
```

```Java
public void selectLambda() {
	LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery();
	lambdaQueryWrapper.like(User::getName, "雨")
		.and(lqw -> lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));
	List<User> users = userMapper.selectList(lambdaQueryWrapper);
	users.forEach(System.out::println);
}
```

#### 分页查询
```Java
@EnableTransactionManagement
@Configuration
@MapperScan("com.hitech.skeleton.**.mapper")
public class MybatisPlusConfig {

	/**
	 * 分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		return paginationInterceptor;
	}
}
```

#### 分页查询
```SQL
SQL1: SELECT COUNT(1) FROM user WHERE age >= 26
SQL2: SELECT id, name, age, email, manager_id, create_time 
        FROM user WHERE age >= 26 LIMIT ?,?
```

```Java
public void selectPage() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.ge("age", 26);
	Page<User> page = new Page<>(1, 2);
	IPage<User> iPage = userMapper.selectPage(page, queryWrapper);
	List<User> users =  iPage.getRecords();
	System.out.println("总页数: " + iPage.getPages());
	System.out.println("总记录数: " + iPage.getTotal());
	users.forEach(System.out::println);
}
```

#### 分页查询 返回 Map
```SQL
SQL1: SELECT COUNT(1) FROM user WHERE age >= 26
SQL2: SELECT id, name, age, email, manager_id, create_time 
        FROM user WHERE age >= 26 LIMIT ?,?
```

```Java
public void selectMapPage() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.ge("age", 26);
	Page<User> page = new Page<>(1, 2);
	IPage<Map<String, Object>> iPage = userMapper.selectMapsPage(page, queryWrapper);
	System.out.println("总页数: " + iPage.getPages());
	System.out.println("总记录数: " + iPage.getTotal());
	List<Map<String, Object>> users =  iPage.getRecords();
	users.forEach(System.out::println);
}
```

#### 分页查询 不返回总记录数，有些场景其实并不需要返回总记录数，消耗太大。因此只会运行有一条 SQL
```SQL
SQL1:  SELECT id, name, age, email, manager_id, create_time FROM user WHERE age >= 26 LIMIT ?,?
```

```Java
public void selectPageWithoutCount() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.ge("age", 26);
	Page<User> page = new Page<>(1, 2, false); // 第三个参数: isSearchCount=false, 无需返回总记录数
	IPage<User> iPage = userMapper.selectPage(page, queryWrapper);
	System.out.println("总页数: " + iPage.getPages());
	System.out.println("总记录数: " + iPage.getTotal());
	List<User> users =  iPage.getRecords();
	users.forEach(System.out::println);
}
```

#### 多表关联的分页查询，这里演示的是单表，多表是一样的，关键是写好 Xml 文件里的 SQL
```Java
// UserMapper.java 中增加接口
@Mapper
public interface UserMapper extends BaseMapper<User> {
    IPage<User> selectUserPage(Page<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);
}

// UserMapper.xml 中增加接口语句配置
<select id="selectUserPage" resultType="com.hitech.skeleton.modules.demo.entity.po.User" >
    SELECT * FROM user ${ew.customSqlSegment}
</select>
```

#### 分页查询 不返回总记录数，有些场景其实并不需要返回总记录数，消耗太大。因此只会运行有一条 SQL
```SQL
SQL1:  SELECT COUNT(1) FROM user WHERE age >= 26
SQL2:  SELECT * FROM user WHERE age >= 26 LIMIT ?,?
```

```Java
public void selectPageWithoutCount() {
	QueryWrapper<User> queryWrapper = new QueryWrapper<>();
	queryWrapper.ge("age", 26);
	Page<User> page = new Page<>(1, 2);
	IPage<User> iPage = userMapper.selectUserPage(page, queryWrapper); // 自定义语句
	System.out.println("总页数: " + iPage.getPages());
	System.out.println("总记录数: " + iPage.getTotal());
	List<User> users =  iPage.getRecords();
	users.forEach(System.out::println);
}
```

#### 多表关联 返回扁平化 List<Map<String, Object>>
```java
@Select(
	"SELECT e.exam_id, e.name, e.pass_score, e.times, s.score, s.employee_id, s.exam_time\n" +
		" FROM ins_exam e, ins_exam_score s \n" +
		"WHERE e.`EXAM_ID` = s.`EXAM_ID`\n" +
		"  AND e.exam_id = #{examId}")
List<Map<String, Object>> queryByExamId(Integer examId);
```

#### 多表关联 返回扁平化数据对象 List<T>
```java
@Select(
	"SELECT e.exam_id, e.name, e.pass_score, e.times, s.score, s.employee_id, s.exam_time\n" +
		" FROM ins_exam e, ins_exam_score s \n" +
		"WHERE e.`EXAM_ID` = s.`EXAM_ID`\n" +
		"  AND e.exam_id = #{examId}")
@Results(
	value = {
		@Result(property = "examId", column = "EXAM_ID"),
		@Result(property = "name", column = "NAME"),
		@Result(property = "passScore", column = "PASS_SCORE"),
		@Result(property = "times", column = "TIMES"),
		@Result(property = "employeeId", column = "EMPLOYEE_ID"),
		@Result(property = "score", column = "SCORE"),
		@Result(property = "examTime", column = "EXAM_TIME")
	}
)
/**
 * 多表关联，扁平化结构
 */
List<ExamResult> queryByExamId(Integer examId);
```

#### 多表关联，实现一对多查询
```java
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExamResultNesting extends BaseEntity {

	@TableField(value = "EXAM_ID")
	private Integer examId;

	@TableField("NAME")
	private String name;

	@TableField("PASS_SCORE")
	private Integer passScore;

	@TableField("TIMES")
	private Integer times;

	/**
	 * 课程分数
	 */
	private List<ExamScore> examScores;

}
```

```java
@Select("select * from ins_exam where exam_id = #{examId}")
@Results(
	value = {
		@Result(property = "examId", column = "EXAM_ID"),
		@Result(property = "name", column = "NAME"),
		@Result(property = "passScore", column = "PASS_SCORE"),
		@Result(property = "times", column = "TIMES"),
		@Result(property = "examScores",
			column = "EXAM_ID",
			many = @Many(
			select = "com.microtomato.hirun.modules.steven.mapper.ExamScoreMapper.getByExamId"
		))
	}
)
List<ExamResultNesting> queryByExamIdNesting(Integer examId);
```

#### 多表关联 + 条件构造器
```java
@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    
    @Override
    public IPage<EmployeeExampleDTO> selectEmployeePageExample(String name, Long orgId, Long jobRole) {
        Page<EmployeeExampleDTO> page = new Page<>(1, 10);
        QueryWrapper<EmployeeExampleDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("b.employee_id = a.employee_id AND now() < b.end_date AND c.org_id = b.org_id");
        queryWrapper.like(StringUtils.isNotEmpty(name), "a.name", name);
        queryWrapper.eq(null != orgId, "b.org_id", orgId);
        queryWrapper.eq(null != jobRole, "b.job_role", jobRole);
        IPage<EmployeeExampleDTO> employeeExampleDTOIPage = employeeMapper.selectEmployeePageExample(page, queryWrapper);
        return employeeExampleDTOIPage;
    }

}

@Mapper
@DS("ins")
public interface EmployeeMapper extends BaseMapper<Employee> {
    
    @Select("SELECT a.name, a.employee_id, a.sex, a.mobile_no, a.identity_no, a.status, date_format( a.in_date, '%Y-%m-%d' ) in_date, b.job_role, c.org_id, c.name org_name\n" +
        "FROM ins_employee a, ins_employee_job_role b, ins_org c\n" +
        "${ew.customSqlSegment}")
    IPage<EmployeeExampleDTO> selectEmployeePageExample(Page<EmployeeExampleDTO> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
```

# 更新操作

#### 按 ID 进行更新，实体中 id 必须有数据，其它字段不为 null 的情况下，会设置到 set 语句中。
```SQL
UPDATE user set email = 'wtf2@qq.com', age = 26 WHERE id = 101010101001
```

```Java
public void updateById() {
	User user = new User();
	user.setId(101010101001L);
	user.setEmail("wtf2@qq.com");
	user.setAge(26);

	int rows = userMapper.updateById(user);
	System.out.println("影响记录数: " + rows);
}
```

#### 按 条件构造器 进行更新，实体中不为 null 的属性，会设置到 set 语句中。
```SQL
UPDATE user set email = 'wtf2@163.com', age = 27 WHERE name = '李毅伟' AND age = 28
```

```Java
public void updateByWrapper() {
	UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
	updateWrapper.eq("name", "李毅伟").eq("age", 28);

	User user = new User();
	user.setId(101010101001L);
	user.setEmail("wtf2@163.com");
	user.setAge(27);

	int rows = userMapper.update(user, updateWrapper);
	System.out.println("影响记录数: " + rows);
}
```

#### 更新少量字段时，无需构造实体，实体中不为 null 的属性，会设置到 set 语句中。
```SQL
UPDATE user SET email = 'wtf2@163.com', age = 27 WHERE name = '李毅伟' AND age = 28
```

```Java
public void updateWithoutEntity() {
	UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
	updateWrapper.eq("name", "李毅伟").eq("age", 28).set("email", "wtf2@163.com").set("age", 27);
	int rows = userMapper.update(null, updateWrapper);
	System.out.println("影响记录数: " + rows);
}
```

#### 基于 Lambda 语法，有防误写能力。
```SQL
UPDATE user SET email = 'wtf2@163.com', age = 27 WHERE name = '李毅伟' AND age = 28
```

```Java
public void updateByLambda() {
	LambdaUpdateWrapper<User> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
	lambdaUpdateWrapper.eq(User::getName, "李毅伟").eq(User::getAge, 28).set(User::getEmail, "wtf2@163.com").set(User::getAge, 27);
	int rows = userMapper.update(null, lambdaUpdateWrapper);
	System.out.println("影响记录数: " + rows);
}
```

#### 链式更新，极为简洁
```SQL
UPDATE user SET email = 'wtf2@163.com', age = 27 WHERE name = '李毅伟' AND age = 28
```

```Java
public void updateByLambdaChain() {
	boolean update = new LambdaUpdateChainWrapper<>(userMapper).eq(User::getName, "李毅伟").eq(User::getAge, 28)
	​	.set(User::getEmail, "wtf2@163.com").set(User::getAge, 27).update();
	System.out.println("是否更新成功: " + update);
}
```

# 删除操作

#### 根据 ID 主键来删除
```SQL
DELETE FROM user WHERE id = 1010101010
```

```Java
public void deleteById() {
	int rows = userMapper.deleteById(1010101010L);
	System.out.println("影响记录数: " + rows);
}
```

```SQL
DELETE FROM user WHERE name = '王天风' AND age = 30
```

```Java
public void deleteByMap() {

	Map<String, Object> map = new HashMap<>();
	map.put("name", "王天风");
	map.put("age", 30);

	int rows = userMapper.deleteByMap(map);
	System.out.println("影响记录数: " + rows);
}
```

#### 根据 ID 批量删除
```SQL
DELETE FROM user WHERE id IN (111, 222, 333)

```
```Java
public void deleteBatchIds() {
	int rows = userMapper.deleteBatchIds(Arrays.asList(111L, 222L, 333L));
	System.out.println("影响记录数: " + rows);
}
```

```SQL
DELETE FROM user WHERE age = 27 OR age > 41
```
```Java
public void deleteByWrapper() {
	LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
	lambdaQueryWrapper.eq(User::getAge, 27).or().gt(User::getAge, 41);
	int rows = userMapper.delete(lambdaQueryWrapper);
	System.out.println("影响记录数: " + rows);
}
```

# Active Record 活动记录

> 是一种领域模型模式，特点是一个模型类对应关系数据库中的一个表，模型实例对应一条记录。Active Record 一直广受动态语言的喜爱，而 Java 这类静态语言往往只能望叹其优雅。MP 的作者在 AR 的道路上做了一定探索。

```Java
/**
 * Active Record 实战
 *
 * 注意: PO 类必须继承 Model<T>
 * SQL: INSERT INTO person (id,name,age,email,manager_id,create_time) VALUES(?,?,?,?,?,?);
 */
public void activeRecordInsert() {
	Person person = new Person();
	person.setName("刘华强");
	person.setAge(29);
	person.setEmail("liuhua@163.com");
	person.setManagerId(111L);
	person.setCreateTime(LocalDateTime.now());
	boolean insert = person.insert();

	System.out.println("操作成功与否: " + insert);
}

/**
 * Active Record 实战
 *
 * 注意: PO 类必须继承 Model<T>
 * SQL: SELECT id,name,age,email,manager_id,create_time FROM person WHERE id = ?
 */
public void select() {
	Person person = new Person().selectById(11111111L);
	System.out.println(person);
}

/**
 * Active Record 实战
 * <p>
 * 注意: PO 类必须继承 Model<T>
 * SQL: SELECT id,name,age,email,manager_id,create_time FROM person WHERE id = ?
 */
public void select() {
	Person person = new Person();
	person.setId(11111L);
	Person selPerson = person.selectById();
	System.out.println(selPerson);
}

/**
 * Active Record 实战
 * <p>
 * 注意: PO 类必须继承 Model<T>
 * SQL: UPDATE person SET name = '金胖' AND id = 1111
 */
public void update() {
	Person person = new Person();
	person.setId(1111L);
	person.setName("金胖");
	boolean update = person.updateById();
	System.out.println("更新状态: " + update);
}

/**
 * Active Record 实战
 * <p>
 * 注意: PO 类必须继承 Model<T>
 * SQL: DELETE FROM person WHERE id = 2222
 */
public void delete() {
	Person person = new Person();
	person.setId(2222L);
	boolean delete = person.deleteById();
	System.out.println("状态: " + delete);
}

/* Active Record 实战
 * 注意: PO 类必须继承 Model<T>
 * PO 实例里有 id 时会先根据 id 查询，有记录时 update，没记录返回，执行 insert
 * SQL: SELECT id,name,age,email,manager_id,create_time FROM person WHERE id = ?
 * SQL: UPDATE person SET age = 30 WHERE id = 1111
 */
public void insertOrUpdate() {
	Person person = new Person();
	person.setId(1111L);
	person.setAge(30);
	boolean state = person.insertOrUpdate();
	System.out.println("状态: " + state);
}
```