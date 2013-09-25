SELECT 
	SearchHistory.uid AS searcherID,
	User.uid AS searcheeID,
	CompareGender.score AS genderScore,
	IF(DATEDIFF(User.DOB, UTC_DATE() - INTERVAL SearchHistory.ageHigh YEAR) > 0 AND
		DATEDIFF(User.DOB, UTC_DATE() - INTERVAL SearchHistory.ageLow YEAR) < 0, 
		100, 20) AS ageScore,
	CompareYear.score AS yearScore,
	0 AS majorScore,
	CompareMusicListening.score AS musicListeningScore,
	CompareVisitors.score AS visitorsScore,
	CompareCleanness.score AS cleannessScore,
	CompareSleepTime.score AS sleepTimeScore,
	CompareWakeTime.score AS wakeTimeScore
FROM SearchHistory, User,
	CompareGender,
	CompareYear,
	CompareMusicListening,
	CompareVisitors,
	CompareCleanness,
	CompareSleepTime,
	CompareWakeTime
WHERE
	SearchHistory.gender = CompareGender.searcherOption AND
	User.gender = CompareGender.searchee AND
	SearchHistory.year = CompareYear.searcherOption AND
	User.year = CompareYear.searchee AND	
	SearchHistory.musicListening = CompareMusicListening.searcherOption AND
	User.musicListening = CompareMusicListening.searchee AND
	SearchHistory.visitors = CompareVisitors.searcherOption AND
	User.visitors = CompareVisitors.searchee AND
	SearchHistory.cleanness = CompareCleanness.searcherOption AND
	User.cleanness = CompareCleanness.searchee AND
	SearchHistory.sleepTime = CompareSleepTime.searcherOption AND
	User.sleepTime = CompareSleepTime.searchee AND
	SearchHistory.wakeTime = CompareWakeTime.searcherOption AND
	User.wakeTime = CompareWakeTime.searchee;


SELECT
	SearchHistory.uid AS searcherID,
	UserMajorIn.uid AS searcheeID,
	COUNT(*) as majorScore 
FROM SearchHistory, UserMajorIn
WHERE
	SearchHistory.major1 = UserMajorIn.major OR
	SearchHistory.major2 = UserMajorIn.major OR
	SearchHistory.major3 = UserMajorIn.major;


CREATE VIEW CompareYear AS
select 
	Y1.name as searcherOption,
	Y2.name as searchee,
	20*(5 - ABS(Y2.value - Y1.value)) as score
from Year as Y1, Year as Y2;
