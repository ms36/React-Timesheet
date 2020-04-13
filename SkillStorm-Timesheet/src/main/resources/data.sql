
INSERT INTO `timesheet` (`submitted`, `week_ending_date`, `user_id`) VALUES (false, '1/26/20', '1');

INSERT INTO `User` (`First_Name`, `Last_Name`, `User_Name`, `Password`) VALUES ('Michael','Stevenson', 'ms36', 'Molly1');
INSERT INTO `User` (`First_Name`, `Last_Name`, `User_Name`, `Password`) VALUES ('Howard','The Duck', 'TheDuck','QuackQuack');
INSERT INTO `User` (`First_Name`, `Last_Name`, `User_Name`, `Password`) VALUES ('William','Wallace', 'Warrior', 'Freedom');
INSERT INTO `User` (`First_Name`, `Last_Name`, `User_Name`, `Password`) VALUES ('Albert','Einstein', 'Genius', 'Emc2');
INSERT INTO `User` (`First_Name`, `Last_Name`, `User_Name`, `Password`) VALUES ('Yoda','', 'MasterJedi', 'PasswordThisIs');

INSERT INTO `day_of_week` (`name`, `timesheet_id`) VALUES ('Monday', '1');
INSERT INTO `day_of_week` (`name`, `timesheet_id`) VALUES ('Tuesday', '1');
INSERT INTO `day_of_week` (`name`, `timesheet_id`) VALUES ('Wednesday', '1');
INSERT INTO `day_of_week` (`name`, `timesheet_id`) VALUES ('Thursday', '1');
INSERT INTO `day_of_week` (`name`, `timesheet_id`) VALUES ('Friday', '1');
INSERT INTO `day_of_week` (`name`, `timesheet_id`) VALUES ('Saturday', '1');
INSERT INTO `day_of_week` (`name`, `timesheet_id`) VALUES ('Sunday', '1');

INSERT INTO `log_in_out` (`log_in`, `log_out`, `day_of_week_id`) VALUES ('09:00', '12:00', '1');
INSERT INTO `log_in_out` (`log_in`, `log_out`, `day_of_week_id`) VALUES ('13:00', '18:00', '1');
INSERT INTO `log_in_out` (`log_in`, `log_out`, `day_of_week_id`) VALUES ('09:00', '12:00', '2');
INSERT INTO `log_in_out` (`log_in`, `log_out`, `day_of_week_id`) VALUES ('13:00', '18:00', '2');
INSERT INTO `log_in_out` (`log_in`, `log_out`, `day_of_week_id`) VALUES ('09:00', '12:00', '3');
INSERT INTO `log_in_out` (`log_in`, `log_out`, `day_of_week_id`) VALUES ('13:00', '18:00', '3');
INSERT INTO `log_in_out` (`log_in`, `log_out`, `day_of_week_id`) VALUES ('09:00', '12:00', '4');
INSERT INTO `log_in_out` (`log_in`, `log_out`, `day_of_week_id`) VALUES ('13:00', '18:00', '4');
INSERT INTO `log_in_out` (`log_in`, `log_out`, `day_of_week_id`) VALUES ('09:00', '12:00', '5');
INSERT INTO `log_in_out` (`log_in`, `log_out`, `day_of_week_id`) VALUES ('13:00', '18:00', '5');
