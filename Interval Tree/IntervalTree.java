package structures;

import java.util.*;

/**
 * Encapsulates an interval tree.
 * 
 * @author runb-cs112
 */
public class IntervalTree {
	
	/**
	 * The root of the interval tree
	 */
	IntervalTreeNode root;
	
	/**
	 * Constructs entire interval tree from set of input intervals. Constructing the tree
	 * means building the interval tree structure and mapping the intervals to the nodes.
	 * 
	 * @param intervals Array list of intervals for which the tree is constructed
	 */
	public IntervalTree(ArrayList<Interval> intervals) {
		
		// make a copy of intervals to use for right sorting
		ArrayList<Interval> intervalsRight = new ArrayList<Interval>(intervals.size());
		for (Interval iv : intervals) {
			intervalsRight.add(iv);
		}
		
		// rename input intervals for left sorting
		ArrayList<Interval> intervalsLeft = intervals;
		
		// sort intervals on left and right end points
		Sorter.sortIntervals(intervalsLeft, 'l');
		Sorter.sortIntervals(intervalsRight,'r');
		
		// get sorted list of end points without duplicates
		ArrayList<Integer> sortedEndPoints = Sorter.getSortedEndPoints(intervalsLeft, intervalsRight);
		
		// build the tree nodes
		root = buildTreeNodes(sortedEndPoints);
		
		// map intervals to the tree nodes
		mapIntervalsToTree(intervalsLeft, intervalsRight);
	}
	
	/**
	 * Builds the interval tree structure given a sorted array list of end points.
	 * 
	 * @param endPoints Sorted array list of end points
	 * @return Root of the tree structure
	 */
	public static IntervalTreeNode buildTreeNodes(ArrayList<Integer> endPoints) {
		
		Queue<IntervalTreeNode> Q = new Queue<IntervalTreeNode>();
		for(int i=0; i<endPoints.size(); i++){
			IntervalTreeNode T = new IntervalTreeNode(endPoints.get(i),endPoints.get(i),endPoints.get(i));
			Q.enqueue(T);
		}
		int size = Q.size();
		IntervalTreeNode result = null;
		
		while(size>0){
			if(size==1){
				result = Q.dequeue();
				return result;
			}
			int temp = size;
			while(temp>1){
				IntervalTreeNode T1 = Q.dequeue();
				IntervalTreeNode T2 = Q.dequeue();
				float v1 = T1.maxSplitValue;
				float v2 = T2.minSplitValue;
				float x = (v1+v2)/2;
				IntervalTreeNode N = new IntervalTreeNode(x,T1.minSplitValue,T2.maxSplitValue);
				N.leftIntervals=new ArrayList<Interval>();
				N.rightIntervals=new ArrayList<Interval>();
				N.leftChild=T1;
				N.rightChild=T2;
				Q.enqueue(N);
				temp=temp-2;
			}
			if(temp==1){
				Q.enqueue(Q.dequeue());
			}
			size=Q.size();
		}
		result = Q.dequeue();
		return result;
		
	}
	
	/**
	 * Maps a set of intervals to the nodes of this interval tree. 
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 */
	public void mapIntervalsToTree(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		if(leftSortedIntervals==null){
			leftSortedIntervals = new ArrayList<Interval>();
		}
		if(rightSortedIntervals==null){
			rightSortedIntervals = new ArrayList<Interval>();
		}
		
		
		for(int i=0; i<leftSortedIntervals.size(); i++){
			IntervalTreeNode r = this.root;
			while(r!=null){
				if(leftSortedIntervals.get(i).contains(r.splitValue)){
					if(r.leftIntervals==null){
						r.leftIntervals = new ArrayList<Interval>();
					}
					r.leftIntervals.add(leftSortedIntervals.get(i));
					break;
				}
				if(r.splitValue>leftSortedIntervals.get(i).rightEndPoint){
					r=r.leftChild;
				}
				else{
					r=r.rightChild;
				}
			}
		}
		
		
		for(int i=0; i<rightSortedIntervals.size(); i++){
			IntervalTreeNode r = this.root;
			while(r!=null){
				if(rightSortedIntervals.get(i).contains(r.splitValue)){
					if(r.rightIntervals==null){
						r.rightIntervals = new ArrayList<Interval>();
					}
					r.rightIntervals.add(rightSortedIntervals.get(i));
					break;
				}
				if(r.splitValue>rightSortedIntervals.get(i).rightEndPoint){
					r=r.leftChild;
				}
				else{
					r=r.rightChild;
				}
			}
			
		}
		
		
		
		
	}	
	
	
	
	/**
	 * Gets all intervals in this interval tree that intersect with a given interval.
	 * 
	 * @param q The query interval for which intersections are to be found
	 * @return Array list of all intersecting intervals; size is 0 if there are no intersections
	 */
	public ArrayList<Interval> findIntersectingIntervals(Interval q) {
		
		return find(root,q);
	}
	
	public ArrayList<Interval> find(IntervalTreeNode R, Interval q){
		
		
		ArrayList<Interval> resultList = new ArrayList<Interval>();
		
		if(R==null){
			return resultList;
		}
		
		float SplitVal = R.splitValue;
		
		if(R.leftIntervals==null){
			R.leftIntervals=new ArrayList<Interval>();
		} 
		ArrayList<Interval> LList = R.leftIntervals;
		
		if(R.rightIntervals==null){
			R.rightIntervals=new ArrayList<Interval>();
		} 
		ArrayList<Interval> RList = R.rightIntervals;
		
		IntervalTreeNode Lsub = R.leftChild;
		IntervalTreeNode Rsub = R.rightChild;
		
		
		if(q.contains(SplitVal)){
			for(int i=0; i<LList.size(); i++){
				resultList.add(LList.get(i));
			}
			resultList.addAll(find(Rsub,q));
			resultList.addAll(find(Lsub,q));
		}
		else if(SplitVal < q.leftEndPoint){
			int i = RList.size()-1;
			while(i>=0 && RList.get(i).intersects(q)){
				resultList.add(RList.get(i));
				i--;
			}
			resultList.addAll(find(Rsub,q));
		}
		else if(SplitVal > q.rightEndPoint){
			int i=0;
			while(i<LList.size() && LList.get(i).intersects(q)){
				resultList.add(LList.get(i));
				i++;
			}
			resultList.addAll(find(Lsub,q));
		}
		return resultList;
		
		
	}
	
	
	
	/**
	 * Returns the root of this interval tree.
	 * 
	 * @return Root of interval tree.
	 */
	public IntervalTreeNode getRoot() {
		return root;
	}
}

