-- 秒杀执行的存储过程
DELIMITER $$ -- console; 转换为 $$

-- 定义存储过程
-- 参数：in 输入参数，可以被存储过程使用；out 输出参数
-- row_count():返回上一条修改类型sql(delete,insert,update)的影响行数
-- row_count:0-未修改数据，>0-表示修改影响的行数 <0-sql错误/未执行影响SQL,或者等待行级锁超时，需要事务回滚
CREATE PROCEDURE `seckill`.`execute_seckill`
  (in v_seckill_id BIGINT, in v_phone BIGINT,
      in v_kill_time TIMESTAMP, out r_result int)

  BEGIN
      DECLARE insert_count int DEFAULT 0;
      START TRANSACTION;
        INSERT IGNORE INTO success_killed
          (seckill_id, user_phone, create_time, state)
        VALUES
          (v_seckill_id,v_phone,v_kill_time, 0);

        SELECT row_count() INTO insert_count;
        IF (insert_count = 0) THEN
            -- 重复秒杀
            ROLLBACK ;
            SET r_result = -1;
        ELSEIF (insert_count < 0) THEN
            ROLLBACK ;
            -- 系统异常
            SET r_result = -2;
        ELSE
          UPDATE
              seckill
          SET
              number = number -1
          WHERE seckill_id = v_seckill_id
          AND end_time > v_kill_time
          AND start_time < v_kill_time
          AND number > 0;

          SELECT row_count() INTO insert_count;
          IF (insert_count = 0) THEN
            -- 秒杀结束
            ROLLBACK ;
            SET r_result = 0;
          ELSEIF (insert_count < 0) THEN
            -- 系统异常
            ROLLBACK ;
            SET r_result = -2;
          ELSE
            -- 秒杀成功
            COMMIT ;
            SET r_result = 1;
          END IF;
        END IF;
  END;
$$

-- 存储过程定义结束

-- 存储过程调用
DELIMITER ;
SET @r_result = -3;
CALL seckill.execute_seckill(1003,18565815836,now(),@r_result);

-- 获取结果
select @r_result;

-- 存储过程
-- 1、存储过程优化：优化事务行级锁的持有时间
-- 2、不要过度依赖存储过程
-- 3、简单的逻辑，可以应用存储过程
-- 4、QPS：一个秒杀单6000左右qps，