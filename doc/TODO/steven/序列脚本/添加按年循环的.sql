create
    definer = `system`@localhost function nextval(seq_name char(50)) returns bigint
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
  elseif (@cycle_mode = 'year_cycle') then
    if(year(@now_time) != year(@update_time)) then
      UPDATE sequence SET cur_value = @initial_value WHERE name = seq_name;
    end if;
  end if;

  UPDATE sequence SET cur_value = last_insert_id(cur_value + increment), update_time=now() WHERE name = seq_name;
  RETURN last_insert_id();
END;

