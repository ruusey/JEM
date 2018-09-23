CREATE DATABASE  IF NOT EXISTS `lb` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `lb`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: lb
-- ------------------------------------------------------
-- Server version	5.6.20

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
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `firstname` varchar(45) DEFAULT NULL,
  `lastname` varchar(45) DEFAULT NULL,
  `job_id` int(11) DEFAULT NULL,
  `geoloc_id` int(11) DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (1,'ruusey@gmail.com','ruusey','Robert','Usey',1,1,100),(2,NULL,'Ru\'s Soul',NULL,NULL,NULL,NULL,666);
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client_geoloc`
--

DROP TABLE IF EXISTS `client_geoloc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `client_geoloc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `geoloc_id` int(11) DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `datetime` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client_geoloc`
--

LOCK TABLES `client_geoloc` WRITE;
/*!40000 ALTER TABLE `client_geoloc` DISABLE KEYS */;
INSERT INTO `client_geoloc` VALUES (1,1,33.8283913,-84.4014042,'2017-06-17:15:57:26');
/*!40000 ALTER TABLE `client_geoloc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service` enum('LAWN_CARE','HOME_CARE','DELIVERY','MOVING_SERVICES','BABYSITTING','GENERAL_HELP','DELIVERY_SERVICES','CLEANING','PRESSURE_WASHING','DOG_SITTING','PROJECT_ASSISTANCE') DEFAULT NULL,
  `shortdescription` varchar(50) DEFAULT NULL,
  `longdescription` varchar(150) DEFAULT NULL,
  `geoloc_id` int(11) DEFAULT NULL,
  `pay` double DEFAULT NULL,
  `complete` tinyint(1) DEFAULT '0',
  `job_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (1,'MOVING_SERVICES','Mow my fucking lawn','Test Long Description',1,42,0,1),(2,'PRESSURE_WASHING','Mow my fucking lawn','Test Long Description',2,72,0,1),(3,'DOG_SITTING','Mow my fucking lawn','Test Long Description',3,52,0,1),(4,'PROJECT_ASSISTANCE','Mow my fucking lawn','Test Long Description',4,94,0,1),(5,'PROJECT_ASSISTANCE','Mow my fucking lawn','Test Long Description',5,95,0,1),(6,'PRESSURE_WASHING','Mow my fucking lawn','Test Long Description',6,49,0,1),(7,'PRESSURE_WASHING','Mow my fucking lawn','Test Long Description',7,50,0,1),(8,'BABYSITTING','Mow my fucking lawn','Test Long Description',8,76,0,1),(9,'BABYSITTING','Mow my fucking lawn','Test Long Description',9,82,0,1),(10,'MOVING_SERVICES','Mow my fucking lawn','Test Long Description',10,95,0,1);
/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_geoloc`
--

DROP TABLE IF EXISTS `job_geoloc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job_geoloc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `geoloc_id` int(11) DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `datetime` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_geoloc`
--

LOCK TABLES `job_geoloc` WRITE;
/*!40000 ALTER TABLE `job_geoloc` DISABLE KEYS */;
INSERT INTO `job_geoloc` VALUES (1,1,34.45290746314021,-83.77042084062998,'2017-06-21:13:53:38'),(2,2,33.92612226810659,-83.84008311350831,'2017-06-21:13:53:38'),(3,3,34.45992847232513,-84.3506301414073,'2017-06-21:13:53:38'),(4,4,34.60386229970991,-84.08019546709026,'2017-06-21:13:53:38'),(5,5,34.394219685512276,-83.72984819337844,'2017-06-21:13:53:38'),(6,6,33.75183603175556,-83.94270949887932,'2017-06-21:13:53:38'),(7,7,34.602589812207874,-83.82266844708337,'2017-06-21:13:53:39'),(8,8,34.06181699049833,-84.21770240294776,'2017-06-21:13:53:39'),(9,9,34.159132904762075,-84.21193041396552,'2017-06-21:13:53:39'),(10,10,33.92549725670893,-83.47489105850063,'2017-06-21:13:53:39');
/*!40000 ALTER TABLE `job_geoloc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_provider`
--

DROP TABLE IF EXISTS `service_provider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_provider` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `firstname` varchar(45) DEFAULT NULL,
  `lastname` varchar(45) DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  `geoloc_id` int(11) DEFAULT NULL,
  `rating` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_provider`
--

LOCK TABLES `service_provider` WRITE;
/*!40000 ALTER TABLE `service_provider` DISABLE KEYS */;
INSERT INTO `service_provider` VALUES (1,'ruusey@gmail.com','ruusey','Robert','Usey',1,1,0);
/*!40000 ALTER TABLE `service_provider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_provider_geoloc`
--

DROP TABLE IF EXISTS `service_provider_geoloc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_provider_geoloc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `geoloc_id` int(11) DEFAULT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `datetime` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_provider_geoloc`
--

LOCK TABLES `service_provider_geoloc` WRITE;
/*!40000 ALTER TABLE `service_provider_geoloc` DISABLE KEYS */;
INSERT INTO `service_provider_geoloc` VALUES (1,1,33.8283913,-84.4014042,'2017-06-17:15:57:26');
/*!40000 ALTER TABLE `service_provider_geoloc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_provider_service`
--

DROP TABLE IF EXISTS `service_provider_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service_provider_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` int(11) DEFAULT NULL,
  `service` enum('LAWN_CARE','HOME_CARE','DELIVERY','MOVING_SERVICES','BABYSITTING','GENERAL_HELP','DELIVERY_SERVICES','CLEANING','PRESSURE_WASHING','DOG_SITTING','PROJECT_ASSISTANCE') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_provider_service`
--

LOCK TABLES `service_provider_service` WRITE;
/*!40000 ALTER TABLE `service_provider_service` DISABLE KEYS */;
INSERT INTO `service_provider_service` VALUES (1,1,'LAWN_CARE'),(2,1,'HOME_CARE');
/*!40000 ALTER TABLE `service_provider_service` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-23 17:49:02
