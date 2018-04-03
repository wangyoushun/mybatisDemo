/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : test2

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2018-04-03 21:26:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL DEFAULT '',
  `age` tinyint(4) NOT NULL DEFAULT '0',
  `address` varchar(255) NOT NULL DEFAULT '',
  `user_account` varchar(11) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'aaa', '12', 'sh', '');
INSERT INTO `user` VALUES ('2', 'aaa', '12', 'sh', '');
INSERT INTO `user` VALUES ('3', 'bbb', '12', '', '182739273');
INSERT INTO `user` VALUES ('4', '', '12', '', '182739273');
INSERT INTO `user` VALUES ('5', '', '12', '', '182739273');
INSERT INTO `user` VALUES ('6', 'cc', '13', '', '1111111');
INSERT INTO `user` VALUES ('17', 'aaa', '12', 'sh', '');
INSERT INTO `user` VALUES ('18', 'aaa', '12', 'sh', '');
INSERT INTO `user` VALUES ('19', 'aaa', '12', 'sh', '');
INSERT INTO `user` VALUES ('20', 'aaa', '12', 'sh', '');
INSERT INTO `user` VALUES ('21', 'aaa', '12', 'sh', '');
INSERT INTO `user` VALUES ('22', 'aaa', '12', 'sh', '');
INSERT INTO `user` VALUES ('23', 'aaa', '12', 'sh', '');
INSERT INTO `user` VALUES ('24', 'aaa', '12', 'sh', '');
INSERT INTO `user` VALUES ('25', 'aaa', '12', 'sh', '');
INSERT INTO `user` VALUES ('26', 'aaa', '12', 'sh', '');
