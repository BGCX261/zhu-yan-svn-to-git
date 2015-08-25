/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50151
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50151
File Encoding         : 65001

Date: 2011-05-11 17:17:08
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `dlb`
-- ----------------------------
DROP TABLE IF EXISTS `dlb`;
CREATE TABLE `dlb` (
  `ID` int(4) NOT NULL,
  `XH` varchar(10) DEFAULT NULL,
  `KL` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of dlb
-- ----------------------------
INSERT INTO `dlb` VALUES ('1', '101', '101');
INSERT INTO `dlb` VALUES ('2', '102', '102');
