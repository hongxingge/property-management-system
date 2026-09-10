-- ============================================================
-- 物业管理系统 · 索引脚本
-- 用途：部署 / 迁移数据库时重建索引（索引是数据库层面，不在代码里）
-- 说明：V1.3 通过 EXPLAIN 分析发现以下字段存在全表扫描(type=ALL)，
--       加索引后降为 ref，这里存档以便新环境重建。
-- ============================================================

-- ---- 业主表 room_user：业主登录 / 按手机号查询 ----
ALTER TABLE room_user ADD INDEX idx_phone (phone);

-- ---- 管理员/员工表 user：登录查询 ----
ALTER TABLE user ADD INDEX idx_phone (phone);

-- ---- 车辆表 owner_car ----
-- 按车牌号查询（唯一索引：车牌号本就唯一，同时防止重复录入）
ALTER TABLE owner_car ADD UNIQUE INDEX idx_car_number (car_number);
-- 按业主查车辆
ALTER TABLE owner_car ADD INDEX idx_owner_id (owner_id);

-- ---- 费用表 cost：业主查自己的账单 ----
ALTER TABLE cost ADD INDEX idx_uid (uid);

-- ---- 车位表 park：按车位编号查询 ----
ALTER TABLE park ADD INDEX idx_number (number);

-- ---- 停车场表 carport：按停车场编号查询 ----
ALTER TABLE carport ADD INDEX idx_number (number);

-- ---- 租售表 park_rent：业主查自己的租售记录 ----
ALTER TABLE park_rent ADD INDEX idx_owner_id (owner_id);