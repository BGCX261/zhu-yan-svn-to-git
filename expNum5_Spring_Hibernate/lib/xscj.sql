/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50022
Source Host           : localhost:3306
Source Database       : xscj

Target Server Type    : MYSQL
Target Server Version : 50022
File Encoding         : 65001

Date: 2011-05-26 21:35:25
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `detail`
-- ----------------------------
DROP TABLE IF EXISTS `detail`;
CREATE TABLE `detail` (
  `ID` int(4) NOT NULL auto_increment,
  `TRUENAME` varchar(8) default NULL,
  `EMAIL` varchar(50) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of detail
-- ----------------------------
INSERT INTO `detail` VALUES ('1', '朱碧波', 'Obama@163.com');

-- ----------------------------
-- Table structure for `dlb`
-- ----------------------------
DROP TABLE IF EXISTS `dlb`;
CREATE TABLE `dlb` (
  `ID` int(4) NOT NULL auto_increment,
  `XH` varchar(10) default NULL,
  `KL` varchar(20) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of dlb
-- ----------------------------
INSERT INTO `dlb` VALUES ('1', '101', '101');
INSERT INTO `dlb` VALUES ('2', '102', '102');
INSERT INTO `dlb` VALUES ('3', '08401', '123');

-- ----------------------------
-- Table structure for `kcb`
-- ----------------------------
DROP TABLE IF EXISTS `kcb`;
CREATE TABLE `kcb` (
  `KCH` varchar(10) character set utf8 NOT NULL,
  `kcm` varchar(20) character set utf8 default NULL,
  `KXXQ` int(4) default NULL,
  `XS` int(4) default NULL,
  `XF` int(4) default NULL,
  PRIMARY KEY  (`KCH`)
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

-- ----------------------------
-- Table structure for `login`
-- ----------------------------
DROP TABLE IF EXISTS `login`;
CREATE TABLE `login` (
  `ID` int(4) NOT NULL,
  `USERNAME` varchar(20) NOT NULL,
  `PASSWORD` varchar(20) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login
-- ----------------------------
INSERT INTO `login` VALUES ('1', 'Obama', '123');

-- ----------------------------
-- Table structure for `xs_kcb`
-- ----------------------------
DROP TABLE IF EXISTS `xs_kcb`;
CREATE TABLE `xs_kcb` (
  `XH` varchar(10) NOT NULL,
  `KCH` varchar(10) NOT NULL,
  PRIMARY KEY  (`XH`,`KCH`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of xs_kcb
-- ----------------------------
INSERT INTO `xs_kcb` VALUES ('101', '104 ');
INSERT INTO `xs_kcb` VALUES ('102', '101');
INSERT INTO `xs_kcb` VALUES ('103', '105');

-- ----------------------------
-- Table structure for `xsb`
-- ----------------------------
DROP TABLE IF EXISTS `xsb`;
CREATE TABLE `xsb` (
  `XH` varchar(20) character set utf8 NOT NULL,
  `xm` varchar(20) character set utf8 default NULL,
  `XB` int(4) default NULL,
  `CSSJ` datetime default NULL,
  `ZY_ID` int(4) default NULL,
  `ZXF` int(4) default NULL,
  `BZ` varchar(500) character set utf8 default NULL,
  `ZP` varchar(500) character set utf8 default NULL,
  PRIMARY KEY  (`XH`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of xsb
-- ----------------------------
INSERT INTO `xsb` VALUES ('101', '严腾飞																	', '1', '1990-02-10 00:00:00', '2', '50', 'dafeiji', '/expNum4/image/dog.jpg');
INSERT INTO `xsb` VALUES ('102', '??					', '1', '1991-02-01 00:00:00', '1', '50', ' ', null);
INSERT INTO `xsb` VALUES ('103', '王敏', '0', '1989-06-10 00:00:00', '2', '42', ' ', ' ');
INSERT INTO `xsb` VALUES ('104', '王林', '1', '1990-02-10 00:00:00', '2', '42', ' ', ' ');

-- ----------------------------
-- Table structure for `xsb1`
-- ----------------------------
DROP TABLE IF EXISTS `xsb1`;
CREATE TABLE `xsb1` (
  `XH` char(6) NOT NULL,
  `XM` char(8) NOT NULL,
  `XB` bit(1) NOT NULL,
  `CSSJ` datetime default NULL,
  `ZY_ID` varchar(8) NOT NULL,
  `BZ` varchar(500) default NULL,
  PRIMARY KEY  (`XH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xsb1
-- ----------------------------
INSERT INTO `xsb1` VALUES ('1001', '朱碧波', '', '2011-04-24 00:00:00', '1001', '00');
INSERT INTO `xsb1` VALUES ('10010', '朱碧波', '', '2011-04-12 00:00:00', '1010', '1111');
INSERT INTO `xsb1` VALUES ('10011', '朱碧波', '', '2011-04-12 00:00:00', '1010', '1111');
INSERT INTO `xsb1` VALUES ('10012', '朱碧波', '', '2011-04-03 00:00:00', '1010', '000');
INSERT INTO `xsb1` VALUES ('20', '啊啊', '', '2011-03-28 00:00:00', '1001', '00');
INSERT INTO `xsb1` VALUES ('23', 'hello', '', '2011-05-10 00:00:00', 'software', 'hello  world');

-- ----------------------------
-- Table structure for `zyb`
-- ----------------------------
DROP TABLE IF EXISTS `zyb`;
CREATE TABLE `zyb` (
  `ID` int(4) NOT NULL auto_increment,
  `zym` varchar(20) character set gb2312 default NULL,
  `RS` int(4) default '0',
  `fdy` varchar(20) character set gb2312 default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of zyb
-- ----------------------------
INSERT INTO `zyb` VALUES ('1', '计算机', '150', '黄日升');
INSERT INTO `zyb` VALUES ('2', '通信工程', '131', '赵红');
