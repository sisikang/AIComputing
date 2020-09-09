
//Programmer: Sisi Kang
//Date: Sep 4
//Description: project 1

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
		
		ProbabilityGenerator<Integer> pitchGenerator = new ProbabilityGenerator<Integer> ();
		ProbabilityGenerator<Double> rhythmGenerator = new ProbabilityGenerator<Double> ();

		// returns a url
		String filePath = getPath("mid/gardel_por.mid");
		playMidiFile(filePath);

		midiNotes = new MidiFileToNotes(filePath); //creates a new MidiFileToNotes -- reminder -- ALL objects in Java must 
													//be created with "new". Note how every object is a pointer or reference. Every. single. one.


		// which line to read in --> this object only reads one line (or ie, voice or ie, one instrument)'s worth of data from the file
		midiNotes.setWhichLine(0);
		
		//training
		pitchGenerator.train(midiNotes.getPitchArray());
		rhythmGenerator.train(midiNotes.getRhythmArray());

		player = new MelodyPlayer(this, 100.0f); //bpm

		player.setup();
		player.setMelody(pitchGenerator.generate(20) ); //assignments
		player.setRhythm(rhythmGenerator.generate(20) ); //assignments
	}

	public void draw() {
	player.play(); //play each note in the sequence -- the player will determine whether is time for a note onset 

		textSize(12);
		
		fill(0,102, 153);
		text("Press 1 to start the unit test!", 60, 120);
		text("Press 2 to rest", 60, 150);

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
				// playMidiFile(filePath);

				midiNotesMary = new MidiFileToNotes(filePath); //creates a new MidiFileToNotes -- reminder -- ALL objects in Java must 
															//be created with "new". Note how every object is a pointer or reference. Every. single. one.


				// which line to read in --> this object only reads one line (or ie, voice or ie, one instrument)'s worth of data from the file
				midiNotesMary.setWhichLine(0);
		
		if (key == '2') {
			player.reset();
			println("Melody started!");
		
		}
		else if (key == '1')
		{
			//run your unit 1
			ProbabilityGenerator pg = new ProbabilityGenerator();
			pg.train(midiNotesMary.getPitchArray());
			printStr(pg);
			ProbabilityGenerator pg1 = new ProbabilityGenerator();
			pg1.train(midiNotesMary.getRhythmArray());
			printStr(pg1);

		}
	}
	
	public void printStr(ProbabilityGenerator pg) {
		System.out.println("-----------Probability Distribution----------------");
		for (int i=0; i<pg.alphabet.size(); i++) {
			String s = "Token:" + pg.alphabet.get(i) + "|Probability:" + pg.pro.get(i);
			System.out.println(s);
		}
	}
}