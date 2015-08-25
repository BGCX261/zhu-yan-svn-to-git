/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50151
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50151
File Encoding         : 65001

Date: 2011-05-11 17:17:44
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `xs_kcb`
-- ----------------------------
DROP TABLE IF EXISTS `xs_kcb`;
CREATE TABLE `xs_kcb` (
  `XH` varchar(10) NOT NULL,
  `KCH` varchar(10) NOT NULL,
  PRIMARY KEY (`XH`,`KCH`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of xs_kcb
-- ----------------------------
