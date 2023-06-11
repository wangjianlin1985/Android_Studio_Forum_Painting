/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : paint_bbs_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-01-09 15:22:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `article`
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `articleId` int(11) NOT NULL auto_increment,
  `title` varchar(20) default NULL,
  `articleClassObj` int(11) default NULL,
  `content` longtext,
  `hitNum` int(11) default NULL,
  `userObj` varchar(20) default NULL,
  `addTime` varchar(20) default NULL,
  PRIMARY KEY  (`articleId`),
  KEY `FK379164D6BBBA1899` (`articleClassObj`),
  KEY `FK379164D6C80FC67` (`userObj`),
  CONSTRAINT `FK379164D6BBBA1899` FOREIGN KEY (`articleClassObj`) REFERENCES `articleclass` (`articleClassId`),
  CONSTRAINT `FK379164D6C80FC67` FOREIGN KEY (`userObj`) REFERENCES `userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES ('1', 'aa', '1', 'bbb', '2', 'user1', '2017-12-29 13:25:52');
INSERT INTO `article` VALUES ('2', '11', '1', '22', '3', 'user2', '2018-01-02 00:17:17');
INSERT INTO `article` VALUES ('3', '今天吃牛排了', '2', '今天生意还可以，心情好去吃好的了！', '1', 'user1', '2018-01-09 15:20:08');

-- ----------------------------
-- Table structure for `articleclass`
-- ----------------------------
DROP TABLE IF EXISTS `articleclass`;
CREATE TABLE `articleclass` (
  `articleClassId` int(11) NOT NULL auto_increment,
  `articleClassName` varchar(20) default NULL,
  PRIMARY KEY  (`articleClassId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of articleclass
-- ----------------------------
INSERT INTO `articleclass` VALUES ('1', '心情随笔');
INSERT INTO `articleclass` VALUES ('2', '趣闻趣事');

