# Moogle Movie Search

ITCS208 Object Oriented Programming — Project 2, Faculty of ICT, Mahidol University (MUICT).

## What it is

Moogle is a command-line movie search engine built on a MovieLens-style dataset. It:

- Loads movies (`movieId,title,genres`) and user ratings (`userId,movieId,rating,timestamp`) from CSV files into `Movie`, `User` and `Rating` objects
- Searches movies by title (exact or approximate match), tag/genre, or release year
- Runs advanced searches that combine title, tag and year
- Sorts results by title or by average rating, ascending or descending

## Project structure

```
src/
  BaseMovieSearchEngine.java    # Search engine interface
  SimpleMovieSearchEngine.java  # Search engine implementation
  Movie.java, Rating.java, User.java
  MainController.java           # Command-line entry point
  MoogleTester.java             # Sample test run on data-micro
data-micro/                     # Tiny dataset for quick testing
data-sample/                    # Larger sample dataset
  results/loaded_data.txt       # Expected output of the load command
```

## How to run

Requires a JDK (Java 8 or newer).

Compile:

```sh
javac -d out src/*.java
```

Run the sample tester:

```sh
java -cp out MoogleTester
```

Run the main controller:

```sh
# Load and print all movies with their ratings
java -cp out MainController load data-sample/movies.csv data-sample/ratings.csv

# Search by title (exact), title (approximate), tag, or year
java -cp out MainController search data-sample/movies.csv data-sample/ratings.csv -e "stand up guys"
java -cp out MainController search data-sample/movies.csv data-sample/ratings.csv -a "stand"
java -cp out MainController search data-sample/movies.csv data-sample/ratings.csv -t Comedy
java -cp out MainController search data-sample/movies.csv data-sample/ratings.csv -y 1995

# Advanced search combining criteria
java -cp out MainController search+ data-sample/movies.csv data-sample/ratings.csv -a "er" -t Drama -y 2013
```

Run `java -cp out MainController` with no arguments to see the full usage.
