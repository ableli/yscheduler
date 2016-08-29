CREATE TABLE agent (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  ip varchar(100) NOT NULL,
  team_id bigint NOT NULL DEFAULT '0',
  alive tinyint NOT NULL DEFAULT '1',
  enable tinyint NOT NULL DEFAULT '1',
  version varchar(20) DEFAULT NULL,
  upgrade_task_id bigint,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY `INDEX_NAME` (`name`)
);

CREATE TABLE attempt (
  id bigint NOT NULL AUTO_INCREMENT,
  task_id bigint NOT NULL,
  instance_id bigint NOT NULL,
  agent_id bigint DEFAULT NULL,
  transaction_id bigint DEFAULT NULL,
  status tinyint NOT NULL,
  active tinyint NOT NULL DEFAULT '1',
  return_value int DEFAULT NULL,
  output varchar(1024),
  start_time datetime DEFAULT NULL,
  end_time datetime DEFAULT NULL,
  duration bigint DEFAULT NULL,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE schedule_progress (
  id int NOT NULL,
  current_schedule_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  create_time datetime DEFAULT NULL,
  update_time datetime DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE task_authority (
  id int NOT NULL AUTO_INCREMENT,
  user_id bigint NOT NULL,
  task_id bigint NOT NULL,
  mode tinyint NOT NULL DEFAULT '1',
  follow tinyint NOT NULL DEFAULT '1',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE task_instance (
  id bigint NOT NULL AUTO_INCREMENT,
  task_id bigint NOT NULL,
  workflow_instance_id bigint DEFAULT NULL,
  status tinyint NOT NULL,
  schedule_time datetime DEFAULT NULL,
  start_time datetime DEFAULT NULL,
  end_time datetime DEFAULT NULL,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE user (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL DEFAULT '',
  password varchar(100) DEFAULT NULL,
  email varchar(255) NOT NULL,
  telephone varchar(32) DEFAULT NULL,
  token varchar(100) DEFAULT NULL,
  team_id bigint DEFAULT NULL,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY `INDEX_USERNAME` (`name`)
);

CREATE TABLE team (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY index_teamname (name)
);

CREATE TABLE task (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  owner bigint NOT NULL,
  type tinyint NOT NULL,
  crontab varchar(100) DEFAULT '',
  command varchar(1000) NOT NULL,
  agent_id bigint DEFAULT NULL,
  status tinyint NOT NULL DEFAULT '1',
  can_skip tinyint NOT NULL DEFAULT '1',
  last_status_dependency tinyint NOT NULL DEFAULT '1',
  timeout int NOT NULL DEFAULT '60',
  retry_times int NOT NULL DEFAULT '0',
  description varchar(1024) DEFAULT NULL,
  last_schedule_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  attachment varchar(200) DEFAULT NULL,
  attachment_version bigint DEFAULT NULL,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE workflow (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  owner bigint NOT NULL,
  crontab varchar(100) DEFAULT '',
  status tinyint NOT NULL DEFAULT '1',
  can_skip tinyint NOT NULL DEFAULT '1',
  last_status_dependency tinyint NOT NULL DEFAULT '1',
  common tinyint NOT NULL DEFAULT '0',
  timeout int NOT NULL DEFAULT '60',
  description varchar(1024) DEFAULT NULL,
  last_schedule_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE workflow_authority (
  id bigint NOT NULL AUTO_INCREMENT,
  workflow_id bigint NOT NULL,
  user_id bigint NOT NULL,
  mode tinyint NOT NULL,
  follow tinyint NOT NULL,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE workflow_detail (
  id bigint NOT NULL AUTO_INCREMENT,
  workflow_id bigint NOT NULL,
  task_id bigint NOT NULL,
  timeout int NOT NULL DEFAULT '60',
  retry_times int NOT NULL DEFAULT '0',
  delay int NOT NULL DEFAULT '0',
  last_status_dependency tinyint NOT NULL DEFAULT '1',
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE workflow_instance (
  id bigint NOT NULL AUTO_INCREMENT,
  workflow_id bigint NOT NULL,
  status tinyint NOT NULL,
  schedule_time datetime DEFAULT NULL,
  start_time datetime DEFAULT NULL,
  end_time datetime DEFAULT NULL,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE workflow_task_dependency (
  id bigint NOT NULL AUTO_INCREMENT,
  workflow_detail_id bigint NOT NULL,
  dependency_task_id bigint NOT NULL,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE `team_workflow_instance_status` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `team_id` bigint NOT NULL,
  `workflow_id` bigint NOT NULL,
  `workflow_instance_id` bigint NOT NULL,
  `status` tinyint NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE VIEW workflow_task_instance
AS select `task_instance`.`id` AS `id`,`task`.`name` AS `name`,1 AS `type`,
`task_instance`.`task_id` AS `workflow_task_id`,`task`.`owner` AS `owner`,
`task_instance`.`status` AS `status`,`task_instance`.`schedule_time` AS `schedule_time`,
`task_instance`.`start_time` AS `start_time`,`task_instance`.`end_time` AS `end_time`,
`task_instance`.`create_time` AS `create_time`,`task_instance`.`update_time` AS `update_time`
from (`task_instance` left join `task` on((`task_instance`.`task_id` = `task`.`id`)))
where (`task_instance`.`schedule_time` is not null) union select `workflow_instance`.`id` AS `id`,
`workflow`.`name` AS `name`,2 AS `2`,`workflow_instance`.`workflow_id` AS `task_workflow_id`,
`workflow`.`owner` AS `owner`,`workflow_instance`.`status` AS `status`,
`workflow_instance`.`schedule_time` AS `schedule_time`,`workflow_instance`.`start_time` AS `start_time`,
`workflow_instance`.`end_time` AS `end_time`,`workflow_instance`.`create_time` AS `create_time`,
`workflow_instance`.`update_time` AS `update_time` from (`workflow_instance` left join `workflow` 
on((`workflow_instance`.`workflow_id` = `workflow`.`id`))) where (`workflow_instance`.`schedule_time` is not null);


INSERT INTO schedule_progress (id, current_schedule_time, create_time, update_time) VALUES (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO team (id,name,create_time,update_time) VALUES (1,'admin',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO team (id,name,create_time,update_time) VALUES (2,'platform',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO user (id,name,password,email,telephone, create_time, update_time, team_id) VALUES (1,'admin','21232f297a57a5a743894a0e4a801fc3','platform@ndpmedia.com','15928776354', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);
INSERT INTO user (id,name,password,email,telephone, create_time, update_time, team_id) VALUES (2,'monitor','08b5411f848a2581a41672a759c87380','platform@ndpmedia.com','15921096896', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);