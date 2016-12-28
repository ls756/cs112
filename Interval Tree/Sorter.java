package structures;

import java.util.ArrayList;

/**
 * This class is a repository of sorting methods used by the interval tree.
 * It's a utility class - all methods are static, and the class cannot be instantiated
 * i.e. no objects can be created for this class.
 * 
 * @author runb-cs112
 */
public class Sorter {

	private Sorter() { }
	
	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		
		for(int i=1; i<intervals.size(); i++){

			Interval x = intervals.get(i);

			int pos=i-1;

			for (pos=i-1; pos >= 0; pos--) {

				Interval posIntrvl = intervals.get(pos);

				int c;

				if (lr == 'l') {

					//c = posIntrvl.compareToLeft(currIntrvl);

					c = posIntrvl.leftEndPoint - x.leftEndPoint;

				} else {

					//c = posIntrvl.compareToRight(currIntrvl);

					c = posIntrvl.rightEndPoint - x.rightEndPoint;

				}

				if (c < 0) break;

			}

			for (int j=i-1; j > pos; j--) {

				intervals.set(j+1,intervals.get(j));

			}

			intervals.set((pos+1), x);

		}

	}
	
	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		
		ArrayList<Integer> LP = new ArrayList<Integer>();
		ArrayList<Integer> RP = new ArrayList<Integer>();
		int count1=0;
		int count2=0;
		
		for(int i=0; i<leftSortedIntervals.size(); i++){
			if(i==0){
				LP.add(leftSortedIntervals.get(i).leftEndPoint);
				count1++;
			}
			else{
				if(leftSortedIntervals.get(i).leftEndPoint!=LP.get(count1-1)){
					LP.add(leftSortedIntervals.get(i).leftEndPoint);
					count1++;
				}
			}
		}
		for(int j=0; j<rightSortedIntervals.size(); j++){
			if(j==0){
				RP.add(rightSortedIntervals.get(j).rightEndPoint);
				count2++;
			}
			else{
				if(rightSortedIntervals.get(j).rightEndPoint!=RP.get(count2-1)){
					RP.add(rightSortedIntervals.get(j).rightEndPoint);
					count2++;
				}
				
			}
		}
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		int i=0;
		int j=0;
		while(i!=LP.size() && j!=RP.size()){
			if(LP.get(i)<RP.get(j)){
				result.add(LP.get(i));
				i++;
			}
			else if(LP.get(i)==RP.get(j)){
				result.add(LP.get(i));
				i++;
				j++;
			}
			else{
				result.add(RP.get(j));
				j++;
			}
		}
		if(i<LP.size()){
			for(int k=i; k<LP.size(); k++){
				result.add(LP.get(k));
			}
		}
		if(j<RP.size()){
			for(int k=j; k<RP.size(); k++){
				result.add(RP.get(k));
			}
		}
		return result;
		
	}	
}
