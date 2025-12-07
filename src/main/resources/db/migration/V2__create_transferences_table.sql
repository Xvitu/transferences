CREATE TABLE IF NOT EXISTS transferences (
    id UUID PRIMARY KEY,
    amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    payer_id INTEGER NOT NULL,
    payee_id INTEGER NOT NULL
);

ALTER TABLE transferences
ADD CONSTRAINT fk_transference_payer
    FOREIGN KEY (payer_id) REFERENCES users(id);

ALTER TABLE transferences
ADD CONSTRAINT fk_transference_payee
    FOREIGN KEY (payee_id) REFERENCES users(id);
