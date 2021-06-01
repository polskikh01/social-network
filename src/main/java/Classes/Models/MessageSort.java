package Classes.Models;

import java.util.Comparator;

public class MessageSort implements Comparator<Messages> {

	@Override
	public int compare(Messages o1, Messages o2) {
		return o1.getMessage().compareTo(o2.getMessage());

	}

}
