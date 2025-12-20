-- customer テーブルへのデータ投入
INSERT INTO customer (id, name, email, age, registration_date, is_active) VALUES
(1, '山田太郎', 'taro.yamada@example.com', 35, '2022-01-15', true),
(2, '佐藤花子', 'hanako.sato@sample.jp', 24, '2023-05-20', true),
(3, '田中一郎', 'ichiro.tanaka@code.net', 48, '2021-08-01', false),
(4, '小林美咲', 'misaki.kobayashi@corp.com', 28, '2023-11-05', true),
(5, '田中健太', 'kenta.tanaka@co.jp', 48, '2024-02-10', true);


-- order_history テーブルへのデータ投入
INSERT INTO order_history (id, customer_id, order_amount, order_date, status) VALUES
(101, 1, 5500.00, '2024-06-01 10:30:00', 'DELIVERED'),
(102, 1, 12000.50, '2024-06-10 15:00:00', 'SHIPPED'),
(103, 2, 2800.00, '2024-06-15 09:00:00', 'DELIVERED'),
(104, 3, 800.00, '2024-06-20 11:00:00', 'PENDING'),
(105, 5, 15000.00, '2024-06-25 18:00:00', 'DELIVERED'),
(106, 1, 600.00, '2024-07-01 20:00:00', 'PENDING');

-- product_review データ
INSERT INTO product_review (customer_id, product_name, rating, content, review_date, is_approved) VALUES
(1, 'スマホX', 5, '最高傑作。', '2024-10-01', TRUE),
(2, 'PC-Lite', 3, '普通に使える。', '2024-10-05', TRUE),
(1, 'スマホX', 1, '動作が遅い。', '2024-10-10', FALSE),
(3, 'スマホX', 4, '満足です。', '2024-10-15', TRUE),
(2, 'イヤホンZ', 5, '音質が素晴らしい。', '2024-10-20', TRUE);
