package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		CardNode ptr=deckRear;
		CardNode prev=null;
		while(ptr.cardValue!=27){
			prev=ptr;
			ptr=ptr.next;
		}
		CardNode temp=ptr;
		prev.next=ptr.next;
		ptr=ptr.next;			
		temp.next=ptr.next;
		ptr.next=temp;
		
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
	    CardNode ptr=deckRear;
	    CardNode prev=null;
	    while(ptr.cardValue!=28){
	    	prev=ptr;
	    	ptr=ptr.next;
	    }
	    CardNode temp=ptr;
	    prev.next=ptr.next;
	    ptr=ptr.next.next;
	    temp.next=ptr.next;
	    ptr.next=temp;
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		CardNode ptr=deckRear;
		CardNode prev=ptr;
		
		if(deckRear.next.cardValue==27 || deckRear.next.cardValue==28){
			if(deckRear.cardValue==27 || deckRear.cardValue==28){
				return;
			}
			else{
				ptr=ptr.next.next;
				while(ptr.cardValue!=27 && ptr.cardValue!=28){
					ptr=ptr.next;
				}
				deckRear=ptr;
			}
			
		}
		
		if(deckRear.cardValue==27 || deckRear.cardValue==28){
			ptr=ptr.next;
			while(ptr.cardValue!=27 && ptr.cardValue!=28){
				prev=ptr;
				ptr=ptr.next;
			}
			deckRear=prev;
			
		}
		
		
		while(ptr.cardValue!=27 && ptr.cardValue!=28){
			prev=ptr;
			ptr=ptr.next;
		}
		CardNode temp=ptr;
		ptr=ptr.next;
		while(ptr.cardValue!=27 && ptr.cardValue!=28){
			ptr=ptr.next;
		}
		prev.next=ptr.next;
		ptr.next=deckRear.next;
		deckRear.next=temp;
		
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {		
		CardNode tep=deckRear.next;
		CardNode ptr=deckRear;
		int count=0;
		while(count<deckRear.cardValue){
			ptr=ptr.next;
			count++;
		}
		CardNode temp=ptr;
		ptr=ptr.next;
		temp.next=deckRear;
		deckRear.next=ptr;
		CardNode prev=null;
		while(ptr!=deckRear){
			prev=ptr;
			ptr=ptr.next;
		}
		prev.next=tep;
		
	}
	
        /**
         * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
         * counts down based on the value of the first card and extracts the next card value 
         * as key, but if that value is 27 or 28, repeats the whole process until a value
         * less than or equal to 26 is found, which is then returned.
         * 
         * @return Key between 1 and 26
         */
	int getKey() {
		
		jokerA();
		jokerB();
		tripleCut();
		countCut();
		
		int target=deckRear.next.cardValue;
		if(target==28){
			target=27;
		}
		CardNode ptr=deckRear;
		int count=0;
		while(count<target){
			ptr=ptr.next;
			count++;
		}
		if(ptr.next.cardValue!=27 && ptr.next.cardValue!=28){
			return ptr.next.cardValue;
		}
		else{
			return getKey();
		}
		
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		String m="";
		String result="";
		for(int i=0; i<message.length(); i++){
			if(Character.isLetter(message.charAt(i))){
				if(Character.isUpperCase(message.charAt(i))){
					m+=message.charAt(i);
				}
			}
			
		}
		for(int i=0; i<m.length();i++){
			int index=m.charAt(i)-64+getKey();
			if(index>26){
				index=index-26;
			}
			index+=64;
			char ch=(char)index;
			result+=ch;
		}
	    return result;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		String m="";
		String result="";
		for(int i=0; i<message.length(); i++){
			if(Character.isLetter(message.charAt(i))){
				if(Character.isUpperCase(message.charAt(i))){
					m+=message.charAt(i);
				}
			}
			
		}
		for(int i=0; i<m.length();i++){
			int index=(int)m.charAt(i);
			index-=64;
			index-=getKey();
			if(index<=0){
				index=index+26;
			}
			index+=64;
			char ch=(char)index;
			result+=ch;
		}
	    return result;
	}
}
