DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id VARCHAR(255) PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       key VARCHAR(255) NOT NULL,
                       secret_key VARCHAR(255) NOT NULL
);

INSERT INTO users (id, username, password, key, secret_key)
values ('a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6', 'guest', '{bcrypt}$2a$10$OwfJjUs3L3JCizxu8CXxuOLu.meL4gMGiEtK/AIGOEpGsJlUZO7om', '01SKyNl2ZWnJmMdWs7ymZkgD', 'Xb1sbpS1R3ASyn1jRMfX0DzEpEUfZqXvzfWPeRPC4hUZIVvX')

