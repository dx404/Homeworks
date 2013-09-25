<!DOCTYPE html>

<html>
  <head>
    <title>Find My Roommate</title>
	<meta charset="utf-8">
	<script src="JS/jquery-1.8.2.js"></script>
	<script src="JS/dorm.js"></script>
	<script src="JS/User.js"></script>
<!-- 	<script src="JS/setup.js"></script> -->
<!-- 	<script src="JS/MainPage.js"></script> -->
    <link rel="stylesheet" type="text/css" href="CSS/findmyroommate.css">
    <link rel="stylesheet" type="text/css" href="CSS/banner.css">
  </head>

  <body>
    <?php include 'common/banner.php' ?>
    <div id="search-option-div">
    <div>Select your search options here:</div>
    <form id="search-form" action="userRequests.php" method="get">
    	<table>
		   <tbody>
	    		<tr><td><div>Gender: <label>Male<input type="radio" name="select-gender" value="M"></label><label>Female<input type="radio" name="select-gender" value="F"></label></div></td>
	    			<td><div>Age: from
					    <select id="age-no-younger-than" name="agelower">
				          <option selected="selected">17</option>
				          <option>18</option>
				          <option>19</option>
				          <option>20</option>
				          <option>21</option>
				          <option>22</option>
				          <option>23</option>
				          <option>24</option>
				          <option>25</option>
				          <option>26</option>
				          <option>27</option>
				          <option>28</option>
				        </select>
				        to
				        <select id="age-no-older-than" name="ageupper">
				          <option>17</option>
				          <option>18</option>
				          <option>19</option>
				          <option>20</option>
				          <option>21</option>
				          <option>22</option>
				          <option>23</option>
				          <option>24</option>
				          <option>25</option>
				          <option>26</option>
				          <option>27</option>
				          <option selected="selected">28</option>
				        </select>
		    		</div></td>
	    <tr><td colspan="2">
		    <div>Year: 
		    	<label>Freshman<input type="radio" name="select-year" value="FR"></label>
		    	<label>Sophomore<input type="radio" name="select-year" value="SO"></label>
		    	<label>Junior<input type="radio" name="select-year" value="JR"></label>
		    	<label>Senior<input type="radio" name="select-year" value="SR"></label>
		    	<label>Graduate Students<input type="radio" name="select-year" value="GR"></label>
		    </div>
		   </td></tr>
	    <tr><td colspan="2">
		    <div>Major: 
			   <select id="major-option-1" name="major1">
					<option value="BCHM">Biochemistry</option>
					<option value="BIOL">Biology</option>
					<option value="BIOS">Biostatistics</option>
					<option value="BSBA">Business Administration</option>
					<option value="BUJO">Business Journalis</option>
					<option value="CHEM">Chemistry</option>
					<option value="COMM">Communication Studies</option>
					<option value="CS">Computer Science</option>
					<option value="ECON">Economics</option>
					<option value="EE">Electronic Engineering</option>
					<option value="NUTR">Health Nutrition</option>
					<option value="MDS">Mathematical Decision Science</option>
					<option value="MATH">Mathematics</option>
					<option value="OR">Operations Research</option>
					<option value="PHIL">Philosophy</option>
					<option value="PHYS">Physics</option>
					<option value="PSYC">Psychology</option>
					<option value="STAT">Statistics</option>
			   	</select>
			   	<select id="major-option-2" name="major2">
					<option value="BCHM">Biochemistry</option>
					<option value="BIOL">Biology</option>
					<option value="BIOS">Biostatistics</option>
					<option value="BSBA">Business Administration</option>
					<option value="BUJO">Business Journalis</option>
					<option value="CHEM">Chemistry</option>
					<option value="COMM">Communication Studies</option>
					<option value="CS">Computer Science</option>
					<option value="ECON">Economics</option>
					<option value="EE">Electronic Engineering</option>
					<option value="NUTR">Health Nutrition</option>
					<option value="MDS">Mathematical Decision Science</option>
					<option value="MATH">Mathematics</option>
					<option value="OR">Operations Research</option>
					<option value="PHIL">Philosophy</option>
					<option value="PHYS">Physics</option>
					<option value="PSYC">Psychology</option>
					<option value="STAT">Statistics</option>
			   	</select>
			   	<select id="major-option-3" name="major3">
					<option value="BCHM">Biochemistry</option>
					<option value="BIOL">Biology</option>
					<option value="BIOS">Biostatistics</option>
					<option value="BSBA">Business Administration</option>
					<option value="BUJO">Business Journalis</option>
					<option value="CHEM">Chemistry</option>
					<option value="COMM">Communication Studies</option>
					<option value="CS">Computer Science</option>
					<option value="ECON">Economics</option>
					<option value="EE">Electronic Engineering</option>
					<option value="NUTR">Health Nutrition</option>
					<option value="MDS">Mathematical Decision Science</option>
					<option value="MATH">Mathematics</option>
					<option value="OR">Operations Research</option>
					<option value="PHIL">Philosophy</option>
					<option value="PHYS">Physics</option>
					<option value="PSYC">Psychology</option>
					<option value="STAT">Statistics</option>
			   	</select>
			    (Up to 3 choices)
		    </div>
		 </td></tr>
		   	<tr>
		   		<td>Music Listening <select id= "music-listening" name="musicListening">
									<option value="headphone">With headsets on</option>
									<option value="speaker">Speaker</option>
							</select></td> 
		   		<td>Visitor <select id="visitor-preference" name="visitors">
									<option value="no">No visitors</option>
									<option value="sometimes">Sometimes</option>
									<option value="freq">Frequent Visitors</option>
							</select></td> 
		   	</tr>
		   	<tr>
		   		<td>Sleeping Preference <select id="sleeping-preference" name="sleepTime">
									<option value="before12">Before 12AM</option>
									<option value="12-2">12AM-2AM</option>
									<option value="after2">After 2AM</option>
							</select></td> 
		        <td>Wake Preference <select id="wake-preference" name="wakeTime">
									<option value="before7">Before 7AM</option>
									<option value="7-9">7AM-9AM</option>
									<option value="after9">After 9AM</option>
							</select></td> 
		   	</tr>
		   	<tr>
		   		<td>Cleanness <select id="cleanness" name="cleanness">
									<option value="daily">Clean every day</option>
									<option value="weekly">Clean every week</option>
									<option value="never">Never to clean up</option>
							</select></td> 
		   	</tr>
		   </tbody>
	   </table>
	   <div id="search-buttons">
	   		<button id="search-option-submit" type="submit">Start</button><button type="reset">Clear</button>
	   </div>
   </form>
    </div>
    <div id="search-result-div"> 
	</div>
  </body>
</html>   