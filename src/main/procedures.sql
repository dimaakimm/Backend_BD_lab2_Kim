DELIMITER //

-- Создание базы данных
CREATE PROCEDURE create_database(IN db_name VARCHAR(255))
BEGIN
    SET @query = CONCAT('CREATE DATABASE IF NOT EXISTS ', db_name);
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END //

-- Удаление базы данных
CREATE PROCEDURE delete_database(IN db_name VARCHAR(255))
BEGIN
    SET @query = CONCAT('DROP DATABASE IF EXISTS ', db_name);
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END //

-- Создание таблицы
CREATE PROCEDURE create_table(IN table_name VARCHAR(255))
BEGIN
    SET @query = CONCAT('CREATE TABLE IF NOT EXISTS ', table_name, ' (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        description TEXT
    )');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END //

-- Удаление таблицы
CREATE PROCEDURE drop_table(IN table_name VARCHAR(255))
BEGIN
    SET @query = CONCAT('DROP TABLE IF EXISTS ', table_name);
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END //

-- Очистка таблицы
CREATE PROCEDURE clear_table(IN table_name VARCHAR(255))
BEGIN
    SET @query = CONCAT('DELETE FROM ', table_name);
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END //

-- Добавление записи
CREATE PROCEDURE add_record(IN table_name VARCHAR(255), IN name VARCHAR(255), IN description TEXT)
BEGIN
    SET @query = CONCAT('INSERT INTO ', table_name, ' (name, description) VALUES (''', name, ''', ''', description, ''')');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END //

-- Поиск по имени с возвратом всех данных
CREATE PROCEDURE search_by_name(IN table_name VARCHAR(255), IN search_name VARCHAR(255))
BEGIN
    SET @query = CONCAT('SELECT id, name, description FROM ', table_name, ' WHERE name = "', search_name, '"');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END //

-- Обновление записи
CREATE PROCEDURE update_record(IN table_name VARCHAR(255), IN old_name VARCHAR(255), IN new_name VARCHAR(255), IN description TEXT)
BEGIN
    SET @query = CONCAT('UPDATE ', table_name, ' SET name = ''', new_name, ''', description = ''', description, ''' WHERE name = ''', old_name, '''');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END //

-- Удаление записи по имени
CREATE PROCEDURE delete_by_name(IN table_name VARCHAR(255), IN del_name VARCHAR(255))
BEGIN
    SET @query = CONCAT('DELETE FROM ', table_name, ' WHERE name = ''', del_name, '''');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END //

-- Показать все базы данных
CREATE PROCEDURE list_databases()
BEGIN
    SHOW DATABASES;
END //

-- Показать все таблицы текущей базы данных
CREATE PROCEDURE list_tables()
BEGIN
    SHOW TABLES;
END //

DELIMITER ;