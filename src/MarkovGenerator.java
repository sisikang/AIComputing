//Programmer:
//Date: Sep 21
//Description: project 2 generating a melody 

import java.util.ArrayList;


public class MarkovGenerator<T> extends ProbabilityGenerator<T> {

	ArrayList<ArrayList<Integer>> transitionTable = new ArrayList();
	T initToken = null;

	MarkovGenerator(){
		
		super();
	}

	
	T generate(T initToken) {
		//System.out.println("init:" +initToken);
		int tokenIndex = alphabet.indexOf(initToken);
		ArrayList<Integer> correct_row = transitionTable.get(tokenIndex);
		int[] pro = new int[correct_row.size()+1];
		int sum = 0;
		for (int i=0; i<correct_row.size(); i++) {
			sum += correct_row.get(i);
			pro[i+1] = pro[i] + correct_row.get(i);
		}
		if ( sum == 0) {
			for (int i=0; i<alphabet_counts.size(); i++) {
				sum += alphabet_counts.get(i);
				pro[i+1] = pro[i] + alphabet_counts.get(i);
			}
		}
		int rand = (int) (sum * Math.random());
		T newToken = null;
		for (int i=0; i<correct_row.size(); i++) {
			if (rand < pro[i+1]) {
				newToken = alphabet.get(i);
				break;
			}
		}
		this.initToken = newToken;
		return newToken;
	}
	
	void train(ArrayList<T> newTokens)
	{
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
		System.out.println(newTokens);
		

	}
	
	void norm() {
		for (int i=0; i<transitionTable.size(); i++) {
			System.out.print(alphabet.get(i) + " ");
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

	ArrayList<T> generate (int length)
	{
		ArrayList<T> newSequence = new ArrayList<T>();
		int rand = (int) (alphabet.size() * Math.random());
		this.initToken = alphabet.get(rand);
		for (int i=0; i<length; i++)
		{
			T newToken = generate(this.initToken);
			if (newToken == null) break;
			newSequence.add(newToken);

		}
		return newSequence;
		
	}
	
	ArrayList<T> generate (int length, T initToken)
	{
		ArrayList<T> newSequence = new ArrayList<T>();
		this.initToken = initToken;
		for (int i=0; i<length; i++)
		{
			T newToken = generate(this.initToken);
			if (newToken == null) break;
			newSequence.add(newToken);
		}
		return newSequence;
	}
	
}
