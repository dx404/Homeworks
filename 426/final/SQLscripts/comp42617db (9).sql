-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: classroom.cs.unc.edu
-- Generation Time: Dec 09, 2012 at 04:55 PM
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
-- Table structure for table `beta`
--

CREATE TABLE IF NOT EXISTS `beta` (
  `uid` varchar(10) NOT NULL,
  `name` varchar(10) NOT NULL,
  `call` varchar(10) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `beta`
--

INSERT INTO `beta` (`uid`, `name`, `call`) VALUES
('fence', 'Fence', '636');

-- --------------------------------------------------------

--
-- Table structure for table `Cleanness`
--

CREATE TABLE IF NOT EXISTS `Cleanness` (
  `op` varchar(16) NOT NULL,
  `val` int(11) NOT NULL,
  PRIMARY KEY (`op`(1)),
  UNIQUE KEY `op` (`op`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Cleanness`
--

INSERT INTO `Cleanness` (`op`, `val`) VALUES
('daily', 3),
('never', 1),
('weekly', 2);

-- --------------------------------------------------------

--
-- Stand-in structure for view `CompareCleanness`
--
CREATE TABLE IF NOT EXISTS `CompareCleanness` (
`searcherOption` varchar(16)
,`searchee` varchar(16)
,`score` bigint(14)
);
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
('M', 'M', 10),
('M', 'F', 2),
('F', 'M', 2),
('F', 'F', 10);

-- --------------------------------------------------------

--
-- Table structure for table `CompareMajor`
--

CREATE TABLE IF NOT EXISTS `CompareMajor` (
  `numOfMatches` int(11) NOT NULL,
  `score` int(11) NOT NULL,
  PRIMARY KEY (`numOfMatches`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `CompareMajor`
--

INSERT INTO `CompareMajor` (`numOfMatches`, `score`) VALUES
(0, 2),
(1, 5),
(2, 8),
(3, 10);

-- --------------------------------------------------------

--
-- Stand-in structure for view `CompareMusicListening`
--
CREATE TABLE IF NOT EXISTS `CompareMusicListening` (
`searcherOption` varchar(16)
,`searchee` varchar(16)
,`score` bigint(14)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `CompareSleepTime`
--
CREATE TABLE IF NOT EXISTS `CompareSleepTime` (
`searcherOption` varchar(16)
,`searchee` varchar(16)
,`score` bigint(14)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `CompareVisitors`
--
CREATE TABLE IF NOT EXISTS `CompareVisitors` (
`searcherOption` varchar(16)
,`searchee` varchar(16)
,`score` bigint(14)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `CompareWakeTime`
--
CREATE TABLE IF NOT EXISTS `CompareWakeTime` (
`searcherOption` varchar(16)
,`searchee` varchar(16)
,`score` bigint(14)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `CompareYear`
--
CREATE TABLE IF NOT EXISTS `CompareYear` (
`searcherOption` varchar(3)
,`searchee` varchar(3)
,`score` bigint(14)
);
-- --------------------------------------------------------

--
-- Table structure for table `Contact`
--

CREATE TABLE IF NOT EXISTS `Contact` (
  `uid` varchar(15) NOT NULL,
  `call` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `facebook` varchar(30) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Contact`
--

INSERT INTO `Contact` (`uid`, `call`, `email`, `facebook`) VALUES
('haoyangh', '', '', ''),
('isabella', '', '', ''),
('spicydragon', '9198888888', '', ''),
('xuanc', '123456', '', ''),
('yier', '', 'isabellahaoyang@gmail.com', '');

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
('NULL', 'NULL', 'NULL', 'NULL'),
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
-- Table structure for table `MusicListening`
--

CREATE TABLE IF NOT EXISTS `MusicListening` (
  `op` varchar(16) NOT NULL,
  `val` int(11) NOT NULL,
  PRIMARY KEY (`op`(1)),
  UNIQUE KEY `op` (`op`),
  KEY `op_2` (`op`),
  KEY `op_3` (`op`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `MusicListening`
--

INSERT INTO `MusicListening` (`op`, `val`) VALUES
('headphone', 1),
('speaker', 2);

-- --------------------------------------------------------

--
-- Table structure for table `Password`
--

CREATE TABLE IF NOT EXISTS `Password` (
  `uid` varchar(15) NOT NULL,
  `password` varchar(128) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Password`
--

INSERT INTO `Password` (`uid`, `password`) VALUES
('32423423', '170c8f98df4fc750172701db74672be82f3dea8ac06edc9'),
('Amyf', 'a8b632549b51939971dff6a6be357fe41fd5ebccd8ed889'),
('cracker001', 'd204bdb6ffe36a4f0cd418dcb5fc7e9fbb29bc4af8f19fd'),
('cracker007', 'c67d428ea0171c7efee253ea4686d6bb6dc7d602321dc53'),
('duo', 'f216811f709de5ab0ad292c965d392061a84ed6ed94d36c'),
('duo2', '348ffb9c97a9b5f5f9eed2daa269edaf9387403af56a516'),
('duo3', '22c66b21e8e4294108ea0f6ca72c43cbfaee7e8604adea6'),
('duoduo', 'b3452bd3351fb5502396748a72d4b7b8fe5eb62b6f35a0a'),
('fence002', '5920d3e646d8f9225c6cebab9f2bc4b3e0d8f4f85d8325a'),
('fence003', '169e14e11b191502789275ee6bc464a1ec838b71b86eb9d'),
('fencecracker', 'e109188249fe3f7a3c0b576e21a23ce36031a20f86fad67'),
('haoyangh', '634be75b28617aa63a88a0db2ec6009e4e0f5b8a5f0bd88'),
('helloWorld', 'a48ee631dd2aa8040dcc3d62c75540716e739559b923e78'),
('helloWorld2', '74ee9ab4fbafc733a9344bd0bf38f097834a866aee944e1'),
('huiweishen', '7f11cc8f9559976250cda9bc1809dcc17f6c17521fd2c7a'),
('isabella', '53ed5111b596427f9320bd320829560ae3899692485ca72'),
('Kmyf', '5ce5e15ec2e426a601b6419b910b6e496eb7cd2731497f4'),
('spacepure', '3af81ca5d317b0d2ad990fce84bf6ea24b30b1f7289324c'),
('spicydragon', 'da9d50f75b09078b9f58d1837e6e4e4c3e591f8e7e8deca'),
('titan', 'ad583718eced024752d22d4375189ca387ea5583edfddd7'),
('Tmyf', 'c9a8142bce563893dead7b742fe9d007538d97c5307dd6c'),
('Xmyf', '6a67c93a1b6c208c5d2074e426ca5b2ca777f7c15d94711'),
('xuanc', 'f7857cdcf624ecb3a0a5360b33d13b22680c3164696269e'),
('yier', '1ece15b4127b8cdbed72236fc9ba5276687576907714fe6'),
('Ymyf', 'afc0aa556a73f5c5c805867d58f267307b35bf2b0143316');

-- --------------------------------------------------------

--
-- Stand-in structure for view `ScoreAge`
--
CREATE TABLE IF NOT EXISTS `ScoreAge` (
`searcherID` varchar(15)
,`searcheeID` varchar(15)
,`score` int(2)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `ScoreCleanness`
--
CREATE TABLE IF NOT EXISTS `ScoreCleanness` (
`searcherID` varchar(15)
,`searcheeID` varchar(15)
,`score` bigint(14)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `ScoreGender`
--
CREATE TABLE IF NOT EXISTS `ScoreGender` (
`searcherID` varchar(15)
,`searcheeID` varchar(15)
,`score` int(11)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `ScoreMajor`
--
CREATE TABLE IF NOT EXISTS `ScoreMajor` (
`searcherID` varchar(15)
,`searcheeID` varchar(15)
,`score` bigint(23)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `ScoreMusicListening`
--
CREATE TABLE IF NOT EXISTS `ScoreMusicListening` (
`searcherID` varchar(15)
,`searcheeID` varchar(15)
,`score` bigint(14)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `ScoreSleepTime`
--
CREATE TABLE IF NOT EXISTS `ScoreSleepTime` (
`searcherID` varchar(15)
,`searcheeID` varchar(15)
,`score` bigint(14)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `ScoreTotal`
--
CREATE TABLE IF NOT EXISTS `ScoreTotal` (
`searcherID` varchar(15)
,`searcheeID` varchar(15)
,`gender` int(11)
,`age` int(2)
,`year` bigint(14)
,`major` bigint(20)
,`musicListening` bigint(14)
,`visitors` bigint(14)
,`cleanness` bigint(14)
,`sleepTime` bigint(14)
,`wakeTime` bigint(14)
,`total` bigint(28)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `ScoreVisitors`
--
CREATE TABLE IF NOT EXISTS `ScoreVisitors` (
`searcherID` varchar(15)
,`searcheeID` varchar(15)
,`score` bigint(14)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `ScoreWakeTime`
--
CREATE TABLE IF NOT EXISTS `ScoreWakeTime` (
`searcherID` varchar(15)
,`searcheeID` varchar(15)
,`score` bigint(14)
);
-- --------------------------------------------------------

--
-- Stand-in structure for view `ScoreYear`
--
CREATE TABLE IF NOT EXISTS `ScoreYear` (
`searcherID` varchar(15)
,`searcheeID` varchar(15)
,`score` bigint(14)
);
-- --------------------------------------------------------

--
-- Table structure for table `SearchHistory`
--

CREATE TABLE IF NOT EXISTS `SearchHistory` (
  `uid` varchar(15) NOT NULL,
  `gender` enum('M','F') NOT NULL,
  `ageLow` int(11) NOT NULL,
  `ageHigh` int(11) NOT NULL,
  `year` varchar(16) NOT NULL,
  `major1` varchar(4) NOT NULL,
  `major2` varchar(4) NOT NULL,
  `major3` varchar(4) NOT NULL,
  `musicListening` varchar(16) NOT NULL,
  `visitors` varchar(16) NOT NULL,
  `cleanness` varchar(16) NOT NULL,
  `sleepTime` varchar(16) NOT NULL,
  `wakeTime` varchar(16) NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `major1` (`major1`),
  KEY `major2` (`major2`),
  KEY `major3` (`major3`),
  KEY `year` (`year`),
  KEY `visitors` (`visitors`),
  KEY `sleepTime` (`sleepTime`),
  KEY `wakeTime` (`wakeTime`),
  KEY `cleanness` (`cleanness`),
  KEY `musicListening` (`musicListening`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `SearchHistory`
--

INSERT INTO `SearchHistory` (`uid`, `gender`, `ageLow`, `ageHigh`, `year`, `major1`, `major2`, `major3`, `musicListening`, `visitors`, `cleanness`, `sleepTime`, `wakeTime`) VALUES
('Amyf', 'F', 17, 28, 'SO', 'BCHM', 'BCHM', 'BCHM', 'headphone', 'no', 'daily', 'before12', 'before7'),
('cracker001', 'F', 17, 28, 'SO', 'BCHM', 'BCHM', 'BCHM', 'headphone', 'no', 'daily', 'before12', 'before7'),
('cracker007', 'F', 17, 28, 'SO', 'BCHM', 'BCHM', 'BCHM', 'headphone', 'no', 'daily', 'before12', 'before7'),
('isabella', 'F', 17, 28, 'SR', 'BUJO', 'COMM', 'BCHM', 'speaker', 'no', 'daily', 'after2', 'before7'),
('spacepure', 'F', 23, 27, 'SO', 'COMM', 'MDS', 'CS', 'headphone', 'sometimes', 'daily', '12-2', '7-9'),
('spicydragon', 'F', 17, 28, 'SO', 'BCHM', 'BCHM', 'BCHM', 'headphone', 'no', 'daily', 'before12', 'before7'),
('xuanc', 'F', 17, 28, 'SR', 'CS', 'BSBA', 'BCHM', 'speaker', 'sometimes', 'daily', '12-2', 'before7'),
('yier', 'F', 17, 28, 'SO', 'BCHM', 'BCHM', 'BCHM', 'headphone', 'no', 'daily', 'before12', 'before7');

-- --------------------------------------------------------

--
-- Table structure for table `SleepTime`
--

CREATE TABLE IF NOT EXISTS `SleepTime` (
  `op` varchar(16) NOT NULL,
  `val` int(11) NOT NULL,
  PRIMARY KEY (`op`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `SleepTime`
--

INSERT INTO `SleepTime` (`op`, `val`) VALUES
('12-2', 2),
('after2', 3),
('before12', 1);

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
  `major1` varchar(6) NOT NULL,
  `major2` varchar(6) DEFAULT NULL,
  `musicListening` varchar(16) NOT NULL,
  `visitors` varchar(16) NOT NULL,
  `cleanness` varchar(16) NOT NULL,
  `sleepTime` varchar(16) NOT NULL,
  `wakeTime` varchar(16) NOT NULL,
  `call` varchar(20) DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  `facebook` varchar(20) DEFAULT NULL,
  `aboutMe` longtext NOT NULL,
  `profilePhotoURL` longtext NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `year` (`year`),
  KEY `visitors` (`visitors`),
  KEY `sleepTime` (`sleepTime`),
  KEY `cleanness` (`cleanness`),
  KEY `wakeTime` (`wakeTime`),
  KEY `uid` (`uid`),
  KEY `musicListening` (`musicListening`),
  KEY `uid_2` (`uid`),
  KEY `major1` (`major1`),
  KEY `major2` (`major2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `User`
--

INSERT INTO `User` (`uid`, `firstName`, `lastName`, `gender`, `DOB`, `year`, `major1`, `major2`, `musicListening`, `visitors`, `cleanness`, `sleepTime`, `wakeTime`, `call`, `email`, `facebook`, `aboutMe`, `profilePhotoURL`) VALUES
('Amyf', 'Amy', 'f', 'F', '1990-01-01', 'JR', 'CHEM', 'BIOS', 'speaker', 'freq', 'weekly', '12-2', 'after9', NULL, NULL, NULL, 'a nice person', ''),
('cracker007', 'Fence VII', 'Cracker', 'M', '1989-07-04', 'FR', 'BSBA', 'COMM', 'speaker', 'no', 'daily', 'after2', 'after9', '919-987-6543', 'cracker@gmail.com', 'cracker@fb.com', 'I am the cracker 7..', '						http://i943.photobucket.com/albums/ad272/spacepure/homelesstoharvard.jpg\r\n						'),
('duo', 'Xence', 'Cracker', 'M', '1999-11-19', 'GR', 'MATH', 'EE', 'speaker', 'freq', 'weekly', '12-2', '7-9', '919-666-9999', 'abc@gmail.com', 'abc@fb.com', 'very nice', 'hello.jpg'),
('fencecracker', 'Fence', 'Cracker', 'M', '1990-01-01', 'SO', 'EE', 'CS', 'headphone', 'no', 'daily', 'after2', 'after9', NULL, NULL, NULL, 'a nice person', 'https://si0.twimg.com/profile_images/2649571860/910e545d7537be6148b7923aa86d2144.png'),
('haoyangh', 'Isabella', 'Huang', 'F', '0000-00-00', 'FR', 'BCHM', NULL, 'headphone', 'no', 'daily', 'before12', 'before7', NULL, NULL, NULL, 'about', 'http://sphotos-a.xx.fbcdn.net/hphotos-prn1/c0.0.403.403/p403x403/28067_10151258409523254_340971923_n.jpg'),
('huiweishen', 'Hui', 'Weishen', 'M', '2002-11-03', 'FR', 'CS', 'EE', 'speaker', 'no', 'daily', 'before12', 'before7', NULL, NULL, NULL, 'about', './photos/littleEngineerAvatar.jpg'),
('isabella', 'Isabella', 'Huang', 'M', '0000-00-00', 'GR', 'MDS', NULL, 'headphone', 'no', 'daily', 'before12', 'before7', NULL, NULL, NULL, 'Say something about yourself', 'http://www.'),
('Kmyf', 'K', 'myf', 'F', '1990-01-01', 'SO', 'BIOL', 'BIOL', 'speaker', 'sometimes', 'daily', 'after2', '7-9', NULL, NULL, NULL, 'nice', ''),
('spacepure', 'Alex', 'Green', 'M', '1990-01-18', 'SO', 'CS', 'EE', 'headphone', 'no', 'weekly', 'after2', 'after9', NULL, NULL, NULL, 'a nice person', './photos/littleEngineerAvatar.jpg'),
('spicydragon', 'Spicy', 'Dragon', 'M', '0000-00-00', 'GR', 'BUJO', 'BUJO', 'headphone', 'no', 'daily', 'before12', 'before7', NULL, NULL, NULL, 'Say something about yourself', 'https://si0.twimg.com/profile_images/2649571860/910e545d7537be6148b7923aa86d2144.png'),
('titan', 'TTTTT587', 'TAAAAA3', 'M', '0000-00-00', 'FR', 'BCHM', NULL, 'headphone', 'no', 'never', 'before12', 'before7', '1010', '1212', '112312', 'where are you', 'http://www.'),
('Xmyf', 'Xence', 'f', 'M', '1999-11-19', 'SO', 'MATH', 'EE', 'speaker', 'freq', 'weekly', '12-2', '7-9', '919-666-9999', 'abc@gmail.com', 'abc@fb.com', 'very nice', 'hello.jpg'),
('xuanc', 'Xuan', 'Chai', 'M', '2012-12-12', 'SR', 'STAT', 'ECON', 'headphone', 'no', 'daily', 'before12', 'before7', NULL, NULL, NULL, 'about', 'http://data.yule.sohu.com/star/pic/star/26/26181/default.jpg'),
('yier', 'Yier', 'Cao', 'M', '0000-00-00', 'FR', 'BCHM', NULL, 'headphone', 'no', 'daily', 'before12', 'before7', NULL, NULL, NULL, 'Say something about yourself', 'http://upload.wikimedia.org/wikipedia/commons/thumb/c/c0/Lin_Chi-Ling_(cropped).jpg/240px-Lin_Chi-Ling_(cropped).jpg');

--
-- Triggers `User`
--
DROP TRIGGER IF EXISTS `insert_major2_null`;
DELIMITER //
CREATE TRIGGER `insert_major2_null` BEFORE INSERT ON `User`
 FOR EACH ROW BEGIN
			IF NEW.major2 = 'NULL' THEN
				SET NEW.major2 = null;
			END IF;
		END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `upd_major2_null`;
DELIMITER //
CREATE TRIGGER `upd_major2_null` BEFORE UPDATE ON `User`
 FOR EACH ROW BEGIN
			IF NEW.major2 = 'NULL' THEN
				SET NEW.major2 = null;
			END IF;
		END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `UserDormInterest`
--

CREATE TABLE IF NOT EXISTS `UserDormInterest` (
  `uid` varchar(15) NOT NULL,
  `dorm` varchar(3) NOT NULL,
  KEY `dorm` (`dorm`),
  KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `UserDormInterest`
--

INSERT INTO `UserDormInterest` (`uid`, `dorm`) VALUES
('spacepure', 'MFR'),
('fencecracker', 'MFR'),
('fencecracker', 'RAM'),
('fencecracker', 'PKR'),
('fencecracker', 'RFN'),
('cracker001', 'TGU'),
('cracker001', 'ODE'),
('cracker001', 'ODW'),
('cracker007', 'ODM'),
('cracker007', 'MRS'),
('cracker007', 'HJA'),
('haoyangh', 'RAM'),
('haoyangh', 'ODM'),
('spicydragon', 'ACK'),
('spicydragon', 'ODM'),
('yier', 'MFR'),
('yier', 'PKR'),
('yier', 'RAM'),
('titan', 'MRS'),
('titan', 'ODM'),
('xuanc', 'ODM'),
('xuanc', 'MRS');

-- --------------------------------------------------------

--
-- Stand-in structure for view `UserMajorIn`
--
CREATE TABLE IF NOT EXISTS `UserMajorIn` (
`uid` varchar(15)
,`major` varchar(6)
);
-- --------------------------------------------------------

--
-- Table structure for table `Visitors`
--

CREATE TABLE IF NOT EXISTS `Visitors` (
  `op` varchar(16) NOT NULL,
  `val` int(11) NOT NULL,
  PRIMARY KEY (`op`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Visitors`
--

INSERT INTO `Visitors` (`op`, `val`) VALUES
('freq', 3),
('no', 1),
('sometimes', 2);

-- --------------------------------------------------------

--
-- Table structure for table `WakeTime`
--

CREATE TABLE IF NOT EXISTS `WakeTime` (
  `op` varchar(16) NOT NULL,
  `val` int(11) NOT NULL,
  PRIMARY KEY (`op`(1)),
  UNIQUE KEY `op` (`op`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `WakeTime`
--

INSERT INTO `WakeTime` (`op`, `val`) VALUES
('7-9', 2),
('after9', 3),
('before7', 1);

-- --------------------------------------------------------

--
-- Table structure for table `Year`
--

CREATE TABLE IF NOT EXISTS `Year` (
  `abbr` varchar(3) NOT NULL,
  `val` int(11) NOT NULL,
  PRIMARY KEY (`abbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Year`
--

INSERT INTO `Year` (`abbr`, `val`) VALUES
('FR', 1),
('GR', 5),
('JR', 3),
('SO', 2),
('SR', 4);

-- --------------------------------------------------------

--
-- Structure for view `CompareCleanness`
--
DROP TABLE IF EXISTS `CompareCleanness`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `CompareCleanness` AS select `CL1`.`op` AS `searcherOption`,`CL2`.`op` AS `searchee`,(10 - (4 * abs((`CL1`.`val` - `CL2`.`val`)))) AS `score` from (`Cleanness` `CL1` join `Cleanness` `CL2`);

-- --------------------------------------------------------

--
-- Structure for view `CompareMusicListening`
--
DROP TABLE IF EXISTS `CompareMusicListening`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `CompareMusicListening` AS select `ML1`.`op` AS `searcherOption`,`ML2`.`op` AS `searchee`,(10 - (8 * abs((`ML1`.`val` - `ML2`.`val`)))) AS `score` from (`MusicListening` `ML1` join `MusicListening` `ML2`);

-- --------------------------------------------------------

--
-- Structure for view `CompareSleepTime`
--
DROP TABLE IF EXISTS `CompareSleepTime`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `CompareSleepTime` AS select `ST1`.`op` AS `searcherOption`,`ST2`.`op` AS `searchee`,(10 - (4 * abs((`ST1`.`val` - `ST2`.`val`)))) AS `score` from (`SleepTime` `ST1` join `SleepTime` `ST2`);

-- --------------------------------------------------------

--
-- Structure for view `CompareVisitors`
--
DROP TABLE IF EXISTS `CompareVisitors`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `CompareVisitors` AS select `V1`.`op` AS `searcherOption`,`V2`.`op` AS `searchee`,(10 - (4 * abs((`V1`.`val` - `V2`.`val`)))) AS `score` from (`Visitors` `V1` join `Visitors` `V2`);

-- --------------------------------------------------------

--
-- Structure for view `CompareWakeTime`
--
DROP TABLE IF EXISTS `CompareWakeTime`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `CompareWakeTime` AS select `WT1`.`op` AS `searcherOption`,`WT2`.`op` AS `searchee`,(10 - (4 * abs((`WT1`.`val` - `WT2`.`val`)))) AS `score` from (`WakeTime` `WT1` join `WakeTime` `WT2`);

-- --------------------------------------------------------

--
-- Structure for view `CompareYear`
--
DROP TABLE IF EXISTS `CompareYear`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `CompareYear` AS select `Y1`.`abbr` AS `searcherOption`,`Y2`.`abbr` AS `searchee`,(10 - (2 * abs((`Y1`.`val` - `Y2`.`val`)))) AS `score` from (`Year` `Y1` join `Year` `Y2`);

-- --------------------------------------------------------

--
-- Structure for view `ScoreAge`
--
DROP TABLE IF EXISTS `ScoreAge`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `ScoreAge` AS select `SearchHistory`.`uid` AS `searcherID`,`User`.`uid` AS `searcheeID`,if((((to_days(`User`.`DOB`) - to_days((utc_date() - interval `SearchHistory`.`ageHigh` year))) > 0) and ((to_days(`User`.`DOB`) - to_days((utc_date() - interval `SearchHistory`.`ageLow` year))) < 0)),10,2) AS `score` from (`SearchHistory` join `User`) where (`SearchHistory`.`uid` <> `User`.`uid`);

-- --------------------------------------------------------

--
-- Structure for view `ScoreCleanness`
--
DROP TABLE IF EXISTS `ScoreCleanness`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `ScoreCleanness` AS select `SearchHistory`.`uid` AS `searcherID`,`User`.`uid` AS `searcheeID`,`CompareCleanness`.`score` AS `score` from ((`SearchHistory` join `User`) join `CompareCleanness`) where ((`SearchHistory`.`uid` <> `User`.`uid`) and (`SearchHistory`.`cleanness` = `CompareCleanness`.`searcherOption`) and (`User`.`cleanness` = `CompareCleanness`.`searchee`));

-- --------------------------------------------------------

--
-- Structure for view `ScoreGender`
--
DROP TABLE IF EXISTS `ScoreGender`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `ScoreGender` AS select `SearchHistory`.`uid` AS `searcherID`,`User`.`uid` AS `searcheeID`,`CompareGender`.`score` AS `score` from ((`SearchHistory` join `User`) join `CompareGender`) where ((`SearchHistory`.`uid` <> `User`.`uid`) and (`SearchHistory`.`gender` = `CompareGender`.`searcherOption`) and (`User`.`gender` = `CompareGender`.`searchee`));

-- --------------------------------------------------------

--
-- Structure for view `ScoreMajor`
--
DROP TABLE IF EXISTS `ScoreMajor`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `ScoreMajor` AS select `SearchHistory`.`uid` AS `searcherID`,`UserMajorIn`.`uid` AS `searcheeID`,(1 + (3 * count(0))) AS `score` from (`SearchHistory` join `UserMajorIn`) where ((`SearchHistory`.`uid` <> `UserMajorIn`.`uid`) and ((`SearchHistory`.`major1` = `UserMajorIn`.`major`) or (`SearchHistory`.`major2` = `UserMajorIn`.`major`) or (`SearchHistory`.`major3` = `UserMajorIn`.`major`))) group by `SearchHistory`.`uid`,`UserMajorIn`.`uid`;

-- --------------------------------------------------------

--
-- Structure for view `ScoreMusicListening`
--
DROP TABLE IF EXISTS `ScoreMusicListening`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `ScoreMusicListening` AS select `SearchHistory`.`uid` AS `searcherID`,`User`.`uid` AS `searcheeID`,`CompareMusicListening`.`score` AS `score` from ((`SearchHistory` join `User`) join `CompareMusicListening`) where ((`SearchHistory`.`uid` <> `User`.`uid`) and (`SearchHistory`.`musicListening` = `CompareMusicListening`.`searcherOption`) and (`User`.`musicListening` = `CompareMusicListening`.`searchee`));

-- --------------------------------------------------------

--
-- Structure for view `ScoreSleepTime`
--
DROP TABLE IF EXISTS `ScoreSleepTime`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `ScoreSleepTime` AS select `SearchHistory`.`uid` AS `searcherID`,`User`.`uid` AS `searcheeID`,`CompareSleepTime`.`score` AS `score` from ((`SearchHistory` join `User`) join `CompareSleepTime`) where ((`SearchHistory`.`uid` <> `User`.`uid`) and (`SearchHistory`.`sleepTime` = `CompareSleepTime`.`searcherOption`) and (`User`.`sleepTime` = `CompareSleepTime`.`searchee`));

-- --------------------------------------------------------

--
-- Structure for view `ScoreTotal`
--
DROP TABLE IF EXISTS `ScoreTotal`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `ScoreTotal` AS select `ScoreGender`.`searcherID` AS `searcherID`,`ScoreGender`.`searcheeID` AS `searcheeID`,`ScoreGender`.`score` AS `gender`,`ScoreAge`.`score` AS `age`,`ScoreYear`.`score` AS `year`,if(isnull(`ScoreMajor`.`score`),0,`ScoreMajor`.`score`) AS `major`,`ScoreMusicListening`.`score` AS `musicListening`,`ScoreVisitors`.`score` AS `visitors`,`ScoreCleanness`.`score` AS `cleanness`,`ScoreSleepTime`.`score` AS `sleepTime`,`ScoreWakeTime`.`score` AS `wakeTime`,((((((((`ScoreGender`.`score` + `ScoreYear`.`score`) + `ScoreYear`.`score`) + if(isnull(`ScoreMajor`.`score`),0,`ScoreMajor`.`score`)) + `ScoreMusicListening`.`score`) + `ScoreVisitors`.`score`) + `ScoreCleanness`.`score`) + `ScoreSleepTime`.`score`) + `ScoreWakeTime`.`score`) AS `total` from ((((((((`ScoreGender` left join `ScoreAge` on(((`ScoreGender`.`searcherID` = `ScoreAge`.`searcherID`) and (`ScoreGender`.`searcheeID` = `ScoreAge`.`searcheeID`)))) left join `ScoreYear` on(((`ScoreGender`.`searcherID` = `ScoreYear`.`searcherID`) and (`ScoreGender`.`searcheeID` = `ScoreYear`.`searcheeID`)))) left join `ScoreMajor` on(((`ScoreGender`.`searcherID` = `ScoreMajor`.`searcherID`) and (`ScoreGender`.`searcheeID` = `ScoreMajor`.`searcheeID`)))) left join `ScoreMusicListening` on(((`ScoreGender`.`searcherID` = `ScoreMusicListening`.`searcherID`) and (`ScoreGender`.`searcheeID` = `ScoreMusicListening`.`searcheeID`)))) left join `ScoreVisitors` on(((`ScoreGender`.`searcherID` = `ScoreVisitors`.`searcherID`) and (`ScoreGender`.`searcheeID` = `ScoreVisitors`.`searcheeID`)))) left join `ScoreCleanness` on(((`ScoreGender`.`searcherID` = `ScoreCleanness`.`searcherID`) and (`ScoreGender`.`searcheeID` = `ScoreCleanness`.`searcheeID`)))) left join `ScoreSleepTime` on(((`ScoreGender`.`searcherID` = `ScoreSleepTime`.`searcherID`) and (`ScoreGender`.`searcheeID` = `ScoreSleepTime`.`searcheeID`)))) left join `ScoreWakeTime` on(((`ScoreGender`.`searcherID` = `ScoreWakeTime`.`searcherID`) and (`ScoreGender`.`searcheeID` = `ScoreWakeTime`.`searcheeID`)))) order by `ScoreGender`.`searcherID`,((((((((`ScoreGender`.`score` + `ScoreYear`.`score`) + `ScoreYear`.`score`) + if(isnull(`ScoreMajor`.`score`),0,`ScoreMajor`.`score`)) + `ScoreMusicListening`.`score`) + `ScoreVisitors`.`score`) + `ScoreCleanness`.`score`) + `ScoreSleepTime`.`score`) + `ScoreWakeTime`.`score`) desc;

-- --------------------------------------------------------

--
-- Structure for view `ScoreVisitors`
--
DROP TABLE IF EXISTS `ScoreVisitors`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `ScoreVisitors` AS select `SearchHistory`.`uid` AS `searcherID`,`User`.`uid` AS `searcheeID`,`CompareVisitors`.`score` AS `score` from ((`SearchHistory` join `User`) join `CompareVisitors`) where ((`SearchHistory`.`uid` <> `User`.`uid`) and (`SearchHistory`.`visitors` = `CompareVisitors`.`searcherOption`) and (`User`.`visitors` = `CompareVisitors`.`searchee`));

-- --------------------------------------------------------

--
-- Structure for view `ScoreWakeTime`
--
DROP TABLE IF EXISTS `ScoreWakeTime`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `ScoreWakeTime` AS select `SearchHistory`.`uid` AS `searcherID`,`User`.`uid` AS `searcheeID`,`CompareWakeTime`.`score` AS `score` from ((`SearchHistory` join `User`) join `CompareWakeTime`) where ((`SearchHistory`.`uid` <> `User`.`uid`) and (`SearchHistory`.`wakeTime` = `CompareWakeTime`.`searcherOption`) and (`User`.`wakeTime` = `CompareWakeTime`.`searchee`));

-- --------------------------------------------------------

--
-- Structure for view `ScoreYear`
--
DROP TABLE IF EXISTS `ScoreYear`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `ScoreYear` AS select `SearchHistory`.`uid` AS `searcherID`,`User`.`uid` AS `searcheeID`,`CompareYear`.`score` AS `score` from ((`SearchHistory` join `User`) join `CompareYear`) where ((`SearchHistory`.`uid` <> `User`.`uid`) and (`SearchHistory`.`year` = `CompareYear`.`searcherOption`) and (`User`.`year` = `CompareYear`.`searchee`));

-- --------------------------------------------------------

--
-- Structure for view `UserMajorIn`
--
DROP TABLE IF EXISTS `UserMajorIn`;

CREATE ALGORITHM=UNDEFINED DEFINER=`duozhao`@`%.unc.edu` SQL SECURITY DEFINER VIEW `UserMajorIn` AS select `User`.`uid` AS `uid`,`User`.`major1` AS `major` from `User` union select `User`.`uid` AS `uid`,`User`.`major2` AS `major` from `User` where (`User`.`major2` is not null);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Contact`
--
ALTER TABLE `Contact`
  ADD CONSTRAINT `Contact_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `Password` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `SearchHistory`
--
ALTER TABLE `SearchHistory`
  ADD CONSTRAINT `SearchHistory_ibfk_12` FOREIGN KEY (`uid`) REFERENCES `Password` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `SearchHistory_ibfk_10` FOREIGN KEY (`musicListening`) REFERENCES `MusicListening` (`op`) ON DELETE NO ACTION,
  ADD CONSTRAINT `SearchHistory_ibfk_11` FOREIGN KEY (`musicListening`) REFERENCES `MusicListening` (`op`) ON DELETE NO ACTION,
  ADD CONSTRAINT `SearchHistory_ibfk_2` FOREIGN KEY (`major1`) REFERENCES `Major` (`abbr`) ON DELETE NO ACTION,
  ADD CONSTRAINT `SearchHistory_ibfk_3` FOREIGN KEY (`major2`) REFERENCES `Major` (`abbr`) ON DELETE NO ACTION,
  ADD CONSTRAINT `SearchHistory_ibfk_4` FOREIGN KEY (`major3`) REFERENCES `Major` (`abbr`) ON DELETE NO ACTION,
  ADD CONSTRAINT `SearchHistory_ibfk_5` FOREIGN KEY (`year`) REFERENCES `Year` (`abbr`) ON DELETE NO ACTION,
  ADD CONSTRAINT `SearchHistory_ibfk_6` FOREIGN KEY (`visitors`) REFERENCES `Visitors` (`op`) ON DELETE NO ACTION,
  ADD CONSTRAINT `SearchHistory_ibfk_7` FOREIGN KEY (`sleepTime`) REFERENCES `SleepTime` (`op`) ON DELETE NO ACTION,
  ADD CONSTRAINT `SearchHistory_ibfk_8` FOREIGN KEY (`wakeTime`) REFERENCES `WakeTime` (`op`) ON DELETE NO ACTION,
  ADD CONSTRAINT `SearchHistory_ibfk_9` FOREIGN KEY (`cleanness`) REFERENCES `Cleanness` (`op`) ON DELETE NO ACTION;

--
-- Constraints for table `User`
--
ALTER TABLE `User`
  ADD CONSTRAINT `User_ibfk_1` FOREIGN KEY (`year`) REFERENCES `Year` (`abbr`) ON DELETE CASCADE,
  ADD CONSTRAINT `User_ibfk_2` FOREIGN KEY (`visitors`) REFERENCES `Visitors` (`op`) ON DELETE CASCADE,
  ADD CONSTRAINT `User_ibfk_3` FOREIGN KEY (`sleepTime`) REFERENCES `SleepTime` (`op`) ON DELETE CASCADE,
  ADD CONSTRAINT `User_ibfk_4` FOREIGN KEY (`cleanness`) REFERENCES `Cleanness` (`op`) ON DELETE NO ACTION,
  ADD CONSTRAINT `User_ibfk_5` FOREIGN KEY (`wakeTime`) REFERENCES `WakeTime` (`op`) ON DELETE NO ACTION,
  ADD CONSTRAINT `User_ibfk_6` FOREIGN KEY (`musicListening`) REFERENCES `MusicListening` (`op`) ON DELETE NO ACTION,
  ADD CONSTRAINT `User_ibfk_7` FOREIGN KEY (`major1`) REFERENCES `Major` (`abbr`) ON DELETE NO ACTION,
  ADD CONSTRAINT `User_ibfk_8` FOREIGN KEY (`major2`) REFERENCES `Major` (`abbr`) ON DELETE NO ACTION,
  ADD CONSTRAINT `User_ibfk_9` FOREIGN KEY (`uid`) REFERENCES `Password` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `UserDormInterest`
--
ALTER TABLE `UserDormInterest`
  ADD CONSTRAINT `UserDormInterest_ibfk_3` FOREIGN KEY (`uid`) REFERENCES `Password` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `UserDormInterest_ibfk_2` FOREIGN KEY (`dorm`) REFERENCES `Dorm` (`abbr`) ON DELETE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
