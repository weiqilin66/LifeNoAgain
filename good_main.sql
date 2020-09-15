/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : dell_mysql

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 14/09/2020 13:35:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for good_main
-- ----------------------------
DROP TABLE IF EXISTS `good_main`;
CREATE TABLE `good_main`  (
  `id` int(4) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `label` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of good_main
-- ----------------------------
INSERT INTO `good_main` VALUES (0001, '女神异闻录5R皇家版', 'PS4');
INSERT INTO `good_main` VALUES (0002, '女神异闻录5', 'PS4');
INSERT INTO `good_main` VALUES (0003, '女神异闻录5S', 'PS4');
INSERT INTO `good_main` VALUES (0004, '女神异闻录5S', 'NS');
INSERT INTO `good_main` VALUES (0005, '宝可梦剑盾', 'NS');
INSERT INTO `good_main` VALUES (0006, '怪物猎人世界', 'PS4');
INSERT INTO `good_main` VALUES (0007, '地平线年度版', 'PS4');
INSERT INTO `good_main` VALUES (0008, '分手厨房1+2', 'NS');
INSERT INTO `good_main` VALUES (0009, '分手厨房1+2', 'PS4');

SET FOREIGN_KEY_CHECKS = 1;
