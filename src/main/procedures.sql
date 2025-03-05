-- Создание таблицы с именем
DELIMITER //
CREATE PROCEDURE create_table(IN table_name VARCHAR(255))
BEGIN
    SET @query = CONCAT('CREATE TABLE IF NOT EXISTS ', table_name, ' (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255),
        age INT
    )');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END //
DELIMITER ;

-- Удаление таблицы по имени
DELIMITER //
CREATE PROCEDURE drop_table(IN table_name VARCHAR(255))
BEGIN
    SET @query = CONCAT('DROP TABLE IF EXISTS ', table_name);
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END //
DELIMITER ;

-- Создание базы данных
DELIMITER //
CREATE PROCEDURE create_database()
BEGIN
    CREATE DATABASE IF NOT EXISTS my_database;
END //
DELIMITER ;

-- Удаление базы данных
DELIMITER //
CREATE PROCEDURE delete_database()
BEGIN
    DROP DATABASE IF EXISTS my_database;
END //
DELIMITER ;

-- Очистка таблицы
DELIMITER //
CREATE PROCEDURE clear_table()
BEGIN
DELETE FROM my_table;
END //
DELIMITER ;

-- Добавление записи
DELIMITER //
CREATE PROCEDURE add_record(IN name VARCHAR(255), IN age INT)
BEGIN
INSERT INTO my_table (name, age) VALUES (name, age);
END //
DELIMITER ;

-- Поиск по имени
DELIMITER //
CREATE PROCEDURE search_by_name(IN search_name VARCHAR(255))
BEGIN
SELECT * FROM my_table WHERE name = search_name;
END //
DELIMITER ;

-- Обновление данных
DELIMITER //
CREATE PROCEDURE update_record(IN old_name VARCHAR(255), IN new_name VARCHAR(255), IN new_age INT)
BEGIN
UPDATE my_table SET name = new_name, age = new_age WHERE name = old_name;
END //
DELIMITER ;

-- Удаление по имени
DELIMITER //
CREATE PROCEDURE delete_by_name(IN del_name VARCHAR(255))
BEGIN
DELETE FROM my_table WHERE name = del_name;
END //
DELIMITER ;
