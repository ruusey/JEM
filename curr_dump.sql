-- MySQL dump 10.13  Distrib 5.6.20, for Win64 (x86_64)
--
-- Host: localhost    Database: lb
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
INSERT INTO `client` VALUES (1,'ruusey@gmail.com','exUser','Example','Client',1,1,100);
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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (1,'DOG_SITTING','Mow my fucking lawn','Test Long Description',1,75,0,1),(2,'PROJECT_ASSISTANCE','Mow my fucking lawn','Test Long Description',2,32,0,1),(3,'PROJECT_ASSISTANCE','Mow my fucking lawn','Test Long Description',3,98,0,1),(4,'CLEANING','Mow my fucking lawn','Test Long Description',4,84,0,1),(5,'HOME_CARE','Mow my fucking lawn','Test Long Description',5,93,0,1),(6,'DELIVERY_SERVICES','Mow my fucking lawn','Test Long Description',6,12,0,1),(7,'DELIVERY','Mow my fucking lawn','Test Long Description',7,58,0,1),(8,'DELIVERY_SERVICES','Mow my fucking lawn','Test Long Description',8,92,0,1),(9,'PROJECT_ASSISTANCE','Mow my fucking lawn','Test Long Description',9,94,0,1),(10,'CLEANING','Mow my fucking lawn','Test Long Description',10,27,0,1),(11,'DOG_SITTING','Mow my fucking lawn','Test Long Description',11,92,0,1),(12,'DELIVERY','Mow my fucking lawn','Test Long Description',12,50,0,1),(13,'HOME_CARE','Mow my fucking lawn','Test Long Description',13,92,0,1),(14,'HOME_CARE','Mow my fucking lawn','Test Long Description',14,6,0,1),(15,'DELIVERY_SERVICES','Mow my fucking lawn','Test Long Description',15,94,0,1),(16,'DELIVERY','Mow my fucking lawn','Test Long Description',16,2,0,1),(17,'GENERAL_HELP','Mow my fucking lawn','Test Long Description',17,6,0,1),(18,'CLEANING','Mow my fucking lawn','Test Long Description',18,58,0,1),(19,'MOVING_SERVICES','Mow my fucking lawn','Test Long Description',19,88,0,1),(20,'PROJECT_ASSISTANCE','Mow my fucking lawn','Test Long Description',20,39,0,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_geoloc`
--

LOCK TABLES `job_geoloc` WRITE;
/*!40000 ALTER TABLE `job_geoloc` DISABLE KEYS */;
INSERT INTO `job_geoloc` VALUES (11,1,34.11476303802274,-80.12752518323484,'2018-10-04:17:06:46'),(12,2,34.55588299439822,-80.53584943349959,'2018-10-04:17:06:46'),(13,3,34.547853557075356,-80.44090600871218,'2018-10-04:17:06:46'),(14,4,34.76920564961042,-80.72561856230924,'2018-10-04:17:06:46'),(15,5,34.85972707717184,-80.46751897876162,'2018-10-04:17:06:46'),(16,6,34.50999329038902,-80.48968031985409,'2018-10-04:17:06:46'),(17,7,34.64784823571869,-80.70897850653843,'2018-10-04:17:06:46'),(18,8,34.322862077280796,-80.195992946173,'2018-10-04:17:06:46'),(19,9,34.76095300312278,-81.00264971267417,'2018-10-04:17:06:46'),(20,10,33.989153825329865,-80.77253680877371,'2018-10-04:17:06:46'),(21,11,34.067077494147775,-80.96484693512302,'2018-10-04:17:06:46'),(22,12,34.40018992084909,-80.80360440566122,'2018-10-04:17:06:46'),(23,13,34.59067856379068,-80.33620722655849,'2018-10-04:17:06:46'),(24,14,34.87547941283903,-80.10526677399102,'2018-10-04:17:06:46'),(25,15,34.14741232347178,-80.09950754827695,'2018-10-04:17:06:46'),(26,16,34.023473733160756,-80.1944872587661,'2018-10-04:17:06:46'),(27,17,34.81050919110837,-80.92419503435671,'2018-10-04:17:06:46'),(28,18,34.68767120174067,-80.5830275794079,'2018-10-04:17:06:46'),(29,19,34.24652971133673,-80.74236993469299,'2018-10-04:17:06:46'),(30,20,34.85176414376841,-81.00369429449944,'2018-10-04:17:06:46');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_provider`
--

LOCK TABLES `service_provider` WRITE;
/*!40000 ALTER TABLE `service_provider` DISABLE KEYS */;
INSERT INTO `service_provider` VALUES (1,'ruusey@gmail.com','ruusey','Robert',' Usey',1,1,88),(2,'nmello7337@gmail.com','nmello','Natalie','Mello',2,2,22),(3,'melitotrevor@gmail.com','tmelito','Trevor',' Melito',3,3,69);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_provider_geoloc`
--

LOCK TABLES `service_provider_geoloc` WRITE;
/*!40000 ALTER TABLE `service_provider_geoloc` DISABLE KEYS */;
INSERT INTO `service_provider_geoloc` VALUES (1,1,33.994952306143105,-81.03156269836427,'2018-10-04:16:18:01'),(2,2,33.982,-81.037,'2018-10-03:19:30:29'),(3,3,33.97497775781825,-81.0201654157471,'2018-10-04:18:47:56');
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
) ENGINE=InnoDB AUTO_INCREMENT=519 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_provider_service`
--

LOCK TABLES `service_provider_service` WRITE;
/*!40000 ALTER TABLE `service_provider_service` DISABLE KEYS */;
INSERT INTO `service_provider_service` VALUES (439,2,'CLEANING'),(440,2,'CLEANING'),(441,2,'BABYSITTING'),(442,2,'CLEANING'),(513,1,'LAWN_CARE'),(514,1,'LAWN_CARE'),(515,1,'LAWN_CARE'),(516,1,'HOME_CARE'),(517,1,'HOME_CARE'),(518,1,'HOME_CARE');
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

-- Dump completed on 2018-10-04 18:51:02
