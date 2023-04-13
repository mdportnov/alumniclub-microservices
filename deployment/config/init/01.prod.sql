CREATE DATABASE IF NOT EXISTS `AlumniClub`;
CREATE USER IF NOT EXISTS admin@'%' IDENTIFIED BY 'password';
GRANT ALL ON AlumniClub.* TO admin@'%';

CREATE DATABASE IF NOT EXISTS `AlumniClub_Recommendations`;
CREATE USER IF NOT EXISTS recommendationer@'%' IDENTIFIED BY 'password';
GRANT ALL ON AlumniClub_Recommendations.* TO recommendationer@'%';