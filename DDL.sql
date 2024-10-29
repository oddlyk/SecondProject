-- Database 생성

-- 1. 한글세트를 저장할 수 있는 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS `project` DEFAULT CHARACTER SET utf8mb4;

-- 2. 데이터베이스 삭제
-- DROP DATABASE `project`;


-- 3. 데이터베이스 사용
USE `project`;


-- Table 생성 

-- User: 사용자 관리
	-- 테이블 삭제
-- drop TABLE project.users;
	-- 테이블 생성
CREATE TABLE project.users
(
	user_id varchar(30) PRIMARY KEY,
	user_pwd varchar(50) NOT NULL,
	user_name varchar(50) NOT NULL,
	email varchar(100) NOT NULL UNIQUE,
	phone varchar(20) NOT NULL,
	user_type int CHECK (user_type BETWEEN 1 AND 4) DEFAULT 1,
	verified char(1) CHECK (verified IN ('0','1')) DEFAULT '0'
);
	-- 비밀번호 암호화를 고려하여 비밀번호 컬럼 크기 증가
ALTER TABLE project.users MODIFY COLUMN user_pwd VARCHAR(255) NOT NULL;
COMMIT;

	-- 테이블 확인
SELECT * FROM project.users;


-- Port : 항구 정보
	-- 테이블 삭제
-- DROP TABLE project.port;
	-- 테이블 생성
CREATE TABLE project.port
(
	port_code varchar(5) PRIMARY KEY,
	port_name varchar(100) NOT NULL,
	port_lat double NOT NULL,
	port_lon double NOT NULL,
	port_contact varchar(20) NOT NULL,
	port_url varchar(255) NOT NULL,
	port_addr varchar(300) NOT NULL,
	avg_working_time double NOT NULL,
	avg_waiting_time double NOT NULL
);

	-- 테이블 확인
SELECT * FROM project.port;


-- Ship : 선박 관리
	-- 테이블 삭제
-- DROP TABLE project.ship;
	-- 테이블 생성
CREATE TABLE project.ship
(
	call_sign varchar(8) PRIMARY KEY,
	ship_name varchar(100) NOT NULL,
	ship_type int NOT NULL CHECK(ship_type BETWEEN 0 AND 8),
	tonnage int NOT NULL,
	mmsi char(9) UNIQUE,
	imo varchar(15) UNIQUE
);
	-- 테이블 확인
SELECT * FROM project.ship;

	-- 테이블 오류 수정 (imo데이터에 일괄적으로 .0이 붙은 상황 정제)
-- UPDATE project.ship
-- SET imo = REPLACE(imo, '.0', '');



-- voyage : 항해 관리
	-- 테이블 삭제
-- DROP TABLE project.voyage;
	-- 테이블 생성
CREATE TABLE project.voyage
(
	v_number int PRIMARY KEY AUTO_INCREMENT,
	call_sign varchar(8) NOT NULL,
	departure_date datetime NOT NULL,
	arrival_date datetime NOT NULL,
	departure varchar(5) NOT NULL,
	destination varchar(5) NOT NULL,
	on_boarding char(1) CHECK (on_boarding IN ('0','1')) DEFAULT '0',
	extra_tonnage int DEFAULT 0,
	entry_exit_fee int,
	berthing_fee int,
	anchorage_fee int,
	security_fee int,
	FOREIGN KEY (call_sign) REFERENCES project.ship(call_sign) ON DELETE CASCADE,
	FOREIGN KEY (destination) REFERENCES project.port(port_code) ON DELETE CASCADE
);

	-- 테이블 확인
SELECT * FROM project.voyage;
SELECT * FROM project.voyage WHERE on_boarding = '1';
SELECT * FROM project.voyage WHERE v_number = 63;

-- ais : ais 신호 관리
	-- 테이블 삭제
-- DROP TABLE project.ais;
	-- 테이블 생성
CREATE TABLE project.ais
(
	ais_number int PRIMARY KEY AUTO_INCREMENT,
	call_sign varchar(8) NOT NULL,
	v_number int,
	signal_date datetime NOT NULL,
	latitude double NOT NULL,
	longitude double NOT NULL,
	speed double NOT NULL,
	direction double CHECK (direction BETWEEN 0 AND 360),
	departure varchar(5) NOT NULL,
	destination varchar(5) NOT NULL,
	FOREIGN KEY (call_sign) REFERENCES project.voyage(call_sign) ON DELETE CASCADE,
	FOREIGN KEY (v_number) REFERENCES project.voyage(v_number) ON DELETE CASCADE,
	FOREIGN KEY (destination) REFERENCES project.voyage(destination) ON DELETE CASCADE
);
	-- 테이블 확인
SELECT * FROM project.ais;
SELECT * FROM project.ais WHERE v_number = 98;


-- AccidentStatus: 항구 근해의 사고 정보
	-- 테이블 삭제
-- DROP TABLE project.accident_status;
	-- 테이블 생성
