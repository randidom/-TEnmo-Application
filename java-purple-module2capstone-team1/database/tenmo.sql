BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, transfer;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance numeric(13, 2) NOT NULL DEFAULT 1000,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);

CREATE SEQUENCE seq_transfer_id
INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;

CREATE TABLE transfer (
	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	transfer_amount numeric(13, 2) NOT NULL,
	from_user varchar(50) NOT NULL,
	to_user varchar(50) NOT NULL,
	transfer_status varchar(40) DEFAULT ('Approved'),
	CONSTRAINT PK_transfer PRIMARY KEY (transfer_id),
	CONSTRAINT FK_transfer_sender FOREIGN KEY (from_user) REFERENCES tenmo_user (username),
	CONSTRAINT FK_transfer_reciever FOREIGN KEY (to_user) REFERENCES tenmo_user (username)
);

INSERT INTO tenmo_user(username, password_hash) 
VALUES('mina', 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtaW5hIiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY5Mjk4ODIzM30.h2ENQ3nTEhUm5dlqOCYbP58D40cK6_EIW8VnsXiww9-c2PtwaGehxkEx9ykD_2ATDIPoCquFL4CEM_EQzRkOgg'),
('randi', 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYW5kaSIsImF1dGgiOiJST0xFX1VTRVIiLCJleHAiOjE2OTI5ODgyNjl9.KsE2RqIs9Kh_Puew3C-zJwQ3Hdlcw6isZRy6PxvsY8CkctGnazO8TvcGPKce965RsO15NKxW5UYdRcfXwZ5EKw');


COMMIT;
