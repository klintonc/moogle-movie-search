// Name: Klinton Chhun
// Section: 1

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SimpleMovieSearchEngine implements BaseMovieSearchEngine {
	public Map<Integer, Movie> movies = new HashMap<Integer, Movie>();
	
	//<mid>,<title> (<year>),<tags> where the title may be wrapped in double quotes
	private static final Pattern MOVIE_PATTERN = Pattern.compile("^(\\d+),\"?(.*) \\((\\d{4})\\)\"?,(.*)$");
	
	@Override
	public Map<Integer, Movie> loadMovies(String movieFilename) {
		Map<Integer, Movie> result = new HashMap<Integer, Movie>();
		if(movieFilename == null) return result;
		
		try(BufferedReader br = new BufferedReader(new FileReader(movieFilename))){
			String line = br.readLine();	//skip the header
			while((line = br.readLine()) != null){
				Matcher m = MOVIE_PATTERN.matcher(line);
				if(!m.matches()) continue;
				
				int mid = Integer.parseInt(m.group(1));
				Movie movie = new Movie(mid, m.group(2), Integer.parseInt(m.group(3)));
				for(String tag: m.group(4).split("\\|")){
					movie.addTag(tag.trim());
				}
				result.put(mid, movie);
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void loadRating(String ratingFilename) {
		if(ratingFilename == null) return;
		
		try(BufferedReader br = new BufferedReader(new FileReader(ratingFilename))){
			String line = br.readLine();	//skip the header
			while((line = br.readLine()) != null){
				String[] parts = line.trim().split(",");
				if(parts.length != 4) continue;
				try {
					int uid = Integer.parseInt(parts[0]);
					int mid = Integer.parseInt(parts[1]);
					double score = Double.parseDouble(parts[2]);
					long timestamp = Long.parseLong(parts[3]);
					
					Movie movie = movies.get(mid);
					if(movie == null || Double.isNaN(score) || Double.isInfinite(score)) continue;
					movie.addRating(new User(uid), movie, score, timestamp);
				} catch(NumberFormatException e){
					//skip malformed line
				}
			}
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void loadData(String movieFilename, String ratingFilename) {
		movies = loadMovies(movieFilename);
		loadRating(ratingFilename);
	}

	@Override
	public Map<Integer, Movie> getAllMovies() {
		return movies;
	}

	@Override
	public List<Movie> searchByTitle(String title, boolean exactMatch) {
		return advanceSearchTitle(new ArrayList<Movie>(movies.values()), title, exactMatch);
	}

	@Override
	public List<Movie> searchByTag(String tag) {
		List<Movie> result = new ArrayList<Movie>();
		if(tag == null) return result;
		for(Movie m: movies.values()){
			if(hasTag(m, tag)) result.add(m);
		}
		return result;
	}

	@Override
	public List<Movie>searchByYear(int year) {
		List<Movie> result = new ArrayList<Movie>();
		for(Movie m: movies.values()){
			if(m.getYear() == year) result.add(m);
		}
		return result;
	}

	@Override
	public List<Movie> advanceSearch(String title, String tag, int year) {
		List<Movie> result = new ArrayList<Movie>();
		if(title == null && tag == null && year == -1) return result;
		
		for(Movie m: movies.values()){
			if(title != null && !m.getTitle().toLowerCase().contains(title.toLowerCase())) continue;
			if(tag != null && !hasTag(m, tag)) continue;
			if(year != -1 && m.getYear() != year) continue;
			result.add(m);
		}
		return result;
	}

	@Override
	public List<Movie> sortByTitle(List<Movie> unsortedMovies, boolean asc) {
		List<Movie> result = new ArrayList<Movie>();
		if(unsortedMovies == null) return result;
		result.addAll(unsortedMovies);
		
		Comparator<Movie> byTitle = new Comparator<Movie>() {
			@Override
			public int compare(Movie a, Movie b) {
				return a.getTitle().compareTo(b.getTitle());
			}
		};
		Collections.sort(result, asc ? byTitle : Collections.reverseOrder(byTitle));
		return result;
	}

	@Override
	public List<Movie> sortByRating(List<Movie> unsortedMovies, boolean asc) {
		List<Movie> result = new ArrayList<Movie>();
		if(unsortedMovies == null) return result;
		result.addAll(unsortedMovies);
		
		Comparator<Movie> byRating = new Comparator<Movie>() {
			@Override
			public int compare(Movie a, Movie b) {
				return Double.compare(a.getMeanRating(), b.getMeanRating());
			}
		};
		Collections.sort(result, asc ? byRating : Collections.reverseOrder(byRating));
		return result;
	}
	
	/**
	 * Objective: filter the given movies by title (case insensitive)
	 * Inputs: list of movies, the title keyword, and whether an exact match is required
	 * Output: list of movies whose title matches the keyword
	 */
	private List<Movie> advanceSearchTitle(List<Movie> list, String title, boolean exactMatch) {
		List<Movie> result = new ArrayList<Movie>();
		if(title == null) return result;
		String keyword = title.toLowerCase();
		for(Movie m: list){
			String t = m.getTitle().toLowerCase();
			if(exactMatch ? t.equals(keyword) : t.contains(keyword)) result.add(m);
		}
		return result;
	}
	
	/**
	 * Objective: check whether a movie has the given tag (case insensitive)
	 * Inputs: the movie and the tag
	 * Output: true if the movie has the tag, false otherwise
	 */
	private boolean hasTag(Movie m, String tag) {
		for(String t: m.getTags()){
			if(t.equalsIgnoreCase(tag)) return true;
		}
		return false;
	}

}
