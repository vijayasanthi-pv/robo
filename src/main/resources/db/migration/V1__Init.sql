
CREATE TABLE Robots (
	id UUID PRIMARY KEY,
	name VARCHAR NOT NULL,
	FK_FirmwareId UUID
);


CREATE TABLE Firmwares (
	id UUID PRIMARY KEY,
	name VARCHAR NOT NULL,
	data VARCHAR,
	foreign key (TOURISTINFO_ID) references Robots(FK_FirmwareId)
);

