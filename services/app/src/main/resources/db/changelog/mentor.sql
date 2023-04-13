-- Set null fields to empty strings
UPDATE AlumniClubDev.MentorData
SET company               = COALESCE(company, ''),
    expertArea            = COALESCE(expertArea, ''),
    formatsOfInteractions = COALESCE(formatsOfInteractions, ''),
    position              = COALESCE(position, ''),
    tags                  = COALESCE(tags, ''),
    whyAreYouMentor       = COALESCE(whyAreYouMentor, ''),
    graduation            = COALESCE(graduation, '');

alter table MentorData
    modify company varchar(2000) not null;

alter table MentorData
    modify expertArea varchar(2000) not null;

alter table MentorData
    modify formatsOfInteractions varchar(2000) not null;

alter table MentorData
    modify position varchar(2000) not null;

alter table MentorData
    modify tags varchar(2000) not null;

alter table MentorData
    modify whyAreYouMentor varchar(2000) not null;

alter table MentorData
    modify graduation varchar(2000) not null;

alter table MentorData
    modify interviewLink varchar(120);
