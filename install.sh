sudo apt install mysql-server
sudo service mysql stop
sudo service mysql start &

mysql -h 127.0.0.1 -P 3306 -u root -p < initDb.sql
