import java.util.ArrayList;


public class MarkovGenerator<T> extends ProbabilityGenerator<T> {

	ArrayList<ArrayList<Integer>> transitionTable = new ArrayList();

	MarkovGenerator(){
		
		super();
	}

	
	T generate() {
		
		T newToken = null;
		//do something here
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

	}
	
	void norm() {
		for (int i=0; i<transitionTable.size(); i++) {
			System.out.print(alphabet.get(i) + " ");
			int sum = 0;
			for (int v : transitionTable.get(i)) {
				sum += v;
			}
			for (int v : transitionTable.get(i)) {
				System.out.print((double) v / sum + " ");
				
			}
			System.out.println( "\n");
		}
	}

	ArrayList<T> generate (int length)
	{
		ArrayList<T> newSequence = new ArrayList<T>();
		
//		for (int i=0; i<length; i++)
//		{
//			newSequence.add(generate());
//			
//		}
		return newSequence;
		
	}
	
	ArrayList<T> generate (int length, T initToken)
	{
		ArrayList<T> newSequence = new ArrayList<T>();
//		for (int i=0; i<length; i++)
//		{
//			newSequence.add(generate());
//		}
		return newSequence;
	}
	
}
