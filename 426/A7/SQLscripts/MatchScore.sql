-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: classroom.cs.unc.edu
-- Generation Time: Nov 17, 2012 at 11:21 PM
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
-- Table structure for table `MatchScore`
--

CREATE TABLE IF NOT EXISTS `MatchScore` (
  `uid` varchar(15) NOT NULL,
  `roomieID` varchar(10) NOT NULL,
  `gender` int(11) NOT NULL,
  `major` int(11) NOT NULL,
  `musicListening` int(11) NOT NULL,
  `visitors` int(11) NOT NULL,
  `cleanness` int(11) NOT NULL,
  `sleepTime` int(11) NOT NULL,
  `wakeTime` int(11) NOT NULL,
  `score` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `roomieID` (`roomieID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `MatchScore`
--
ALTER TABLE `MatchScore`
  ADD CONSTRAINT `MatchScore_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`),
  ADD CONSTRAINT `MatchScore_ibfk_2` FOREIGN KEY (`roomieID`) REFERENCES `User` (`uid`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
