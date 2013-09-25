<form id="active-search-form" action="PHPScripts/userRequests.php"
	method="get">
	<fieldset>
		<legend>Select your search options here:</legend>
		<table>
			<tbody>
				<tr>
					<td>
						<div>
							Gender: <label>Male <input type="radio" name="gender" value="M">
							</label> <label>Female<input type="radio" name="gender" value="F"
								checked>
							</label>
						</div>
					</td>
					<td><div>
							Age: from <select id="age-no-younger-than" name="ageLow">
								<option selected="selected" value=17>17</option>
								<?php for ($i = 18; $i<=28; $i++): ?>
								<option value=<?php echo $i;?>>
									<?php echo $i;?>
								</option>
								<?php endfor; ?>
							</select> to <select id="age-no-older-than" name="ageHigh">
								<?php for ($i = 17; $i<=27; $i++): ?>
								<option value=<?php echo $i;?>>
									<?php echo $i;?>
								</option>
								<?php endfor; ?>
								<option selected="selected" value=28>28</option>
							</select>
						</div></td>
				
				
				<tr>
					<td colspan="2">
						<div>
							Year: <label>Freshman<input type="radio" name="year" value="FR">
							</label> <label>Sophomore<input type="radio" name="year"
								value="SO" checked>
							</label> <label>Junior<input type="radio" name="year" value="JR">
							</label> <label>Senior<input type="radio" name="year" value="SR">
							</label> <label>Graduate Students<input type="radio" name="year"
								value="GR">
							</label>
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<div>
							Major: <select id="major-option-1" name="major1">
								<?php include 'common/majorOptions.php';?>
							</select> <select id="major-option-2" name="major2">
								<?php include 'common/majorOptions.php';?>
							</select> <select id="major-option-3" name="major3">
								<?php include 'common/majorOptions.php';?>
							</select> (Up to 3 choices)
						</div>
					</td>
				</tr>
				<tr>
					<td>Music Listening <select id="music-listening"
						name="musicListening">
							<option value="headphone">With headsets on</option>
							<option value="speaker">Speaker</option>
					</select>
					</td>
					<td>Visitor <select id="visitor-preference" name="visitors">
							<option value="no">No visitors</option>
							<option value="sometimes">Sometimes</option>
							<option value="freq">Frequent Visitors</option>
					</select>
					</td>
				</tr>
				<tr>
					<td>Sleeping Preference <select id="sleeping-preference"
						name="sleepTime">
							<option value="before12">Before 12AM</option>
							<option value="12-2">12AM-2AM</option>
							<option value="after2">After 2AM</option>
					</select>
					</td>
					<td>Wake Preference <select id="wake-preference" name="wakeTime">
							<option value="before7">Before 7AM</option>
							<option value="7-9">7AM-9AM</option>
							<option value="after9">After 9AM</option>
					</select>
					</td>
				</tr>
				<tr>
					<td>Cleanness <select id="cleanness" name="cleanness">
							<option value="daily">Clean every day</option>
							<option value="weekly">Clean every week</option>
							<option value="never">Never to clean up</option>
					</select>
					</td>
				</tr>
			</tbody>
		</table>
		<div id="search-buttons">
			<input type="hidden" id="baseIndex" name='base' value=0> <input
				type="hidden" id="windowSize" name='size' value=5>
			<button id="search-option-submit" type="submit">Start</button>
			<button type="reset">Clear</button>
		</div>
	</fieldset>
</form>

<div class="search-result-div">
	<div></div>
</div>
<div id="bottom-button">
	<button type="button" id="next">Next</button>
	<button type="button" id="prev">Previous</button>
</div>
