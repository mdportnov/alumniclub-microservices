CREATE DATABASE IF NOT EXISTS `AlumniClubDev`;
CREATE USER IF NOT EXISTS admin@'%' IDENTIFIED BY 'password';
GRANT ALL ON AlumniClubDev.* TO admin@'%';

CREATE DATABASE IF NOT EXISTS `AlumniClub_Recommendations`;
CREATE USER IF NOT EXISTS recommendationer@'%' IDENTIFIED BY 'password';
GRANT ALL ON AlumniClub_Recommendations.* TO recommendationer@'%';
