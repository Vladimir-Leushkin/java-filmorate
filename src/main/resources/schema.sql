create table if not exists MPA
(
    mpa_id   int generated by default as identity primary key,
    mpa_name varchar not null
);

create table if not exists GENRES
(
    genre_id   int generated by default as identity primary key,
    genre_name varchar not null
);

create table if not exists FILMS
(
    film_id      int generated by default as identity primary key,
    film_name    varchar not null,
    description  varchar(200),
    release_date date,
    duration     int,
    mpa_id       int,
    CONSTRAINT mpa_fk
        FOREIGN KEY (mpa_id)
            REFERENCES MPA (mpa_id)
);

create table if not exists USERS
(
    user_id   int generated by default as identity primary key,
    user_name varchar not null,
    email     varchar not null,
    login     varchar not null,
    birthday  date
);

create table if not exists FILM_GENRES
(
    film_id  int REFERENCES FILMS (film_id) ON DELETE CASCADE,
    genre_id int REFERENCES GENRES (genre_id) ON DELETE CASCADE,
        PRIMARY KEY (film_id, genre_id)

);

create table if not exists FILM_LIKES
(
    film_id int REFERENCES FILMS (film_id) ON DELETE CASCADE,
    user_id int REFERENCES USERS (user_id) ON DELETE CASCADE,
    CONSTRAINT film_likes_fk
        FOREIGN KEY (film_id)
            REFERENCES FILMS (film_id),
    CONSTRAINT user_likes_fk
        FOREIGN KEY (user_id)
            REFERENCES USERS (user_id),
            PRIMARY KEY (film_id, user_id)
);

create table if not exists USER_FRIENDS
(
    user_id int REFERENCES USERS (user_id) ON DELETE CASCADE,
    friend_id int REFERENCES USERS (user_id) ON DELETE CASCADE,
    CONSTRAINT user_friend_fk
        FOREIGN KEY (user_id)
            REFERENCES USERS (user_id),
            PRIMARY KEY (user_id, friend_id)
);

create unique index if not exists film_id_uindex
    on FILMS (film_id);

create unique index if not exists user_id_uindex
    on USERS (user_id);

CREATE TYPE IF NOT EXISTS event_type AS ENUM ('LIKE', 'FRIEND', 'REVIEW');
CREATE TYPE IF NOT EXISTS operation_type AS ENUM ('REMOVE', 'ADD', 'UPDATE');

CREATE TABLE IF NOT EXISTS event (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_time TIMESTAMP,
    user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    event_type event_type,
    operation operation_type,
    entity_id INTEGER
);

