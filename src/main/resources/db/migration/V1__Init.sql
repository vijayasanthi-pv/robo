CREATE TABLE firmware (
	id UUID PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	data VARCHAR(255),
	created_at TIMESTAMP,
	modified_at TIMESTAMP
);


CREATE TABLE robot (
	id UUID PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	fk_firmware_id UUID,
	foreign key (fk_firmware_id) references firmware(id)
);


