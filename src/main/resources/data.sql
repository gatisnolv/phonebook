INSERT INTO person (id, first_name, last_name, birth_date) VALUES
  (1, 'George', 'Harrison', '1943-02-25'),
  (2, 'Ringo', 'Starr', '1940-07-07');

INSERT INTO phone (id, number, type, person_id) VALUES
  (1, '+371 12345678', 'mobile', 1),
  (2, '+371 23456789', 'work', 1),
  (3, '+371 87654321', 'home', 2);