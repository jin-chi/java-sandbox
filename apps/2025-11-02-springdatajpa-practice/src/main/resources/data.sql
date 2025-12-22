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


-- Product (商品) データ
INSERT INTO product (id, product_code, name, price, category, stock_quantity) VALUES
(1, 'P001', '高性能ノートPC', 150000, '家電', 5),
(2, 'P002', 'ワイヤレスマウス', 3500, '家電', 50),
(3, 'P003', 'オーガニックリンゴ', 200, '食品', 100),
(4, 'P004', '高級オリーブオイル', 3000, '食品', 10),
(5, 'P005', 'コットンTシャツ', 2500, '衣類', 30),
(6, 'P006', '4Kテレビ', 80000, '家電', 0); -- 在庫切れケース

-- Coupon (クーポン) データ
INSERT INTO coupon (id, coupon_code, discount_rate, expiry_date, used_at, is_active) VALUES
(1, 'WELCOME10', 10, '2025-12-31', NULL, true), -- 有効・未使用
(2, 'WINTER20', 20, '2025-12-25', NULL, true), -- 有効・未使用
(3, 'OLD_SALE', 50, '2024-01-01', NULL, true), -- 期限切れ（過去日付）
(4, 'USED_500', 5, '2026-01-01', '2025-12-01 10:00:00', true), -- 使用済み（used_at IS NOT NULL）
(5, 'STOP_100', 15, '2026-12-31', NULL, false); -- 無効設定（is_active = false）
