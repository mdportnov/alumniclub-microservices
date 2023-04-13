ALTER TABLE Fingerprint
    ADD COLUMN name    VARCHAR(300),
    ADD COLUMN type    ENUM ('WEB', 'ANDROID', 'IOS', 'HUAWEI'),
    ADD COLUMN version VARCHAR(300);
