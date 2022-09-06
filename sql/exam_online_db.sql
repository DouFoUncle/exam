/*
 Navicat Premium Data Transfer

 Source Server         : Tencent
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 106.53.73.30:3306
 Source Schema         : exam_online_db

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 09/04/2022 15:50:59
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `course_name` varchar(32) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '课程名',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8  COMMENT = '课程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (2, '计算机科学与技术课程一', '2022-03-30 13:57:19');
INSERT INTO `course` VALUES (3, '软件工程课程一', '2022-04-01 13:32:10');

-- ----------------------------
-- Table structure for examination_detail
-- ----------------------------
DROP TABLE IF EXISTS `examination_detail`;
CREATE TABLE `examination_detail`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int NULL DEFAULT NULL COMMENT '学生ID',
  `test_paper_id` int NULL DEFAULT NULL COMMENT '考试ID',
  `exam_title` varchar(255) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '试卷标题',
  `subject_id` int NULL DEFAULT NULL COMMENT '题目ID',
  `user_select` varchar(32) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '学生作答的选择（0答错  1答对）',
  `subject_title` varchar(1024) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '题目标题',
  `create_date` datetime NULL DEFAULT NULL COMMENT '记录时间',
  `user_result` varchar(64) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '学生的作答',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8  COMMENT = '考试详情（记录学生每道题的对错情况）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of examination_detail
-- ----------------------------
INSERT INTO `examination_detail` VALUES (1, 7, 3, '自动组卷第一套考试卷', 4, '0', '计算机应用最广泛的应用领域是（）', '2022-04-06 14:39:11', NULL);
INSERT INTO `examination_detail` VALUES (2, 7, 3, '自动组卷第一套考试卷', 3, '1', '将来计算机的发展趋势将表现在以下几个方面：', '2022-04-06 14:39:11', '2,3,4');
INSERT INTO `examination_detail` VALUES (3, 7, 3, '自动组卷第一套考试卷', 8, '0', 'CPU 是不能直接访问外存储器的', '2022-04-06 14:39:11', NULL);
INSERT INTO `examination_detail` VALUES (4, 7, 3, '自动组卷第一套考试卷', 6, '0', '计算机内存比外存（?）', '2022-04-06 14:39:11', NULL);
INSERT INTO `examination_detail` VALUES (5, 7, 3, '自动组卷第一套考试卷', 7, '0', '防火墙的类型包括（?）', '2022-04-06 14:39:11', NULL);
INSERT INTO `examination_detail` VALUES (6, 7, 3, '自动组卷第一套考试卷', 5, '1', '一个字节包含的二进制位数是8位', '2022-04-06 14:39:11', '1');
INSERT INTO `examination_detail` VALUES (7, 7, 11, '自动组卷1', 4, '0', '计算机应用最广泛的应用领域是（）', '2022-04-08 17:10:53', '3');
INSERT INTO `examination_detail` VALUES (8, 7, 11, '自动组卷1', 3, '0', '将来计算机的发展趋势将表现在以下几个方面：', '2022-04-08 17:10:53', '3,4');
INSERT INTO `examination_detail` VALUES (9, 7, 11, '自动组卷1', 8, '1', 'CPU 是不能直接访问外存储器的', '2022-04-08 17:10:53', '1');
INSERT INTO `examination_detail` VALUES (10, 7, 11, '自动组卷1', 6, '0', '计算机内存比外存（?）', '2022-04-08 17:10:53', '1');
INSERT INTO `examination_detail` VALUES (11, 7, 11, '自动组卷1', 7, '0', '防火墙的类型包括（?）', '2022-04-08 17:10:53', '1,2,3,4');
INSERT INTO `examination_detail` VALUES (12, 7, 11, '自动组卷1', 5, '1', '一个字节包含的二进制位数是8位', '2022-04-08 17:10:53', '1');

-- ----------------------------
-- Table structure for examination_visit
-- ----------------------------
DROP TABLE IF EXISTS `examination_visit`;
CREATE TABLE `examination_visit`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `test_paper_id` int NULL DEFAULT NULL COMMENT '对应发布的考试的ID',
  `user_id` int NULL DEFAULT NULL COMMENT '对应参与考试的学生ID',
  `title` varchar(255) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '试卷标题',
  `score` int NULL DEFAULT NULL COMMENT '考试分数',
  `end_date` datetime NULL DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8  COMMENT = '考试记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of examination_visit
-- ----------------------------
INSERT INTO `examination_visit` VALUES (1, 3, 7, '自动组卷第一套考试卷', 7, '2022-04-06 14:39:11');
INSERT INTO `examination_visit` VALUES (2, 11, 7, '自动组卷1', 4, '2022-04-08 17:10:53');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int NULL DEFAULT NULL COMMENT '对应留言的学生ID',
  `student_name` varchar(32) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '学生姓名',
  `content` varchar(1024) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '留言内容',
  `title` varchar(255) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '留言标题',
  `user_teacher_id` int NULL DEFAULT NULL COMMENT '对应回复的老师ID',
  `teacher_name` varchar(32) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '老师姓名',
  `reply_content` varchar(1024) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '回复内容',
  `create_date` datetime NULL DEFAULT NULL COMMENT '留言时间',
  `reply_date` datetime NULL DEFAULT NULL COMMENT '回复时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8  COMMENT = '留言表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (1, 7, '孙悟饭', '请问老师1+1等于几呀？', '小问题请教老师', 4, '龟仙人', '等于2', '2022-04-07 13:15:33', '2022-04-07 15:20:44');
INSERT INTO `message` VALUES (2, 7, '孙悟饭', '请问老师1+1等于几？？？？？', '小问题请教老师', 4, '龟仙人', '不知道', '2022-04-07 13:27:42', '2022-04-08 17:11:52');

-- ----------------------------
-- Table structure for subject
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `course_id` varchar(255) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '对应的课程ID',
  `title` varchar(1024) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '题目标题',
  `option_one` varchar(1024) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '选项1',
  `option_two` varchar(1024) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '选项2',
  `option_three` varchar(1024) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '选项3',
  `option_four` varchar(1024) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '选项4',
  `result` varchar(32) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '正确答案（多个答案用英文逗号隔开）',
  `type` varchar(1) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '题目类型（0单选  1多选  2判断）',
  `category` varchar(1) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '题目类别（0练习题   1考试题）',
  `score` int NULL DEFAULT NULL COMMENT '题目分值',
  `del_flag` varchar(2) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '逻辑删除标识 0没删除 1删除了',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8  COMMENT = '试题表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of subject
-- ----------------------------
INSERT INTO `subject` VALUES (1, '2', '1+1=？', '1', '11', '3', '2', '4', '0', '0', 2, '0');
INSERT INTO `subject` VALUES (2, '2', '1、5、2、8哪些是单数', '1', '5', '2', '8', '1,2', '1', '0', 2, '0');
INSERT INTO `subject` VALUES (3, '2', '将来计算机的发展趋势将表现在以下几个方面：', '虚拟化', '网络化', '多媒体化', '智能化', '2,3,4', '1', '1', 5, '0');
INSERT INTO `subject` VALUES (4, '2', '计算机应用最广泛的应用领域是（）', '数值计算', '数据处理', '程序控制', '人工智能', '2', '0', '1', 5, '0');
INSERT INTO `subject` VALUES (5, '3', '一个字节包含的二进制位数是8位', '正确', '错误', '', '', '1', '2', '1', 2, '0');
INSERT INTO `subject` VALUES (6, '3', '计算机内存比外存（?）', '便宜但能存储更多信息', '存储容量大', '存取速度快', '更贵但能存储更多的信息', '3', '0', '1', 2, '0');
INSERT INTO `subject` VALUES (7, '3', '防火墙的类型包括（?）', '数据包过滤', '应用级网关', '网关', '复合型防火墙', '1,2', '1', '1', 5, '0');
INSERT INTO `subject` VALUES (8, '2', 'CPU 是不能直接访问外存储器的', '正确', '错误', '', '', '1', '2', '1', 2, '0');
INSERT INTO `subject` VALUES (9, '2', '1+1=2吗？', '正确', '错误', '', '', '1', '2', '0', 2, '0');
INSERT INTO `subject` VALUES (10, '3', '2+2=？', '1', '4', '2', '12', '2', '0', '0', 2, '0');
INSERT INTO `subject` VALUES (11, '3', '2、13、88、9 以上这些数字哪些是双数？', '2', '13', '88', '9', '1,3', '1', '0', 2, '0');
INSERT INTO `subject` VALUES (12, '3', '4+4等于9', '正确', '错误', '', '', '2', '2', '0', 2, '0');
INSERT INTO `subject` VALUES (22, '2', '以下哪个是波形文件格式', 'wav', 'cmf', 'voc', 'mid', '1', '0', '1', 2, '0');
INSERT INTO `subject` VALUES (23, '2', '在TPC/IP协议簇中，TCP是？', '传输控制协议', '网际协议', '文件传输协议', '超文本传输协议', '1', '0', '1', 2, '0');
INSERT INTO `subject` VALUES (24, '2', 'Web也叫万维网，主要包含', 'TCP/IP协议', 'HTTP协议', '客户机', 'WWW服务', '1,2,3,4', '1', '1', 5, '0');


-- ----------------------------
-- Records of test_paper
-- ----------------------------
INSERT INTO `test_paper` VALUES (1, '第一套考试', '0', 17, 1, 4, '2022-04-01 13:38:36', 1, '50', '2022-04-30 14:16:26', '2022-04-30 20:50:07', '1');
INSERT INTO `test_paper` VALUES (3, '自动组卷第一套考试卷', '0', 21, 1, 6, '2022-04-03 09:24:37', 1, '50', '2022-04-30 17:22:47', '2022-04-30 20:50:07', '1');
INSERT INTO `test_paper` VALUES (4, '练习卷', '1', 6, 1, 3, '2022-04-03 12:27:21', 0, '30', '2022-04-30 20:27:07', '2022-04-30 20:50:07', '1');
INSERT INTO `test_paper` VALUES (5, '孙悟饭在线练习-20220408134636528', '1', 29, 7, 10, '2022-04-08 13:46:37', 1, '20', '2022-04-08 13:56:36', '2022-04-08 14:24:36', '2');
INSERT INTO `test_paper` VALUES (6, '人工组卷', '1', 6, 1, 3, '2022-04-08 14:49:49', 0, '50', '2022-04-30 14:49:17', '2022-04-30 15:00:00', '0');
INSERT INTO `test_paper` VALUES (7, '人工组卷2', '1', 6, 1, 3, '2022-04-08 14:53:15', 0, '50', '2022-04-30 14:52:51', '2022-05-01 14:52:51', '0');
INSERT INTO `test_paper` VALUES (10, '人工组卷3', '0', 7, 1, 2, '2022-04-08 17:08:59', 0, '50', '2022-04-30 17:08:38', '2022-04-30 18:08:38', '0');
INSERT INTO `test_paper` VALUES (11, '自动组卷1', '0', 21, 1, 6, '2022-04-08 17:09:46', 1, '50', '2022-04-08 17:09:31', '2022-04-09 17:09:25', '0');

-- ----------------------------
-- Table structure for test_paper_subject_real
-- ----------------------------
DROP TABLE IF EXISTS `test_paper_subject_real`;
CREATE TABLE `test_paper_subject_real`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `test_paper_id` int NULL DEFAULT NULL COMMENT '对应试卷ID',
  `subject_id` int NULL DEFAULT NULL COMMENT '对应试题ID',
  `title` varchar(255) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '试卷标题',
  `subject_title` varchar(1024) CHARACTER SET utf8  NULL DEFAULT NULL COMMENT '试题标题',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8  COMMENT = '试题与试卷关系绑定' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test_paper_subject_real
-- ----------------------------
INSERT INTO `test_paper_subject_real` VALUES (4, 3, 4, '自动组卷第一套考试卷', '计算机应用最广泛的应用领域是（）');
INSERT INTO `test_paper_subject_real` VALUES (5, 3, 3, '自动组卷第一套考试卷', '将来计算机的发展趋势将表现在以下几个方面：');
INSERT INTO `test_paper_subject_real` VALUES (6, 3, 8, '自动组卷第一套考试卷', 'CPU不能直接访问外存储器');
INSERT INTO `test_paper_subject_real` VALUES (7, 3, 6, '自动组卷第一套考试卷', '计算机内存比外存（?）');
INSERT INTO `test_paper_subject_real` VALUES (8, 3, 7, '自动组卷第一套考试卷', '防火墙的类型包括（?）');
INSERT INTO `test_paper_subject_real` VALUES (9, 3, 5, '自动组卷第一套考试卷', '一个字节包含的二进制位数是8位');
INSERT INTO `test_paper_subject_real` VALUES (19, 4, 2, '练习卷', '1、5、2、8哪些是单数');
INSERT INTO `test_paper_subject_real` VALUES (20, 4, 10, '练习卷', '2+2=？');
INSERT INTO `test_paper_subject_real` VALUES (21, 4, 11, '练习卷', '2、13、88、9 以上这些数字哪些是双数？');
INSERT INTO `test_paper_subject_real` VALUES (22, 1, 3, '第一套考试', '将来计算机的发展趋势将表现在以下几个方面：');
INSERT INTO `test_paper_subject_real` VALUES (23, 1, 4, '第一套考试', '计算机应用最广泛的应用领域是（）');
INSERT INTO `test_paper_subject_real` VALUES (24, 1, 5, '第一套考试', '一个字节包含的二进制位数是8位');
INSERT INTO `test_paper_subject_real` VALUES (25, 1, 7, '第一套考试', '防火墙的类型包括（?）');
INSERT INTO `test_paper_subject_real` VALUES (26, 5, 9, '孙悟饭在线练习-20220408134636528', '1+1=2吗？');
INSERT INTO `test_paper_subject_real` VALUES (27, 5, 8, '孙悟饭在线练习-20220408134636528', 'CPU 是不能直接访问外存储器的');
INSERT INTO `test_paper_subject_real` VALUES (28, 5, 10, '孙悟饭在线练习-20220408134636528', '2+2=？');
INSERT INTO `test_paper_subject_real` VALUES (29, 5, 7, '孙悟饭在线练习-20220408134636528', '防火墙的类型包括（?）');
INSERT INTO `test_paper_subject_real` VALUES (30, 5, 5, '孙悟饭在线练习-20220408134636528', '一个字节包含的二进制位数是8位');
INSERT INTO `test_paper_subject_real` VALUES (31, 5, 3, '孙悟饭在线练习-20220408134636528', '将来计算机的发展趋势将表现在以下几个方面：');
INSERT INTO `test_paper_subject_real` VALUES (32, 5, 11, '孙悟饭在线练习-20220408134636528', '2、13、88、9 以上这些数字哪些是双数？');
INSERT INTO `test_paper_subject_real` VALUES (33, 5, 4, '孙悟饭在线练习-20220408134636528', '计算机应用最广泛的应用领域是（）');
INSERT INTO `test_paper_subject_real` VALUES (34, 5, 12, '孙悟饭在线练习-20220408134636528', '4+4等于9');
INSERT INTO `test_paper_subject_real` VALUES (35, 5, 2, '孙悟饭在线练习-20220408134636528', '1、5、2、8哪些是单数');
INSERT INTO `test_paper_subject_real` VALUES (36, 6, 1, '人工组卷', '1+1=？');
INSERT INTO `test_paper_subject_real` VALUES (37, 6, 10, '人工组卷', '2+2=？');
INSERT INTO `test_paper_subject_real` VALUES (38, 6, 11, '人工组卷', '2、13、88、9 以上这些数字哪些是双数？');
INSERT INTO `test_paper_subject_real` VALUES (39, 7, 2, '人工组卷2', '1、5、2、8哪些是单数');
INSERT INTO `test_paper_subject_real` VALUES (40, 7, 9, '人工组卷2', '1+1=2吗？');
INSERT INTO `test_paper_subject_real` VALUES (41, 7, 12, '人工组卷2', '4+4等于9');
INSERT INTO `test_paper_subject_real` VALUES (57, 10, 3, '人工组卷3', '将来计算机的发展趋势将表现在以下几个方面：');
INSERT INTO `test_paper_subject_real` VALUES (58, 10, 6, '人工组卷3', '计算机内存比外存（?）');
INSERT INTO `test_paper_subject_real` VALUES (59, 11, 4, '自动组卷1', '计算机应用最广泛的应用领域是（）');
INSERT INTO `test_paper_subject_real` VALUES (60, 11, 3, '自动组卷1', '将来计算机的发展趋势将表现在以下几个方面：');
INSERT INTO `test_paper_subject_real` VALUES (61, 11, 8, '自动组卷1', 'CPU 是不能直接访问外存储器的');
INSERT INTO `test_paper_subject_real` VALUES (62, 11, 6, '自动组卷1', '计算机内存比外存（?）');
INSERT INTO `test_paper_subject_real` VALUES (63, 11, 7, '自动组卷1', '防火墙的类型包括（?）');
INSERT INTO `test_paper_subject_real` VALUES (64, 11, 5, '自动组卷1', '一个字节包含的二进制位数是8位');


-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '0', NULL, '超级管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '140827199802280012', '19988776677', '1', NULL, NULL);
INSERT INTO `user` VALUES (2, '0', NULL, '管理员', 'wukong', 'e10adc3949ba59abbe56e057f20f883e', '140827199802280012', '19988776677', '1', NULL, NULL);
INSERT INTO `user` VALUES (3, '0', NULL, '张三', 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '140827199802280012', '18899778899', '1', NULL, NULL);
INSERT INTO `user` VALUES (4, '1', NULL, '龟仙人', 'xianren', 'e10adc3949ba59abbe56e057f20f883e', '140827199802280012', '18899777788', '1', '一班', '计算机科学与技术');
INSERT INTO `user` VALUES (5, '1', NULL, '桃白白', 'taobaibai', 'e10adc3949ba59abbe56e057f20f883e', '140827199802280012', '15566778899', '1', '一班', '计算机信息管理');
INSERT INTO `user` VALUES (6, '1', NULL, '测试', '123456', '698d51a19d8a121ce581499d7b701668', '140827199802280012', '15588774455', '1', '测试', '测试');
INSERT INTO `user` VALUES (7, '2', '201809011501', '孙悟饭', 'wufan', 'e10adc3949ba59abbe56e057f20f883e', '140827199802280012', '15544889966', '1', '一班', '计算机科学与技术');
INSERT INTO `user` VALUES (8, '2', '201809011502', '孙悟天', 'wutian', 'e10adc3949ba59abbe56e057f20f883e', '140827199802280012', '13555664477', '1', '一班', '计算机科学与技术');
INSERT INTO `user` VALUES (9, '2', '201809011503', '克林', 'kelin', 'e10adc3949ba59abbe56e057f20f883e', '140827199802280012', '13655667788', '1', '一班', '计算机科学与技术');

SET FOREIGN_KEY_CHECKS = 1;
