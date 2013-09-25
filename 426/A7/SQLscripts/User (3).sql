-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: classroom.cs.unc.edu
-- Generation Time: Nov 25, 2012 at 05:46 PM
-- Server version: 5.1.61
-- PHP Version: 5.3.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `comp42617db`
--

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `uid` varchar(15) NOT NULL,
  `firstName` varchar(10) NOT NULL,
  `lastName` varchar(10) NOT NULL,
  `gender` enum('M','F') NOT NULL,
  `DOB` date NOT NULL DEFAULT '1990-01-01',
  `year` varchar(3) NOT NULL,
  `musicListening` varchar(16) NOT NULL,
  `visitors` varchar(16) NOT NULL,
  `cleanness` varchar(16) NOT NULL,
  `sleepTime` varchar(16) NOT NULL,
  `wakeTime` varchar(16) NOT NULL,
  `aboutMe` longtext NOT NULL,
  `profilePhotoURL` longtext NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `year` (`year`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `User`
--

INSERT INTO `User` (`uid`, `firstName`, `lastName`, `gender`, `DOB`, `year`, `musicListening`, `visitors`, `cleanness`, `sleepTime`, `wakeTime`, `aboutMe`, `profilePhotoURL`) VALUES
('fencecraker', 'Ashley', 'James', 'F', '1990-02-21', 'GR', 'speaker', 'no', 'daily', '12-2', '7-9', '', ''),
('spacepure', 'Alex', 'Green', 'M', '1990-01-18', 'SO', 'headphone', 'no', 'weekly', 'after2', 'after9', 'a nice person', 'hello world'),
('wisdompilot', 'Duo', 'Zhao', 'M', '1988-01-19', 'JR', 'headphone', 'freq', 'weekly', 'before12', 'before7', 'fadf', '/wisdompilot.jpg');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
