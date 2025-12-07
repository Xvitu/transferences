INSERT INTO users (id, full_name, type, document, email, password)
VALUES
    (1, 'Alice Shopkeeper', 'SHOPKEEPER', '1234455', 'test@test.com', 'passleu'),
    (2, 'Bob Customer', 'CUSTOMER', '76666', 'test@fff.com', 'pass');

INSERT INTO wallets (user_id, available_amount)
VALUES
    (1, 1000000.00),  -- Alice
    (2, 500000.00);   -- Bob