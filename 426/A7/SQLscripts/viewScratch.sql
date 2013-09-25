CREATE VIEW CompareYear AS
SELECT 
	Y1.abbr as searcherOption, 
	Y2.abbr as searchee,
	10 - 2 * ABS(Y1.val - Y2.val) as score
FROM Year as Y1, Year as Y2;

CREATE VIEW CompareWakeTime AS
SELECT
	WT1.op as searcherOption,
	WT2.op as searchee,
	10 - 4 * ABS(WT1.val - WT2.val) as score
FROM
	WakeTime as WT1, 
	WakeTime as WT2;

CREATE VIEW CompareSleepTime AS
SELECT
	ST1.op as searcherOption,
	ST2.op as searchee,
	10 - 4 * ABS(ST1.val - ST2.val) as score
FROM
	SleepTime as ST1, 
	SleepTime as ST2;

CREATE VIEW CompareVisitors AS
SELECT
	V1.op as searcherOption,
	V2.op as searchee,
	10 - 4 * ABS(V1.val - V2.val) as score
FROM 
	Visitors as V1,
	Visitors as V2;

CREATE VIEW CompareMusicListening AS
SELECT
	ML1.op as searcherOption,
	ML2.op as searchee,
	10 - 8 * ABS(ML1.val - ML2.val) as score
FROM
	MusicListening as ML1,
	MusicListening as ML2;

CREATE VIEW CompareCleanness AS
SELECT 
	CL1.op as searcherOption,
	CL2.op as searchee,
	10 - 4 * ABS(CL1.val - CL2.val) as score
FROM
	Cleanness as CL1,
	Cleanness as CL2;

CREATE VIEW ScoreGender AS
SELECT
	SearchHistory.uid AS searcherID,
	User.uid AS searcheeID,
	CompareGender.score AS score
FROM
	SearchHistory, User, CompareGender
WHERE
	SearchHistory.uid <> User.uid AND
	SearchHistory.gender = CompareGender.searcherOption AND
	User.gender = CompareGender.searchee;

CREATE VIEW ScoreMusicListening AS
SELECT
	SearchHistory.uid AS searcherID,
	User.uid AS searcheeID,
	CompareMusicListening.score AS score
FROM
	SearchHistory, User, CompareMusicListening
WHERE
	SearchHistory.uid <> User.uid AND
	SearchHistory.musicListening = CompareMusicListening.searcherOption AND
	User.musicListening = CompareMusicListening.searchee;

CREATE VIEW ScoreVisitors AS
SELECT
	SearchHistory.uid AS searcherID,
	User.uid AS searcheeID,
	CompareVisitors.score AS score
FROM
	SearchHistory, User, CompareVisitors
WHERE
	SearchHistory.uid <> User.uid AND
	SearchHistory.visitors = CompareVisitors.searcherOption AND
	User.visitors = CompareVisitors.searchee;

CREATE VIEW ScoreCleanness AS
SELECT
	SearchHistory.uid AS searcherID,
	User.uid AS searcheeID,
	CompareCleanness.score AS score
FROM
	SearchHistory, User, CompareCleanness
WHERE
	SearchHistory.uid <> User.uid AND
	SearchHistory.cleanness = CompareCleanness.searcherOption AND
	User.cleanness = CompareCleanness.searchee;

CREATE VIEW ScoreWakeTime AS
SELECT
	SearchHistory.uid AS searcherID,
	User.uid AS searcheeID,
	CompareWakeTime.score AS score
FROM
	SearchHistory, User, CompareWakeTime
WHERE
	SearchHistory.uid <> User.uid AND
	SearchHistory.wakeTime = CompareWakeTime.searcherOption AND
	User.wakeTime = CompareWakeTime.searchee;

CREATE VIEW ScoreSleepTime AS
SELECT
	SearchHistory.uid AS searcherID,
	User.uid AS searcheeID,
	CompareSleepTime.score AS score
FROM
	SearchHistory, User, CompareSleepTime
WHERE
	SearchHistory.uid <> User.uid AND
	SearchHistory.sleepTime = CompareSleepTime.searcherOption AND
	User.sleepTime = CompareSleepTime.searchee;

CREATE VIEW ScoreAge AS
SELECT
	SearchHistory.uid AS searcherID,
	User.uid AS searcheeID,
	IF(DATEDIFF(User.DOB, UTC_DATE() - INTERVAL SearchHistory.ageHigh YEAR) > 0 AND
		DATEDIFF(User.DOB, UTC_DATE() - INTERVAL SearchHistory.ageLow YEAR) < 0, 
		10, 2) AS score
FROM
	SearchHistory, User
WHERE
	SearchHistory.uid <> User.uid;

CREATE VIEW ScoreMajor AS
SELECT
	SearchHistory.uid AS searcherID,
	UserMajorIn.uid AS searcheeID,
	1 + 3 * COUNT(*) AS score
FROM
	SearchHistory, UserMajorIn
WHERE
	SearchHistory.uid <> UserMajorIn.uid AND (
	SearchHistory.major1 = UserMajorIn.major OR
	SearchHistory.major2 = UserMajorIn.major OR
	SearchHistory.major3 = UserMajorIn.major)
GROUP BY
	SearchHistory.uid, UserMajorIn.uid;


CREATE VIEW ScoreYear AS
SELECT
	SearchHistory.uid AS searcherID,
	User.uid AS searcheeID,
	CompareYear.score AS score
FROM
	SearchHistory, User, CompareYear
WHERE
	SearchHistory.uid <> User.uid AND
	SearchHistory.year = CompareYear.searcherOption AND
	User.year = CompareYear.searchee;


CREATE VIEW ScoreTotal AS
SELECT
	ScoreGender.searcherID,
	ScoreGender.searcheeID, 
	ScoreGender.score AS gender,
	ScoreAge.score AS age,
	ScoreYear.score AS 'year',
	IF(ISNULL(ScoreMajor.score), 0, ScoreMajor.score) AS major,
	ScoreMusicListening.score AS musicListening,
	ScoreVisitors.score AS visitors,
	ScoreCleanness.score AS cleanness,
	ScoreSleepTime.score AS sleepTime,
	ScoreWakeTime.score AS wakeTime,
	(ScoreGender.score + ScoreYear.score + ScoreYear.score + 
		IF(ISNULL(ScoreMajor.score), 0, ScoreMajor.score) + 
		ScoreMusicListening.score + ScoreVisitors.score +
		ScoreCleanness.score + ScoreSleepTime.score + 
		ScoreWakeTime.score) as total
FROM
	ScoreGender LEFT JOIN
	ScoreAge USING (searcherID, searcheeID) LEFT JOIN
	ScoreYear USING (searcherID, searcheeID) LEFT JOIN
	ScoreMajor USING (searcherID, searcheeID) LEFT JOIN
	ScoreMusicListening USING (searcherID, searcheeID) LEFT JOIN
	ScoreVisitors USING (searcherID, searcheeID) LEFT JOIN
	ScoreCleanness USING (searcherID, searcheeID) LEFT JOIN
	ScoreSleepTime USING (searcherID, searcheeID) LEFT JOIN
	ScoreWakeTime USING (searcherID, searcheeID)
ORDER BY searcherID, total DESC
;


