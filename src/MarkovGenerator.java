//Programmer: Sisi Kang
//Date: Oct 7
//Description: project 3 generating a melody 

import java.util.ArrayList;


public class MarkovGenerator<T> extends ProbabilityGenerator<T> {

	ArrayList<ArrayList<Integer>> transitionTable = new ArrayList<>();
	ArrayList<ArrayList<T>> uniqueAlphabetSequences = new ArrayList<>();
	ProbabilityGenerator<T> e;
	ArrayList<T> initSeq = null;
	int orderM;
	String type;

	MarkovGenerator(String t, int M){
		super();
		type = t;
		orderM = M;
}

	
	T generate(ArrayList<T> initSeq) {
		int curSeqIndex = uniqueAlphabetSequences.indexOf(initSeq);
		int[] pro = new int[transitionTable.get(0).size()+1];
		int sum = 0;
		if (curSeqIndex < 0) {
			return e.generate();
		} else {
			ArrayList<Integer> correct_row = transitionTable.get(curSeqIndex);
			for (int i=0; i<correct_row.size(); i++) {
				sum += correct_row.get(i);
				pro[i+1] = pro[i] + correct_row.get(i);
			}
			if ( sum == 0) {
				return e.generate();
			} else {
				int rand = (int) (sum * Math.random());
				T newToken = null;
				for (int i=0; i<correct_row.size(); i++) {
					if (rand < pro[i+1]) {
						newToken = alphabet.get(i);
						break;
					}
				}
				return newToken;
			}
		}

	}
	
	void train(ArrayList<T> newTokens)
	{
		boolean found=true;
		int RowIndex=0;
		int tokenIn=0;
		//Create the current sequence (eg. curSequence) of size orderM from the input
		
		//ArrayList<ArrayList<Integer>> getRow = new ArrayList<ArrayList<Integer>>();
		 ArrayList<Integer> getRow = new ArrayList<Integer>();
		 ArrayList<Integer> getColumn = new ArrayList<Integer>();
		 //	add the previous tokens to a container (eg ArrayList). 
		ArrayList<T> container = new ArrayList<T>();
		for(int i=0; i < newTokens.size(); i++) {
			container.add( newTokens.get(i));
		}
		// for i = orderM -1 to (i < size of the input - 1) do
	for(int i=orderM-1;i<newTokens.size()-1;i++) {
			
		 ArrayList<Integer> curSequence = new ArrayList<Integer>();
		//sequence will be the container
		 //You may do this in a for-loop or use .subList())
		    List<T> uniqueSequence =  container.subList(orderM,newTokens.size());
		//List<T> uniqueSequence =(List<T>) container.subList(orderM,newTokens.size());
		//Find  curSequence in uniqueAlphabetSequences
		for(int x=0;x<uniqueSequence.size();x++) {
			// case: not found 	1. set rowIndex to the size of uniqueAlphabetSequences
			if(!found) {
				RowIndex=uniqueSequence.size();
				//add the curSequence to uniqueAlphabetSequences
				uniqueSequence.add((T) container);
				//add a new row to the transition table the size of the alphabet
				ArrayList<Integer> nRow=new ArrayList<Integer>();
				for(int y=0;y<alphabet.size();y++) {
					nRow.add(0);
				}
				transitionTable.add(nRow);
			}
		}//for
		//Find the current next token (tokenIndex)
		for(int z=0;z<alphabet.size();z++) {
			//tokenIndex = the next index of the token in the alphabet (i+1)
			tokenIn=alphabet.indexOf(newTokens.get(z+1));
			//if tokenIndex is not found in the alphabet
				if(tokenIn==-1) {
					//1. tokenIndex = size of the alphabet 
					tokenIn=alphabet.size();
					//2. add the token to the alphabet
					alphabet.add(newTokens.get(z));
					//3. expand transitionTable horizontally
						for(int j=0;j<transitionTable.size();j++) {
							transitionTable.get(j).add(0);
						}
				}
		}//Update the counts – since we started after the beginning, rowIndex will not be -1
		
	
		 if(RowIndex!=-1&&transitionTable.size()>0) {
			//Get the row using rowIndex
			 getRow=transitionTable.get(RowIndex);
				//Get the column using tokenIndex
			// getColumn=transitionTable.get(tokenIn);
				//Add one to that value retrieved from the transition table
			 getRow.set(tokenIn,getRow.get(tokenIn)+1);
		 }
	}
	System.out.println(newTokens);
	}

	
		
		/*
		int lastIndex = -1;
		for (int i=0; i < newTokens.size(); i++)
		{
			int tokenIndex = alphabet.indexOf(newTokens.get(i));
			if (tokenIndex<0)
			{
				tokenIndex = alphabet.size();
				ArrayList<Integer> row = new ArrayList<>();
				int j=0;
				while (j++<tokenIndex) {
					row.add(0);
				}
				transitionTable.add(row);
				for (ArrayList<Integer> r : transitionTable) {
					r.add(0);
				}
				alphabet.add(newTokens.get(i));
				
			}

			//ok, now add the counts to the transition table
			if(lastIndex > -1) //that is, we have a previous token so its not the 1st time thru
			{
				ArrayList<Integer> correct_row = transitionTable.get(lastIndex);
				correct_row.set(tokenIndex, correct_row.get(tokenIndex) + 1);
			}
			lastIndex = tokenIndex; //setting current to previous for next round
		}
		*/

	void norm() {
		System.out.println(type + " for order "+ orderM +":");
		System.out.println("                 "+alphabet);
		for (int i=0; i<transitionTable.size(); i++) {
			System.out.print(uniqueAlphabetSequences.get(i) + " ");
			int sum = 0;
			for (int v : transitionTable.get(i)) {
				sum += v;
			}
			for (int v : transitionTable.get(i)) {
				if (sum == 0) {
					System.out.print("0.0 ");
				} else {
					System.out.print((double) v / sum + " ");
				}
				
			}
			System.out.println( "\n");
		}
	}

//	ArrayList<T> generate (int length)
//	{
//		ArrayList<T> newSequence = new ArrayList<T>();
//		int rand = (int) (alphabet.size() * Math.random());
//		this.initToken = alphabet.get(rand);
//		for (int i=0; i<length; i++)
//		{
//			T newToken = generate(this.initToken);
//			if (newToken == null) break;
//			newSequence.add(newToken);
//
//		}
//		return newSequence;
//
//	}
//
	ArrayList<T> generate (int length, ArrayList<T> initSeq)
	{
		ArrayList<T> newSequence = new ArrayList<T>();
		this.initSeq = initSeq;
		for (int i=0; i<length; i++)
		{
			T newToken = generate(this.initSeq);
			this.initSeq.remove(0);
			this.initSeq.add(newToken);
			newSequence.add(newToken);
		}
		return newSequence;
	}
	
}
