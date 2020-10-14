//Programmer: Sisi Kang

//Date: Oct 11
//Description: project 3 generating a melody 

import processing.core.*;

import java.util.*; 

//importing the JMusic stuff
import jm.music.data.*;
import jm.JMC;
import jm.util.*;
import jm.midi.*;

import java.io.UnsupportedEncodingException;
import java.net.*;

//import javax.sound.midi.*;

			//make sure this class name matches your file name, if not fix.
public class HelloWorldMidiMain extends PApplet {

	MelodyPlayer player; //play a midi sequence
	MidiFileToNotes midiNotes; //read a midi file

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PApplet.main("HelloWorldMidiMain"); //change this to match above class & file name 

	}

	//setting the window size to 300x300
	public void settings() {
		size(300, 300);

	}

	//doing all the setup stuff
	public void setup() {
		fill(120, 50, 240);
		
		MarkovGenerator<Integer> pitchGenerator = new MarkovGenerator<Integer> (3);
		MarkovGenerator<Double> rhythmGenerator = new MarkovGenerator<Double> (3);

		// returns a url
		String filePath = getPath("mid/gardel_por.mid");
		//playMidiFile(filePath);

		midiNotes = new MidiFileToNotes(filePath); //creates a new MidiFileToNotes -- reminder -- ALL objects in Java must 
													//be created with "new". Note how every object is a pointer or reference. Every. single. one.


		// which line to read in --> this object only reads one line (or ie, voice or ie, one instrument)'s worth of data from the file
		midiNotes.setWhichLine(0);
		
		//training
		pitchGenerator.train(midiNotes.getPitchArray()); //train with getPitcher array
		rhythmGenerator.train(midiNotes.getRhythmArray()); //train with getRhthmArray

		player = new MelodyPlayer(this, 100.0f); //bpm

		player.setup();
		player.setMelody(pitchGenerator.generate(20) ); //assignments, generating probability on each pitch
		player.setRhythm(rhythmGenerator.generate(20) ); //assignments, generating probability on each rhythm
	}

	public void draw() {
	player.play(); //play each note in the sequence -- the player will determine whether is time for a note onset 

		textSize(12);
		
		fill(0,102, 153);
		text("Press 1 to start the unit test", 60, 120);
		text("Press 2 to rest", 60, 150);
		text("Press 3 to Run Unit Test 2", 60, 180);
		text("Press 4 to Run Unit Test 3", 60, 210);

	}

	//this finds the absolute path of a file
	String getPath(String path) {

		String filePath = "";
		try {
			filePath = URLDecoder.decode(getClass().getResource(path).getPath(), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}

	//this function is not currently called. you may call this from setup() if you want to test
	//this just plays the midi file -- all of it via your software synth. You will not use this function in upcoming projects
	//but it could be a good debug tool.
	void playMidiFile(String filename) {
		Score theScore = new Score("Temporary score");
		Read.midi(theScore, filename);
		Play.midi(theScore);
	}

	//this starts & restarts the melody.
	public void keyPressed() {
		MidiFileToNotes midiNotesMary; //read a midi file
		
		// returns a url
		String filePath = getPath("mid/MaryHadALittleLamb.mid");
		//playMidiFile(filePath);

		midiNotesMary = new MidiFileToNotes(filePath); //creates a new MidiFileToNotes -- reminder -- ALL objects in Java must
													//be created with "new". Note how every object is a pointer or reference. Every. single. one.


		// which line to read in --> this object only reads one line (or ie, voice or ie, one instrument)'s worth of data from the file
		midiNotesMary.setWhichLine(0);


		ProbabilityGenerator<Integer> firstNoteGen_pitch = new ProbabilityGenerator<>();
		ProbabilityGenerator<Double> firstNoteGen_rhythm = new ProbabilityGenerator<>();
		firstNoteGen_pitch.train(midiNotesMary.getPitchArray());
		firstNoteGen_rhythm.train(midiNotesMary.getRhythmArray());

		MarkovGenerator<Integer>[] melodyGen_pitch = new MarkovGenerator[10];
		MarkovGenerator<Double>[] melodyGen_rhythm  = new MarkovGenerator[10];
		for (int i=1; i<=10; i++) {
			melodyGen_pitch[i-1]  = new MarkovGenerator<>("Pitch",i);
			melodyGen_pitch[i-1].train(midiNotesMary.getPitchArray());
			melodyGen_pitch[i-1].e = firstNoteGen_pitch;
			melodyGen_rhythm[i-1]  = new MarkovGenerator<>("Rhythm",i);
			melodyGen_rhythm[i-1].train(midiNotesMary.getRhythmArray());
			melodyGen_rhythm[i-1].e = firstNoteGen_rhythm;
		}

		MarkovGenerator<Integer>[] ttGen_pitch   = new MarkovGenerator[10];
		MarkovGenerator<Double>[] ttGen_rhythm   = new MarkovGenerator[10];
		Arrays.setAll(ttGen_pitch, i-> new MarkovGenerator<>("Pitch",i+1));
		Arrays.setAll(ttGen_rhythm, i-> new MarkovGenerator<>("Rhythm",i+1));

		if (key == '1') {
			for (int i=0; i<10; i++) {
				melodyGen_pitch[i].norm();
			}
			for (int i=0; i<10; i++) {
				melodyGen_rhythm[i].norm();
			}
		} else if (key == '2') {
			for (int n=0; n<10000; n++) {
				ArrayList<Integer>[] initSeq_pitch = new ArrayList[10];
				ArrayList<Double>[] initSeq_rhythm = new ArrayList[10];
				Arrays.setAll(initSeq_pitch, i->new ArrayList<>());
				Arrays.setAll(initSeq_rhythm, i->new ArrayList<>());
				initSeq_pitch[0].add(firstNoteGen_pitch.generate());
				initSeq_rhythm[0].add(firstNoteGen_rhythm.generate());
				for (int i=1; i<10; i++) {
					initSeq_pitch[i].addAll(initSeq_pitch[i-1]);
					initSeq_pitch[i].add(melodyGen_pitch[i-1].generate(initSeq_pitch[i-1]));
					initSeq_rhythm[i].addAll(initSeq_rhythm[i-1]);
					initSeq_rhythm[i].add(melodyGen_rhythm[i-1].generate(initSeq_rhythm[i-1]));
				}
				for (int i=0; i<10; i++) {
					ttGen_pitch[i].train(melodyGen_pitch[i].generate(20, initSeq_pitch[i]));
					ttGen_rhythm[i].train(melodyGen_rhythm[i].generate(20, initSeq_rhythm[i]));
				}
			}
			for (int i=0; i<10; i++) {
				ttGen_pitch[i].norm();
			}
			for (int i=0; i<10; i++) {
				ttGen_rhythm[i].norm();
			}
		}
//		MarkovGenerator<Integer> melodyGen_pitch  = new MarkovGenerator<>();
//		MarkovGenerator<Integer> ttGen_pitch   = new MarkovGenerator<>();
//		ProbabilityGenerator<Integer> firstNoteGen_pitch = new ProbabilityGenerator<>();
//		MarkovGenerator<Double> melodyGen_rhythm  = new MarkovGenerator<>();
//		MarkovGenerator<Double> ttGen_rhythm   = new MarkovGenerator<>();
//		ProbabilityGenerator<Double> firstNoteGen_rhythm = new ProbabilityGenerator<>();
//
//		firstNoteGen_pitch.train(midiNotesMary.getPitchArray());
//		melodyGen_pitch.train(midiNotesMary.getPitchArray());
//		firstNoteGen_rhythm.train(midiNotesMary.getRhythmArray());
//		melodyGen_rhythm.train(midiNotesMary.getRhythmArray());
//		melodyGen_rhythm.alphabet_counts = new ArrayList<>(firstNoteGen_rhythm.alphabet_counts);
//
//		if (key == '1') {
//			melodyGen_pitch.norm();
//			melodyGen_rhythm.norm();
//		} else if (key == '4') {
//					for (int i=0; i<10000; i++) {
//			int initToken = firstNoteGen_pitch.generate();
//			ttGen_pitch.train(melodyGen_pitch.generate(20, initToken));
//		}
//		ttGen_pitch.norm();
//
//
//		//melodyGen_rhythm.norm();
//		for (int i=0; i<10000; i++) {
//			double initToken = firstNoteGen_rhythm.generate();
//			ttGen_rhythm.train(melodyGen_rhythm.generate(20, initToken));
//		}
//		ttGen_rhythm.norm();
//		}

//		ProbabilityGenerator<Integer> pitchGenerator = new ProbabilityGenerator<>();
//		ProbabilityGenerator<Double> rhythmGenerator = new ProbabilityGenerator<>();
//		pitchGenerator.train(midiNotesMary.getPitchArray());
//		rhythmGenerator.train(midiNotesMary.getRhythmArray());
//		if (key == '2') {
//			player.reset();
//			println("Melody started!");
//
//		}
//		else if (key == '1')
//		{
//			//run your unit 1
//			pitchGenerator.printProbabilityDistribution();
//			rhythmGenerator.printProbabilityDistribution();
//
//
//		} else if (key == '3') {
//
//			//run your unit 2
//			System.out.println(pitchGenerator.generate(20));
//			System.out.println(rhythmGenerator.generate(20));
//		} else if (key == '4') {
//			//run your unit 3
//			ProbabilityGenerator<Integer> pitchProbDistGen  = new ProbabilityGenerator<>();
//			ProbabilityGenerator<Double> rhythmProbDistGen = new ProbabilityGenerator<>();
//			for (int i=0; i<100000; i++) {
//				ArrayList<Integer> newPitch = pitchGenerator.generate(20);
//				pitchProbDistGen.train(newPitch);
//				ArrayList<Double> newRhythm = rhythmGenerator.generate(20);
//				rhythmProbDistGen.train(newRhythm);
//			}
//			pitchProbDistGen.printProbabilityDistribution();
//			rhythmProbDistGen.printProbabilityDistribution();
//		}
	}
}