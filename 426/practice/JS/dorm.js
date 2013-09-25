/** Dorm
 * 
 * Models the information associated with each dormitory.
 */


var Dorm = function (abbr, name, style, gender, community, location) {
	this.abbr = abbr; 
	this.name = name;
	this.style = style;
	this.gender = gender;
	this.community = community;
	this.location = location;
}

Dorm.all = {};
Dorm.all['RAM'] = new Dorm('RAM', 'Ram Village', 'Apartment', 'Coed', 'Ram Village', 'S');
Dorm.all['ODM'] = new Dorm('ODM', 'Odum Village', 'Apartment', 'Coed', 'Odum Village', 'S');
Dorm.all['MFR'] = new Dorm('MFR', '1101 Mason Farm Road', 'Apartment', 'Coed', 'Odum Village', 'S');
Dorm.all['MRS'] = new Dorm('MRS', 'Morrison', 'Suite', 'Coed', 'Morrison Community', 'S');
Dorm.all['HJA'] = new Dorm('HJA', 'Hinton James', 'Apartment', 'Coed', 'Hinton James', 'S');
Dorm.all['CRN'] = new Dorm('CRN', 'Craige North', 'Suite', 'Coed', 'Manning West', 'S')

Dorm.all['PKR'] = new Dorm('PKR', 'Parker', 'Suite', 'Female', 'Parker', 'Mid');
Dorm.all['TGU'] = new Dorm('TGU', 'Teague', 'Suite', 'Coed', 'Parker', 'Mid');

Dorm.all['ODE'] = new Dorm('ODE', 'Old East', 'Corridor', 'Coed', 'Olde Campus Upper Quad', 'N');
Dorm.all['ODW'] = new Dorm('ODW', 'Old Wast', 'Corridor', 'Coed', 'Olde Campus Upper Quad', 'N');
Dorm.all['RFN'] = new Dorm('RFN', 'Ruffin', 'Corridor', 'Coed', 'Olde Campus Upper Quad', 'N');
Dorm.all['GHM'] = new Dorm('GHM', 'Graham', 'Corridor', 'Male', 'Olde Campus Lower Quad', 'N');
Dorm.all['ACK'] = new Dorm('ACK', 'Aycock', 'Corridor', 'Female', 'Olde Campus Lower Quad', 'N');

Dorm.fullLocation = {'S':'Southern Campus', 'M':'Mid Campus', 'N':'Northern Campus'};


