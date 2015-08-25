/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50151
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50151
File Encoding         : 65001

Date: 2011-05-11 17:17:17
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `kcb`
-- ----------------------------
DROP TABLE IF EXISTS `kcb`;
CREATE TABLE `kcb` (
  `KCH` varchar(10) NOT NULL,
  `kcm` varchar(20) CHARACTER SET gb2312 DEFAULT NULL,
  `KXXQ` int(4) DEFAULT NULL,
  `XS` int(4) DEFAULT NULL,
  `XF` int(4) DEFAULT NULL,
  PRIMARY KEY (`KCH`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of kcb
-- ----------------------------
INSERT INTO `kcb` VALUES ('101', '计算机基础', '1', '80', '5');
INSERT INTO `kcb` VALUES ('102', '程序设计语言', '2', '68', '4');
INSERT INTO `kcb` VALUES ('103', '离散数学', '4', '68', '4');
INSERT INTO `kcb` VALUES ('104 ', '数据结构', '5', '68', '4');
INSERT INTO `kcb` VALUES ('105', '计算机原理', '5', '85', '5');
INSERT INTO `kcb` VALUES ('106', '操作系统', '6', '68', '4');
INSERT INTO `kcb` VALUES ('107', '数据库原理', '7', '68', '4');
INSERT INTO `kcb` VALUES ('108', '网络', '7', '51', '3');
INSERT INTO `kcb` VALUES ('109', '软件工程', '7', '51', '3');
