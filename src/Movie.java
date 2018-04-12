// Name: Klinton Chhun
// Section: 1

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Movie {
	private int mid;
	private String title;
	private int year;
	private Set<String> tags;
	private Map<Integer, Rating> ratings;	//mapping userID -> rating
	private Double avgRating;
	//additional
	
	public Movie(int _mid, String _title, int _year){
		mid = _mid;
		title = _title;
		year = _year;
		tags = new HashSet<String>();
		ratings = new HashMap<Integer, Rating>();
		avgRating = 0.0;
	}
	
	public int getID() {
		return mid;
	}
	public String getTitle(){
		return title;
	}
	
	public Set<String> getTags() {
		return tags;
	}
	
	public void addTag(String tag){
		if(tag != null) tags.add(tag);
	}
	
	public int getYear(){
		return year;
	}
	
	public String toString()
	{
		return "[mid: "+mid+":"+title+" ("+year+") "+tags+"] -> avg rating: " + avgRating;
	}
	
	/**
	 * Objective: compute the average score of all the ratings of this movie
	 * Output: the average rating, or 0 if this movie has no rating
	 */
	public double calMeanRating(){
		if(ratings.isEmpty()) return 0;
		double sum = 0;
		for(Rating r: ratings.values()){
			sum += r.rating;
		}
		return sum / ratings.size();
	}
	
	public Double getMeanRating(){
		return avgRating;
	}
	
	/**
	 * Objective: add a rating of a user to this movie. If the user already rated
	 * this movie, only the latest rating (larger timestamp) is kept.
	 * Inputs: the rating user, the rated movie, the rating score and its timestamp
	 */
	public void addRating(User user, Movie movie, double rating, long timestamp) {
		if(user == null || movie == null) return;
		Rating old = ratings.get(user.getID());
		if(old == null || timestamp > old.timestamp){
			ratings.put(user.getID(), new Rating(user, movie, rating, timestamp));
			avgRating = calMeanRating();
		}
	}
	
	public Map<Integer, Rating> getRating(){
		return ratings;
	}
	
}
