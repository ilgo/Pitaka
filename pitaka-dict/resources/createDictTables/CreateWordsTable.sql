CREATE TABLE words
(
	id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
	defid INT NOT NULL,
	word VARCHAR(512) NOT NULL
)