-- ----------------------------
-- Table structure for `news`
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `newsId` int(11) NOT NULL auto_increment,
  `title` varchar(80) default NULL,
  `content` longtext,
  `viewNum` int(11) default NULL,
  `publishDate` varchar(30) default NULL,
  PRIMARY KEY  (`newsId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES ('1', '111111', '2222', '1', '2017-12-29 15:13:42');

-- ----------------------------
-- Table structure for `paint`
-- ----------------------------
DROP TABLE IF EXISTS `paint`;
CREATE TABLE `paint` (
  `paintId` int(11) NOT NULL auto_increment,
  `paintName` varchar(20) default NULL,
  `paintClassObj` int(11) default NULL,
  `paintPhoto` varchar(50) default NULL,
  `paintDesc` longtext,
  `paintFile` varchar(50) default NULL,
  `hitNum` int(11) default NULL,
  `userObj` varchar(20) default NULL,
  `addTime` varchar(20) default NULL,
  PRIMARY KEY  (`paintId`),
  KEY `FK495083E6115EA19` (`paintClassObj`),
  KEY `FK495083EC80FC67` (`userObj`),
  CONSTRAINT `FK495083E6115EA19` FOREIGN KEY (`paintClassObj`) REFERENCES `paintclass` (`paintClassId`),
  CONSTRAINT `FK495083EC80FC67` FOREIGN KEY (`userObj`) REFERENCES `userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of paint
-- ----------------------------
INSERT INTO `paint` VALUES ('1', '11', '1', 'upload/noimage.jpg', '222', '', '9', 'user1', '2017-12-29 18:52:46');
INSERT INTO `paint` VALUES ('3', '小美女一个', '2', 'upload/1180215192418.jpg', '大家看下吧！', 'upload/118021519241.jpg', '2', 'user1', '2018-01-09 15:19:24');

-- ----------------------------
-- Table structure for `paintclass`
-- ----------------------------
DROP TABLE IF EXISTS `paintclass`;
CREATE TABLE `paintclass` (
  `paintClassId` int(11) NOT NULL auto_increment,
  `paintClassName` varchar(20) default NULL,
  PRIMARY KEY  (`paintClassId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of paintclass
-- ----------------------------
INSERT INTO `paintclass` VALUES ('1', '水彩画');
INSERT INTO `paintclass` VALUES ('2', '素描');

-- ----------------------------
-- Table structure for `postinfo`
-- ----------------------------
DROP TABLE IF EXISTS `postinfo`;
CREATE TABLE `postinfo` (
  `postInfoId` int(11) NOT NULL auto_increment,
  `title` varchar(80) default NULL,
  `content` longtext,
  `hitNum` int(11) default NULL,
  `userObj` varchar(20) default NULL,
  `addTime` varchar(30) default NULL,
  PRIMARY KEY  (`postInfoId`),
  KEY `FK30F4858EC80FC67` (`userObj`),
  CONSTRAINT `FK30F4858EC80FC67` FOREIGN KEY (`userObj`) REFERENCES `userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of postinfo
-- ----------------------------
INSERT INTO `postinfo` VALUES ('1', 'aaa', 'bbb', '38', 'user1', '2017-12-29 14:15:56');
INSERT INTO `postinfo` VALUES ('2', '大家好', '初来乍到，大家多多关照！', '15', 'user2', '2018-01-01 00:51:54');
INSERT INTO `postinfo` VALUES ('3', '我也认识下大家', '我叫双鱼林 ，大神哈', '3', 'user1', '2018-01-09 15:17:20');

-- ----------------------------
-- Table structure for `reply`
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply` (
  `replyId` int(11) NOT NULL auto_increment,
  `postInfoObj` int(11) default NULL,
  `content` longtext,
  `userObj` varchar(20) default NULL,
  `replyTime` varchar(30) default NULL,
  PRIMARY KEY  (`replyId`),
  KEY `FK4B322CAC80FC67` (`userObj`),
  KEY `FK4B322CA82E86259` (`postInfoObj`),
  CONSTRAINT `FK4B322CA82E86259` FOREIGN KEY (`postInfoObj`) REFERENCES `postinfo` (`postInfoId`),
  CONSTRAINT `FK4B322CAC80FC67` FOREIGN KEY (`userObj`) REFERENCES `userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of reply
-- ----------------------------
INSERT INTO `reply` VALUES ('1', '1', 'ccc', 'user2', '2017-12-29 18:52:47');
INSERT INTO `reply` VALUES ('2', '1', '11', 'user2', '2017-12-29 18:52:47');
INSERT INTO `reply` VALUES ('3', '1', '22', 'user1', '2017-12-29 18:52:48');
INSERT INTO `reply` VALUES ('4', '1', '3333', 'user1', '2017-12-29 18:52:50');
INSERT INTO `reply` VALUES ('5', '2', '11', 'user1', '2018-01-01 23:55:50');
INSERT INTO `reply` VALUES ('6', '2', 'very good!', 'user1', '2018-01-01 23:56:04');
INSERT INTO `reply` VALUES ('7', '2', '支持你 新人', 'user1', '2018-01-09 15:16:52');

-- ----------------------------
-- Table structure for `userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `user_name` varchar(20) NOT NULL,
  `password` varchar(30) default NULL,
  `name` varchar(20) default NULL,
  `gender` varchar(4) default NULL,
  `birthDate` datetime default NULL,
  `userPhoto` varchar(50) default NULL,
  `telephone` varchar(20) default NULL,
  `email` varchar(50) default NULL,
  `address` varchar(80) default NULL,
  `regTime` varchar(30) default NULL,
  PRIMARY KEY  (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES ('user1', '123', '双鱼林', '男', '2017-12-01 00:00:00', 'upload/810878EDC6EAFC18E192E63D14DDB182.jpg', '13573598343', 'syl@163.com', '四川成都红星路13号', '2017-12-29 14:15:15');
INSERT INTO `userinfo` VALUES ('user2', '123', '李明生', '男', '2017-12-30 00:00:00', 'upload/F482558D03BE6C633158FCDE5F6FFAA8.jpg', '13459830843', 'limings@163.com', '福建福州', '2018-01-01 00:19:48');