CREATE TABLE project.accident_status
(
	accident_date varchar(20),
	port_code varchar(5),
	first_rank varchar(50) NOT NULL,
	first_per double NOT NULL,
	second_rank varchar(50),
	second_per double,
	third_rank varchar(50),
	third_per double,
	PRIMARY KEY (accident_date, port_code),
	FOREIGN KEY (port_code) REFERENCES project.port(port_code) ON DELETE CASCADE
);
	-- 테이블 확인
SELECT * FROM project.accident_status;


-- PortInfoA: 도착항 연관 좌표 (폴리곤)
	-- 테이블 삭제
-- DROP TABLE project.port_info_a;
	-- 테이블 생성
CREATE TABLE project.port_info_a
(
	pia_number int PRIMARY KEY AUTO_INCREMENT,
	port_code varchar(5) NOT NULL,
	loc_type int NOT NULL CHECK (loc_type BETWEEN 1 AND 3),
	info varchar(100) NOT NULL,
	location varchar(3000) NOT NULL,
	min_ton int,
	max_ton int,
	FOREIGN KEY (port_code) REFERENCES project.port(port_code) ON DELETE CASCADE
);
	-- 테이블 확인
SELECT * FROM project.port_info_a;


-- PortInfoB: 도착항 연관 좌표(마커)
-- DROP TABLE project.port_info_b;
	-- 테이블 생성
CREATE TABLE project.port_info_b
(
	pib_number int PRIMARY KEY AUTO_INCREMENT,
	port_code varchar(5) NOT NULL,
	loc_type int NOT NULL CHECK (loc_type BETWEEN 1 AND 2),
	location varchar(30) NOT NULL,
	add_date datetime NOT NULL DEFAULT current_timestamp,
	end_date datetime,
	FOREIGN KEY (port_code) REFERENCES project.port(port_code) ON DELETE CASCADE
);
	-- 테이블 확인
SELECT * FROM project.port_info_b;

-- BerthStatus: 항구 터미널의 선석현황
	-- 테이블 삭제
-- DROP TABLE project.berth_status;
	-- 테이블 생성
CREATE TABLE project.berth_status
(
	terminal_name varchar(5) NOT NULL,
	berth_number varchar(10) NOT NULL,
	port_code varchar(5) NOT NULL,
	is_using char(1) check(is_using IN ('0','1')) DEFAULT '0',
	ship_name varchar(100),
	arrival_date datetime,
	estimate_date datetime,
	discharge int,
	discharge_complete int,
	loading int,
	loading_complete int,
	PRIMARY KEY (terminal_name, berth_number),
	FOREIGN KEY (port_code) REFERENCES project.port(port_code) ON DELETE CASCADE
);
	-- 테이블 확인
SELECT * FROM project.berth_status;

-- favorite_voyage : 선박 저장
	-- 테이블 삭제
-- DROP TABLE project.favorite_voyage;
	-- 테이블 생성
CREATE TABLE project.favorite_voyage
(
	fs_number int PRIMARY KEY AUTO_INCREMENT,
	user_id varchar(30) NOT NULL,
	v_number int,
	top_favorite char(1) check(top_favorite IN ('0','1')) DEFAULT '0',
	FOREIGN KEY (user_id) REFERENCES project.users(user_id) ON DELETE CASCADE,
	FOREIGN KEY (v_number) REFERENCES project.voyage(v_number) ON DELETE CASCADE
);
	-- 테이블 확인
SELECT * FROM project.favorite_voyage;
COMMIT;



-- user_defined : 사용자가 수정한 선박의 정보 (계산용으로만 사용)
-- 	테이블 삭제
-- DROP TABLE project.user_defined;
-- 	테이블 생성
-- CREATE TABLE project.user_defined
-- (
-- 	ud_number int PRIMARY KEY AUTO_INCREMENT,
-- 	user_id varchar(30) NOT NULL,
-- 	v_number int,
-- 	tonnage int,
-- 	departure_date datetime,
-- 	arrival_date datetime,
-- 	waiting_start datetime,
-- 	waiting_end datetime,
-- 	FOREIGN KEY (user_id) REFERENCES project.users(user_id) ON DELETE CASCADE,
-- 	FOREIGN KEY (v_number) REFERENCES project.voyage(v_number) ON DELETE CASCADE
-- );
-- 	테이블 확인
-- SELECT * FROM project.user_defined;
-- 	더미 데이터 삽입
-- INSERT INTO project.user_defined (user_id, v_number, tonnage, departure_date, arrival_date, waiting_start, waiting_end)
-- VALUES
-- ('user001', 1, 50000, '2024-02-01 10:00:00', '2024-02-15 14:00:00', '2024-02-01 08:00:00', '2024-02-01 09:30:00'),
-- ('user002', 2, 75000, '2024-03-01 08:00:00', '2024-03-10 16:00:00', '2024-03-01 07:00:00', '2024-03-01 07:45:00');



COMMIT;