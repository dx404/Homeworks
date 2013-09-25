-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: classroom.cs.unc.edu
-- Generation Time: Nov 17, 2012 at 11:22 PM
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
-- Table structure for table `CompareCleanness`
--

CREATE TABLE IF NOT EXISTS `CompareCleanness` (
  `searcherOption` enum('daily','weekly','never') NOT NULL,
  `searchee` enum('daily','weekly','never') NOT NULL,
  `score` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `CompareGender`
--

CREATE TABLE IF NOT EXISTS `CompareGender` (
  `searcherOption` enum('M','F') NOT NULL,
  `searchee` enum('M','F') NOT NULL,
  `score` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `CompareGender`
--

INSERT INTO `CompareGender` (`searcherOption`, `searchee`, `score`) VALUES
('M', 'M', 50),
('M', 'F', 80),
('F', 'M', 60),
('F', 'F', 100);

-- --------------------------------------------------------

--
-- Table structure for table `CompareMajor`
--

CREATE TABLE IF NOT EXISTS `CompareMajor` (
  `numOfMatches` int(11) NOT NULL,
  `score` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `CompareMusicListening`
--

CREATE TABLE IF NOT EXISTS `CompareMusicListening` (
  `searcherOption` enum('headphone','speaker') NOT NULL,
  `searchee` enum('headphone','speaker') NOT NULL,
  `score` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `CompareMusicListening`
--

INSERT INTO `CompareMusicListening` (`searcherOption`, `searchee`, `score`) VALUES
('headphone', 'headphone', 100),
('headphone', 'speaker', 0),
('speaker', 'headphone', 50),
('speaker', 'speaker', 80);

-- --------------------------------------------------------

--
-- Table structure for table `CompareVisitors`
--

CREATE TABLE IF NOT EXISTS `CompareVisitors` (
  `searcherOption` enum('freq','sometimes','no') NOT NULL,
  `searchee` enum('freq','sometimes','no') NOT NULL,
  `score` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Contact`
--

CREATE TABLE IF NOT EXISTS `Contact` (
  `uid` varchar(15) NOT NULL,
  `Call` varchar(20) NOT NULL,
  `Email` varchar(30) NOT NULL,
  `Facebook` varchar(30) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Contact`
--

INSERT INTO `Contact` (`uid`, `Call`, `Email`, `Facebook`) VALUES
('wisdompilot', '9196369669', 'duo.zhao@gmail.com', 'www.facebook.com/duo.zhao');

-- --------------------------------------------------------

--
-- Table structure for table `Dorm`
--

CREATE TABLE IF NOT EXISTS `Dorm` (
  `abbr` varchar(3) NOT NULL,
  `name` varchar(32) NOT NULL,
  `style` enum('Apartment','Suite','Corridor') NOT NULL,
  `gender` enum('male','female','coed') NOT NULL,
  `community` varchar(32) NOT NULL,
  `location` enum('N','M','S') NOT NULL,
  PRIMARY KEY (`abbr`),
  UNIQUE KEY `abbr` (`abbr`),
  KEY `abbr_2` (`abbr`),
  KEY `abbr_3` (`abbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Dorm`
--

INSERT INTO `Dorm` (`abbr`, `name`, `style`, `gender`, `community`, `location`) VALUES
('ACK', 'Aycock', 'Corridor', 'female', 'Olde Campus Lower Quad', 'N'),
('CRN', 'Craige North', 'Suite', 'coed', 'Manning West', 'S'),
('GHM', 'Graham', 'Corridor', 'male', 'Olde Campus Lower Quad', 'N'),
('HJA', 'Hinton James', 'Apartment', 'coed', 'Hinton James', 'S'),
('MFR', '1101 Mason FarmRoad', 'Apartment', 'coed', 'Odum Village', 'S'),
('MRS', 'Morrison', 'Suite', 'coed', 'Morrison Community', 'S'),
('ODE', 'Old East', 'Corridor', 'coed', 'Olde Campus Upper Quad', 'N'),
('ODM', 'Odum Village', 'Apartment', 'coed', 'Odum Village', 'S'),
('ODW', 'Old Wast', 'Corridor', 'coed', 'Olde Campus Upper Quad', 'N'),
('PKR', 'Parker', 'Suite', 'female', 'Parker', 'M'),
('RAM', 'Ram Village', 'Apartment', 'coed', 'Ram Village', 'S'),
('RFN', 'Ruffin', 'Corridor', 'coed', 'Olde Campus Upper Quad', 'N'),
('TGU', 'Teague', 'Suite', 'coed', 'Parker', 'M');

-- --------------------------------------------------------

--
-- Table structure for table `Major`
--

CREATE TABLE IF NOT EXISTS `Major` (
  `abbr` varchar(5) NOT NULL,
  `name` varchar(32) NOT NULL,
  `department` varchar(128) DEFAULT NULL,
  `school` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`abbr`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Major`
--

INSERT INTO `Major` (`abbr`, `name`, `department`, `school`) VALUES
('BCHM', 'Biochemistry', 'Biochemistry and Biophysics', 'Medicine'),
('BIOL', 'Biology', 'Biology', 'Arts and Sciences'),
('BIOS', 'Biostatistics', 'Biostatistics', 'Public Health'),
('BSBA', 'Business Administration', 'Business', 'Kenan-Flagler Business School'),
('BUJO', 'Business Journalism', 'Journalism and Mass Communication', 'Journalism and Mass Communication'),
('CHEM', 'Chemistry', 'Chemistry', 'Arts and Sciences'),
('COMM', 'Communication Studies', 'Communication Studies', 'Arts and Sciences'),
('CS', 'Computer Science', 'Computer Science', 'Arts and Sciences'),
('ECON', 'Economics', 'Economics', 'Arts and Sciences'),
('EE', 'Electronic Engineering', 'Computer Science', 'Arts and Sciences'),
('MATH', 'Mathematics', 'Mathematics', 'Arts and Sciences'),
('MDS', 'Mathematical Decision Science', 'Statistics and Operations Research', 'Arts and Sciences'),
('NUTR', 'Health Nutrition', 'Nutrition', 'Public Health'),
('OR', 'Operations Research', 'Statistics and Operations Research', 'Arts and Sciences'),
('PHIL', 'Philosophy', 'Philosophy', 'Arts and Sciences'),
('PHYS', 'Physics', 'Physics and Astronomy', 'Arts and Sciences'),
('PSYC', 'Psychology', 'Psychology', 'Arts and Sciences'),
('STAT', 'Statistics', 'Statistics and Operations Research', 'Arts and Sciences');

-- --------------------------------------------------------

--
-- Table structure for table `MatchedList`
--

CREATE TABLE IF NOT EXISTS `MatchedList` (
  `uid` varchar(15) NOT NULL,
  `roomieID` varchar(15) DEFAULT NULL,
  `score` double DEFAULT '99.99',
  KEY `uid` (`uid`),
  KEY `roomieID` (`roomieID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

-- --------------------------------------------------------

--
-- Table structure for table `Password`
--

CREATE TABLE IF NOT EXISTS `Password` (
  `uid` varchar(15) NOT NULL,
  `password` varchar(128) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `SearchHistory`
--

CREATE TABLE IF NOT EXISTS `SearchHistory` (
  `uid` varchar(15) NOT NULL,
  `gender` enum('M','F') NOT NULL,
  `ageLow` int(11) NOT NULL,
  `ageHigh` int(11) NOT NULL,
  `year` enum('fre','soph','jun','sen','other') NOT NULL,
  `major1` varchar(4) NOT NULL,
  `major2` varchar(4) NOT NULL,
  `major3` varchar(4) NOT NULL,
  `musicListening` enum('headphone','speaker') NOT NULL,
  `visitors` enum('freq','sometimes','no') NOT NULL,
  `cleanness` enum('daily','weekly','never') NOT NULL,
  `sleepTime` time NOT NULL DEFAULT '22:00:00',
  `wakeTime` time NOT NULL DEFAULT '08:00:00',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `SearchHistory`
--

INSERT INTO `SearchHistory` (`uid`, `gender`, `ageLow`, `ageHigh`, `year`, `major1`, `major2`, `major3`, `musicListening`, `visitors`, `cleanness`, `sleepTime`, `wakeTime`) VALUES
('wisdompilot', 'M', 18, 26, 'jun', 'MATH', 'CS', 'STAT', 'headphone', 'sometimes', 'weekly', '22:00:00', '08:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `uid` varchar(15) NOT NULL,
  `firstName` varchar(10) NOT NULL,
  `lastName` varchar(10) NOT NULL,
  `gender` enum('M','F') NOT NULL,
  `dateOfBirth` date NOT NULL DEFAULT '1990-01-01',
  `yearOfEnroll` year(4) DEFAULT '2012',
  `major` varchar(5) NOT NULL,
  `musicListening` enum('headphone','speaker') NOT NULL,
  `visitors` enum('freq','sometimes','no') NOT NULL,
  `cleanness` enum('daily','weekly','never') NOT NULL,
  `sleepTime` time NOT NULL DEFAULT '22:00:00',
  `wakeTime` time NOT NULL DEFAULT '08:00:00',
  `aboutMe` longtext NOT NULL,
  `profilePhotoURL` longtext NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `User`
--

INSERT INTO `User` (`uid`, `firstName`, `lastName`, `gender`, `dateOfBirth`, `yearOfEnroll`, `major`, `musicListening`, `visitors`, `cleanness`, `sleepTime`, `wakeTime`, `aboutMe`, `profilePhotoURL`) VALUES
('fencecraker', 'Ashley', 'James', 'F', '1990-02-21', 2009, 'CHEM', 'speaker', 'freq', 'daily', '22:00:00', '08:00:00', '', ''),
('spacepure', 'Alex', 'Green', 'M', '1990-01-18', 2011, 'CS', 'headphone', 'sometimes', 'weekly', '23:00:00', '09:00:00', 'a nice person', 'hello world'),
('wisdompilot', 'Duo', 'Zhao', 'M', '1988-01-19', 2010, 'MATH', 'headphone', 'sometimes', 'weekly', '01:00:00', '08:30:00', 'fadf', '/wisdompilot.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `UserDormInterest`
--

CREATE TABLE IF NOT EXISTS `UserDormInterest` (
  `uid` varchar(15) NOT NULL,
  `dorm` varchar(3) NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `dorm` (`dorm`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `UserMajorIn`
--

CREATE TABLE IF NOT EXISTS `UserMajorIn` (
  `uid` varchar(15) NOT NULL,
  `major` varchar(4) NOT NULL,
  KEY `major` (`major`),
  KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `vistorsCompare`
--

CREATE TABLE IF NOT EXISTS `vistorsCompare` (
  `searcherOption` enum('freq','sometimes','no') NOT NULL,
  `searchee` enum('freq','sometimes','no') NOT NULL,
  `score` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Contact`
--
ALTER TABLE `Contact`
  ADD CONSTRAINT `Contact_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`);

--
-- Constraints for table `MatchedList`
--
ALTER TABLE `MatchedList`
  ADD CONSTRAINT `MatchedList_ibfk_2` FOREIGN KEY (`roomieID`) REFERENCES `User` (`uid`),
  ADD CONSTRAINT `MatchedList_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`);

--
-- Constraints for table `MatchScore`
--
ALTER TABLE `MatchScore`
  ADD CONSTRAINT `MatchScore_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`),
  ADD CONSTRAINT `MatchScore_ibfk_2` FOREIGN KEY (`roomieID`) REFERENCES `User` (`uid`);

--
-- Constraints for table `Password`
--
ALTER TABLE `Password`
  ADD CONSTRAINT `Password_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`);

--
-- Constraints for table `SearchHistory`
--
ALTER TABLE `SearchHistory`
  ADD CONSTRAINT `SearchHistory_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`);

--
-- Constraints for table `UserDormInterest`
--
ALTER TABLE `UserDormInterest`
  ADD CONSTRAINT `UserDormInterest_ibfk_3` FOREIGN KEY (`dorm`) REFERENCES `Dorm` (`abbr`),
  ADD CONSTRAINT `UserDormInterest_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`),
  ADD CONSTRAINT `UserDormInterest_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`);

--
-- Constraints for table `UserMajorIn`
--
ALTER TABLE `UserMajorIn`
  ADD CONSTRAINT `UserMajorIn_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
