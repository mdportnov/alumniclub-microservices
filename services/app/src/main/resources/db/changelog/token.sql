alter table ResetPasswordToken
    modify token varchar(36) not null;

alter table Affiliate
    modify token varchar(36) not null;