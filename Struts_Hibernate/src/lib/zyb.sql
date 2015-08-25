/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50151
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50151
File Encoding         : 65001

Date: 2011-05-11 17:17:54
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `zyb`
-- ----------------------------
DROP TABLE IF EXISTS `zyb`;
CREATE TABLE `zyb` (
  `ID` int(4) NOT NULL AUTO_INCREMENT,
  `zym` varchar(20) CHARACTER SET gb2312 DEFAULT NULL,
  `RS` int(4) DEFAULT '0',
  `fdy` varchar(20) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of zyb
-- ----------------------------
INSERT INTO `zyb` VALUES ('1', '计算机', '150', '黄日升');
INSERT INTO `zyb` VALUES ('2', '通信工程', '131', '赵红');
