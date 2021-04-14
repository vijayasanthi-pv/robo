CREATE TABLE Firmwares (
	id UUID PRIMARY KEY,
	name VARCHAR NOT NULL,
	data VARCHAR
);


CREATE TABLE Robots (
	id UUID PRIMARY KEY,
	name VARCHAR NOT NULL,
	FK_FirmwareId UUID,
	foreign key (FK_FirmwareId) references Firmwares(id)
);


