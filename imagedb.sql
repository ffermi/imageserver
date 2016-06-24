CREATE DATABASE  IF NOT EXISTS `imagedb` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `imagedb`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: imagedb
-- ------------------------------------------------------
-- Server version	5.7.12-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accesstoken`
--

DROP TABLE IF EXISTS `accesstoken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accesstoken` (
  `accessToken` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `start` datetime NOT NULL,
  `expiration` datetime NOT NULL,
  `idUser` int(11) NOT NULL,
  `idClient` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`accessToken`),
  KEY `fk_acc_user_idx` (`idUser`),
  KEY `fk_acc_client_idx` (`idClient`),
  CONSTRAINT `fk_acc_client` FOREIGN KEY (`idClient`) REFERENCES `client` (`idClient`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_acc_user` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `authztoken`
--

DROP TABLE IF EXISTS `authztoken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authztoken` (
  `authToken` varchar(32) CHARACTER SET utf8 NOT NULL,
  `start` datetime NOT NULL,
  `expiration` datetime NOT NULL,
  `idClient` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `idUser` int(11) NOT NULL,
  PRIMARY KEY (`authToken`),
  KEY `fk_authz_user_idx` (`idUser`),
  KEY `fk_authz_client_idx` (`idClient`),
  CONSTRAINT `fk_authz_client` FOREIGN KEY (`idClient`) REFERENCES `client` (`idClient`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_authz_user` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client` (
  `idClient` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `secret` varchar(32) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`idClient`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `idImage` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(500) CHARACTER SET utf8 NOT NULL,
  `path` varchar(1000) CHARACTER SET utf8 NOT NULL,
  `owner` int(11) NOT NULL,
  PRIMARY KEY (`idImage`),
  UNIQUE KEY `path_UNIQUE` (`path`),
  KEY `fk_image_1_idx` (`owner`),
  CONSTRAINT `fk_image_1` FOREIGN KEY (`owner`) REFERENCES `user` (`idUser`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8 NOT NULL,
  `usergroup` varchar(10) CHARACTER SET utf8 NOT NULL DEFAULT 'REGISTRATO',
  `firstname` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lastname` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `name_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'imagedb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-06-24 15:01:12
