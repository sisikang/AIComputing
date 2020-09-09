
//Programmer: Sisi Kang
//Date: Sep 4
//Description: Probability generator; project 1

import java.util.ArrayList;


public class ProbabilityGenerator<T> {

	ArrayList<T> alphabet; //T is type of token coming into probability generator class
	ArrayList<Integer> alphabet_counts; //need to use numbers 
	ArrayList<Double> pro;
	
	
	ProbabilityGenerator()
	{
		alphabet = new ArrayList<T>();
		alphabet_counts = new ArrayList<Integer>();
		pro = new ArrayList<Double>();
		
	}
	
	//it is training probability generator with new data
	void train(ArrayList<T> newTokens) // new Token as a parameter
	{
	
	
	//you will code the training, find the probability of each note in the array
	
	for (int i=0; i < newTokens.size(); i++){
	

		int index=alphabet.indexOf(newTokens.get(i));
        if(index<0){
            alphabet.add(newTokens.get(i));
            alphabet_counts.add(0);
            index=alphabet.size()-1;
        }
        alphabet_counts.set(index,alphabet_counts.get(index)+1);
	}
	int sum = 0;
	for (int v : alphabet_counts) {
		sum += v;  //sum of counts 
	}
	for ( int v : alphabet_counts) {
		pro.add((double) v / sum); //probability
	}
	
	
	}

	
	
	//generate one token
	T generate()
	{
		T newToken = null;
		//do something here
		return newToken;
	}
	
	//generate a lot of token
	ArrayList<T> generate( int length )
	{
		ArrayList<T> newSequence = new ArrayList<T>();
		for(int i=0; i<length; i++)
		{
			newSequence.add(generate());
			
		}
		return newSequence;
		
	}
	
}
