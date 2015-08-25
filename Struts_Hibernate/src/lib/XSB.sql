/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50151
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50151
File Encoding         : 65001

Date: 2011-05-11 17:17:49
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `xsb`
-- ----------------------------
DROP TABLE IF EXISTS `xsb`;
CREATE TABLE `xsb` (
  `XH` varchar(20) NOT NULL,
  `xm` varchar(20) CHARACTER SET gb2312 DEFAULT NULL,
  `XB` int(4) DEFAULT NULL,
  `CSSJ` datetime DEFAULT NULL,
  `ZY_ID` int(4) DEFAULT NULL,
  `ZXF` int(4) DEFAULT NULL,
  `BZ` varchar(500) DEFAULT NULL,
  `ZP` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`XH`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of xsb
-- ----------------------------
INSERT INTO `xsb` VALUES ('101', '严																			', '1', '1990-02-10 00:00:00', '2', '50', 'fd bf', null);
INSERT INTO `xsb` VALUES ('102', '??					', '1', '1991-02-01 00:00:00', '1', '50', ' ', null);
INSERT INTO `xsb` VALUES ('103', '王敏', '0', '1989-06-10 00:00:00', '2', '42', ' ', ' ');
INSERT INTO `xsb` VALUES ('104', '王林', '1', '1990-02-10 00:00:00', '2', '42', ' ', ' ');
