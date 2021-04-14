CREATE TABLE firmware (
	id UUID PRIMARY KEY,
	name VARCHAR NOT NULL,
	data VARCHAR
);


CREATE TABLE robot (
	id UUID PRIMARY KEY,
	name VARCHAR NOT NULL,
	FK_FirmwareId UUID,
	foreign key (FK_FirmwareId) references firmware(id)
);


