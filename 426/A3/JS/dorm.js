/** Dorm
 * 
 * Models the information associated with each dormitory.
 */


var Dorm = function (abbr, name, style, gender, community, location, roomStyles) {
	this.abbr = abbr;
	this.name = name;
	this.style = style;
	this.gender = gender;
	this.community = community;
	this.location = location;
	this.roomStyle = roomStyle;
}

Dorm.all = {};
Dorm.all['RAM'] = new Dorm('RAM', 'Ram Village', 'Apartment', 'Coed', 'Ram Village', 'South', ['2B', '']);
Dorm.all['ODM'] = new Dorm('ODM', 'Odum Village', 'Apartment', 'Coed', 'Odum Village', 'South');
Dorm.all['MFR'] = new Dorm('MFR', '1101 Mason Farm Road', 'Apartment', 'Coed', 'Odum Village', 'South');
Dorm.all['MRS'] = new Dorm('MRS', 'Morrison', 'Suite', 'Coed', 'Morrison Community', 'South');
Dorm.all['HJA'] = new Dorm('HJA', 'Hinton James', 'Apartment', 'Coed', 'Hinton James', 'South');

Dorm.all['PKR'] = new Dorm('PKR', 'Parker', 'Suite', 'Female', 'Parker', 'Mid');
Dorm.all['TGU'] = new Dorm('TGU', 'Teague', 'Suite', 'Coed', 'Parker', 'Mid');

Dorm.all['ODE'] = new Dorm('ODE', 'Old East', 'Corridor', 'Coed', 'Olde Campus Upper Quad', 'North');
Dorm.all['ODW'] = new Dorm('ODW', 'Old Wast', 'Corridor', 'Coed', 'Olde Campus Upper Quad', 'North');
Dorm.all['RFN'] = new Dorm('RFN', 'Ruffin', 'Corridor', 'Coed', 'Olde Campus Upper Quad', 'North');
Dorm.all['GHM'] = new Dorm('GHM', 'Graham', 'Corridor', 'Male', 'Olde Campus Lower Quad Community', 'North');
Dorm.all['ACK'] = new Dorm('ACK', 'Aycock', 'Corridor', 'Female', 'Olde Campus Lower Quad Community', 'North');

