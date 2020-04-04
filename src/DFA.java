import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DFA {
	static String goal;
	static String[] states;
	static String[] acceptedStates;
	static String[] zeroTransitions;
	static String[] oneTransitions;
	static String[] eTransitions = {};
	static String[] temp;
	
	static ArrayList<String> repeatedStates = new ArrayList<String>();
	static ArrayList<String> remainingStates = new ArrayList<String>();
	static ArrayList<String> undoneStates = new ArrayList<String>();
	
	public static void divide(String description) {
		String[] values = description.split("#");
		
		states = values[0].split(";");
		
		goal = values[1];
		acceptedStates = goal.split(",");
		
	}
	
	public static void divideNFA(String description) {
		String[] values = description.split("#");
		
		String zeros = values[0];
		zeroTransitions = zeros.split(";");
		
		String ones = values[1];
		oneTransitions = ones.split(";");
		
		String e = values[2];
		temp = e.split(";");
		if(!temp[0].equals(""))
			eTransitions = temp;
		
		goal = values[3];
		acceptedStates = goal.split(",");
		
	}
	
	public static String zTransition() {
		int currentState = 0;
		int newState = 0;
		String currentOutput = "";
		boolean first = true;
		
		for(int i = 0; i < zeroTransitions.length; i++) {
			String[] currentTransition = zeroTransitions[i].split(",");
			
			while(!currentTransition[0].equals(Integer.toString(newState))) 
				newState++;
			
			if(currentTransition[0].equals(Integer.toString(newState))) {
				if( currentState != newState ) {
					if(first) {
						currentOutput = newState + ",";
						first = false;
					}
					else
						currentOutput = currentOutput + "#" + newState + ",";
					currentState = newState;
				}
				
				else if(currentState == 0 && first) {
					currentOutput += 0 + ",";
					first = false;
				}
				
				currentOutput = currentOutput + (currentTransition[1]);
			}
		}
		
		return currentOutput;
	}
	
	public static String oTransition() {
		int currentState = 0;
		int newState = 0;
		String currentOutput = "";
		boolean first = true;
		
		for(int i = 0; i < oneTransitions.length ; i++) {
			String[] currentTransition = oneTransitions[i].split(",");
			
			while(!currentTransition[0].equals(Integer.toString(newState))) 
				newState++;
			
			if(currentTransition[0].equals(Integer.toString(newState))) {
				if( currentState != newState ) {
					if(first) {
						currentOutput = newState + ",";
						first = false;
					}
					else
						currentOutput = currentOutput + "#" + newState + ",";
					currentState = newState;
				}
				
				else if(currentState == 0 && first) {
					currentOutput += 0 + ",";
					first = false;
				}
				
				currentOutput = currentOutput + (currentTransition[1]);
			}
		}
		
		return currentOutput;
	}
	
	public static String closure() {
		int currentState = 0;
		int newState = 0;
		String currentOutput = "";
		boolean first = true;
		
		for(int i = 0; i < eTransitions.length; i++) {
			String[] currentTransition = eTransitions[i].split(",");
			
			while(!currentTransition[0].equals(Integer.toString(newState))) 
				newState++;
			
			if(currentTransition[0].equals(Integer.toString(newState))) {
				if( currentState != newState ) {
					if(first) {
						currentOutput = newState + ",";
						first = false;
					}
					else
						currentOutput = currentOutput + "#" + newState + ",";
					currentState = newState;
				}
				
				else if(currentState == 0 && first) {
					currentOutput += 0 + "," ;
					first = false;
				}
				
				currentOutput = currentOutput + (currentTransition[1]);
			}
		}
		
		return currentOutput;
	}
	
	public static String formDFA(String zero, String one, String closure) {
		String output = "";
		String startState = "0";
		
		String[] closures = closure.split("#");
		
		// startState = initial state + closure if any
		for(int x = 0; x < closures.length; x++) {
			String[] sub = closures[x].split(",");
			
			if(sub[0].equals(startState)) {
				startState += sub[1];
			}
		}
		
		output += startState + ",";
		
		String remain = findZeros(startState, zero, closure);
		output += remain + ",";

		String remains = findOnes(startState, one, closure);
		output += remains;

		repeatedStates.add(startState);
		
		int currentIndex = 0;
		int size = undoneStates.size();
		
		while(size > 0 && currentIndex < remainingStates.size()) {
			if(check(remainingStates.get(currentIndex))) {
				String o1 = findZeros(remainingStates.get(currentIndex), zero, closure);
				String o2 = findOnes(remainingStates.get(currentIndex), one, closure);
				output += ";" + remainingStates.get(currentIndex) + "," + o1 + "," + o2;
				repeatedStates.add(remainingStates.get(currentIndex));
				size = removeDup(remainingStates.get(currentIndex));
			}
			
			currentIndex++;
			}
		output+= "#" + goal;
		return output;
	}
	
	public static Boolean check(String state) {
		if(state.length() == 1) {
			for(int i = 0; i < repeatedStates.size(); i++) {
				if(state.equals(repeatedStates.get(i)))
					return false;
			}
		}
			
		else if(state.length() > 1) {
			char[] allChar = state.toCharArray();
	        sort(allChar); 
			String x = new String(allChar);
			
			for(int ii = 0; ii < repeatedStates.size(); ii++) {
				char[] tempArray = repeatedStates.get(ii).toCharArray(); 
		
				sort(tempArray);
				
				if(new String(tempArray).equals(x)) {
				        return false;
				}
			}
		}
		
		return true;
	}
	
	 static void sort(char arr[]) 
	    { 
	        int n = arr.length; 
	  
	        // The output character array that will have sorted arr 
	        char output[] = new char[n]; 
	  
	        // Create a count array to store count of inidividul 
	        // characters and initialize count array as 0 
	        int count[] = new int[256]; 
	        for (int i=0; i<256; ++i) 
	            count[i] = 0; 
	  
	        // store count of each character 
	        for (int i=0; i<n; ++i) 
	            ++count[arr[i]]; 
	  
	        // Change count[i] so that count[i] now contains actual 
	        // position of this character in output array 
	        for (int i=1; i<=255; ++i) 
	            count[i] += count[i-1]; 
	  
	        // Build the output character array 
	        // To make it stable we are operating in reverse order. 
	        for (int i = n-1; i>=0; i--) 
	        { 
	            output[count[arr[i]]-1] = arr[i]; 
	            --count[arr[i]]; 
	        } 
	  
	        // Copy the output array to arr, so that arr now 
	        // contains sorted characters 
	        for (int i = 0; i<n; ++i) 
	            arr[i] = output[i]; 
	    } 
	
	public static int removeDup(String state) {
		
		int size = 0;
		while(size < undoneStates.size()) {
			if(undoneStates.get(size).equals(state)) {
				undoneStates.remove(size);
				size--;
			}
			size++;	
		}
		return size;
	}
	
	public static String findZeros(String state, String zero, String closure) {
		String output = "";
		
		if(state.equals("dead"))
			return "dead";
		
		String[] zeros = zero.split("#");
		
		for(int y = 0; y < zeros.length; y++) {
			String[] sub = zeros[y].split(",");
			
			if(state.length() == 1 && sub[0].equals(state)) {
				//String remaining = findClosure(state, closure);
				//output += remaining;

				String r = findClosure(sub[1], closure);

				if( r != "" && notFound(output, r)) 
					output += r ;
				
				if(notFound(output, sub[1])) {
					output += sub[1] ;
				}
			}
			
			else if(state.length() > 1) {
				char[] allStates = state.toCharArray();
				
				for(int i = 0; i < allStates.length; i++) {
					if(sub[0].charAt(0) == allStates[i]) {
						//String remaining = findClosure(String.valueOf(allStates[i]), closure);
						//output += remaining;

						String r = findClosure(sub[1], closure);

						if( r != "" && notFound(output, r)) 
							output += r ;
						
						if(notFound(output, sub[1])) {
							output += sub[1] ;
						}
					}
				}
			}
		}
		
		char[] allClosures = output.toCharArray();
		String temp = output;
		
		do{
			temp = output;
			for(int x = 0; x < allClosures.length;x++) {
			String c = findClosure(String.valueOf(allClosures[x]), closure);
			
			if( c != "" && notFound(output, c))
				output += c ;
			}
		}while(temp != output);
		
		if(check(output) && !output.equals("")) {
			remainingStates.add(output);
			undoneStates.add(output);
		}
		
		if(output.equals("")) {
			output = "dead";
			remainingStates.add(output);
			undoneStates.add(output);
		}
		
		return output;
	}
	
	public static String findOnes(String state, String one, String closure) {
		String output = "";
		
		if(state.equals("dead"))
			return "dead";
		
		String[] ones = one.split("#");
		
		for(int y = 0; y < ones.length; y++) {
			String[] sub = ones[y].split(",");
			
			if(state.length() == 1 && sub[0].equals(state)) {
				//String remaining = findClosure(state, closure);
				//output += remaining;

				String r = findClosure(sub[1], closure);

				if( r != "" && notFound(output, r)) 
					output += r ;
				
				if(notFound(output, sub[1]))
					output += sub[1] ;
			}
			
			else if(state.length() > 1) {
				char[] allStates = state.toCharArray();
				
				for(int i = 0; i < allStates.length; i++) {
					if(sub[0].charAt(0) == allStates[i]) {
						//String remaining = findClosure(String.valueOf(allStates[i]), closure);
						//output += remaining;

						String r = findClosure(sub[1], closure);

						if( r != "" && notFound(output, r)) 
							output += r ;
						
						if(notFound(output, sub[1]))
							output += sub[1] ;
					}
				}
			}
		}
		
		char[] allClosures = output.toCharArray();
		String temp = output;
		
		do{
			temp = output;
			for(int x = 0; x < allClosures.length;x++) {
			String c = findClosure(String.valueOf(allClosures[x]), closure);
			
			if( c != "" && notFound(output, c))
				output += c ;
			}
		}while(temp != output);
		
		if(check(output) && !output.equals("")) {
			remainingStates.add(output);
			undoneStates.add(output);
		}
		
		if(output.equals("")) {
			output = "dead";
			remainingStates.add(output);
			undoneStates.add(output);
		}
		return output;
	}
	
	public static String findClosure(String state, String closure) {
		String output = ""; 
		
		String[] closures = closure.split("#");
		
		
		if(closure.length() != 0) {
			for(int x = 0; x < closures.length; x++) {
				String[] sub = closures[x].split(",");
				
				if(state.length() == 1 && sub[0].equals(state)) {
					if(notFound(output, sub[1]))
						output += sub[1] ;
				}
				
				else if(state.length() > 1) {
					char[] allStates = state.toCharArray();
					
					for(int i = 0; i < allStates.length; i++) {
						if(sub[0].charAt(0) == allStates[i]) {
							String remaining = findClosure(String.valueOf(allStates[i]), closure);
							output +=  remaining;
							if(notFound(output, sub[1]))
								output += sub[1] ;
						}
					}
				}
			}
		}
		
		return output;
	}
	
	public static Boolean run(String string) {
		char[] characters = string.toCharArray();
		
		String[] startState = states[0].split(",");
		String current = startState[0];
		
		for(int i = 0; i < characters.length; i ++) {
			for(int ii =0; ii < states.length; ii++) {
				String[] currentState = states[ii].split(",");

				if(currentState[0].equals(current)) {
					if(characters[i] == '0' ) {
						current = currentState[1];
						break;
					}
					else {
						current = currentState[2];
						break;
					}
				}
			}
			
		}
		
		if(current.equals("dead"))
			return false;
		
		char[] finish = current.toCharArray();

		for(int j = 0; j < acceptedStates.length; j++) {
			for(int jj = 0; jj < finish.length; jj++) {
				if(finish[jj] == acceptedStates[j].charAt(0))
					return true;
			}
		}
		
		return false;
		
	}
	
	public static Boolean notFound(String state, String found) {
		char[] allStates = state.toCharArray();
		
		for(int x = 0; x < allStates.length; x++) {
			if(allStates[x] == found.charAt(0)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static void main (String[] args) {
		
		Scanner input = new Scanner(System.in);
		
		//divideNFA("0,0;1,2;3,3#0,0;0,1;2,3;3,3#1,2#3");
		//divideNFA("0,2;1,0;2,1#2,1;2,2#0,1#1");
		divideNFA("0,0;0,1;0,4;4,4#0,0;1,2;2,3;4,5#3,4;3,1#3,5");

		//divideNFA("0,0;1,2#0,01#0,1#2");
		//divideNFA("0,0;1,2;3,3#0,0;0,1;3,3#1,2;2,3#2,3");
		//divideNFA("0,0;3,1;4,0#0,1;3,2#0,2;1,3;3,4;4,1#2");
		//divideNFA("0,1;2,1#0,3;3,2#1,0;3,2#2");


		String output1 = zTransition();
		String output2 = oTransition();
		String output3 = closure();
		
		String o = formDFA(output1, output2, output3);

		System.out.println(o);
		
		divide(o);
		
		System.out.println("Enter your string");
		String strInput = input.next();
		
		Boolean result = run(strInput);
		
		System.out.println(result);
	}
	

}
