package Classes.Models;

import java.util.Comparator;


public class NewsSorter {

	
	private class DateComparator implements Comparator<News>{
		@Override
		public int compare(News o1, News o2) {
			return o2.getDate().compareTo(o1.getDate());
		}

	}
	
	private class BestComparator implements Comparator<News>{
		@Override
		public int compare(News o1, News o2) {
			return o2.getRating().compareTo(o1.getRating());
		}

	}
	
	private class WrostComparator implements Comparator<News>{
		@Override
		public int compare(News o1, News o2) {
			return o1.getRating().compareTo(o2.getRating());
		}

	}

	public  Comparator<News> getComparator(String type){
		
		switch(type)
		{
			case "new":
				return new DateComparator();
			case "best":
				return new BestComparator();
			case "worst":
				return new WrostComparator();
			default:
				return new DateComparator();
				
		}
		
	}
}
