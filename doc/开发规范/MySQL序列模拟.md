## 序列模拟

#### 序列定义

* 定义序列名
* 初始值（如无特殊都设置成0）
* 当前值（如无特殊都设置成0）
* 最大值
* 步长
* 循环模式（day_cycle: 零点一过再取序列会从 initial_value 重新开始；max_cycle: 达到最大值再取序列会从 initial_value 重新开始）
* 更新时间

#### 例子
```SQL
INSERT INTO sequence VALUES ('SEQ_ID_MAX_CYCLE', 0, 0, 2000000, 1, 'max_cycle', now());
INSERT INTO sequence VALUES ('SEQ_ID_DAY_CYCLE', 0, 0, 2000000, 1, 'day_cycle', now());
INSERT INTO sequence VALUES ('SEQ_ID_MINUTE_CYCLE', 0, 0, 2000000, 1, 'minute_cycle', now());
```

| name                | initial_value | cur_value | max_value | increment | cycle_mode   | update_time |
| ------------------- | ------------- | --------- | --------- | --------- | ------------ | ----------- |
| SEQ_ID_DAY_CYCLE    | 0             | 0         | 2000000   | 1         | day_cycle    | ...         |
| SEQ_ID_MAX_CYCLE    | 0             | 0         | 2000000   | 1         | max_cycle    | ...         |
| SEQ_ID_MINUTE_CYCLE | 0             | 0         | 2000000   | 1         | minute_cycle | ...         |

#### 如何使用
```java
    /** 注入 IDualService */
    @Autowired
    private IDualService dualService;
    
    /** 获取原生的序列，未加工的 */
    public void rawSequence() {
        Long nextval = dualService.nextval("SEQ_ID_MAX_CYCLE"); // 名称跟 sequence 表的 name 对应。
    }
    
    /** 获取自定义序列 */
    public void userDefineSequence() {
        Long nextval = dualService.nextval(CustIdMaxCycleSeq.class); // 名称跟 CustIdMaxCycleSeq 是自定义的序列对象，主要用于自定义序列格式。
    }
    
```

```java
@Component
public class CustIdMaxCycleSeq extends AbstractSequence {

    @Override
    public Long nextval() {
        Long nextval = nextval("SEQ_ID_MAX_CYCLE");
        String strNextval = StringUtils.leftPad(String.valueOf(nextval), 8, '0');
        String timestamp = TimeUtils.now("yyyyMMdd");
        return Long.parseLong(timestamp + strNextval);
    }

}
```

### 建表语句
```SQL
-- 创建序列表
DROP TABLE IF EXISTS sequence;
CREATE TABLE sequence
(
    name          VARCHAR(50) NOT NULL,
    initial_value INT NOT NULL DEFAULT 0,
    cur_value     BIGINT unsigned NOT NULL,
    max_value     BIGINT unsigned NOT NULL,
    increment     INT NOT NULL DEFAULT 1,
    cycle_mode    VARCHAR(20) NOT NULL COMMENT 'max_cycle: 按最大值循环, day_cycle: 按天循环, minute_cycle: 按分钟循环',
    update_time   DATETIME NOT NULL,
    PRIMARY KEY (name)
)
ENGINE=InnoDB;

-- 创建对应函数
DROP FUNCTION IF EXISTS nextval;
DELIMITER $$
CREATE FUNCTION nextval(seq_name char (50)) returns BIGINT
BEGIN
  SELECT cur_value, max_value, initial_value, cycle_mode, update_time, now() INTO @cur_value, @max_value, @initial_value, @cycle_mode, @update_time, @now_time FROM sequence WHERE name = seq_name;
  if (@cycle_mode = 'max_cycle') then
    if(@cur_value >= @max_value) then
      UPDATE sequence SET cur_value = @initial_value WHERE name = seq_name;
    end if;
  elseif (@cycle_mode = 'day_cycle') then
    if(to_days(@now_time) != to_days(@update_time)) then
      UPDATE sequence SET cur_value = @initial_value WHERE name = seq_name;
    end if;
  elseif (@cycle_mode = 'minute_cycle') then
    if(minute(@now_time) != minute(@update_time)) then
      UPDATE sequence SET cur_value = @initial_value WHERE name = seq_name;
    end if;
  end if;

  UPDATE sequence SET cur_value = last_insert_id(cur_value + increment), update_time=now() WHERE name = seq_name;
  RETURN last_insert_id();
END $$
DELIMITER ;

-- 插入测试数据
INSERT INTO sequence VALUES ('SEQ_ID_MAX_CYCLE', 0, 0, 99999999, 1, 'max_cycle', now());
INSERT INTO sequence VALUES ('SEQ_ID_DAY_CYCLE', 0, 0, 99999999, 1, 'day_cycle', now());
INSERT INTO sequence VALUES ('SEQ_ID_MINUTE_CYCLE', 0, 0, 99999999, 1, 'minute_cycle', now());

-- 序列测试验证
select nextval('SEQ_ID_MAX_CYCLE');
select nextval('SEQ_ID_DAY_CYCLE');
select nextval('SEQ_ID_MINUTE_CYCLE');

grant execute on sys.* to sys;
grant execute on ins.* to ins;
```