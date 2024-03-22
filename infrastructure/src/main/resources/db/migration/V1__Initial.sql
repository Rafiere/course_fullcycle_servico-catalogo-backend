CREATE TABLE category (
    id VARCHAR(36) NOT NULL PRIMARY KEY, # 36 é a quantidade de caracteres do UUID. O "PK" já informa pro MySQL que essa é uma chave primária e que o MySQL precisa criar um índice, portanto, não precisamos criá-lo de forma redundante.
    name VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL
)