-- Добавляем категории
INSERT INTO categories (id, name) VALUES (1, 'Выпечка');
INSERT INTO categories (id, name) VALUES (2, 'Напитки');
INSERT INTO categories (id, name) VALUES (3, 'Молочные продукты');
INSERT INTO categories (id, name) VALUES (4, 'Кондитерские изделия');

-- Добавляем товары (хлеб, напитки и т.д.)
INSERT INTO products (name, price, category_id) VALUES ('Хлеб Бородинский', 35.0, 1);
INSERT INTO products (name, price, category_id) VALUES ('Батон нарезной', 30.0, 1);
INSERT INTO products (name, price, category_id) VALUES ('Лепешка узбекская', 25.0, 1);
INSERT INTO products (name, price, category_id) VALUES ('Кола 0.5л', 55.0, 2);
INSERT INTO products (name, price, category_id) VALUES ('Вода без газа 0.5л', 20.0, 2);
INSERT INTO products (name, price, category_id) VALUES ('Молоко 2.5%', 75.0, 3);
INSERT INTO products (name, price, category_id) VALUES ('Кефир 1%', 60.0, 3);
INSERT INTO products (name, price, category_id) VALUES ('Шоколад "Казахстан"', 100.0, 4);