package sorttomidi;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.midi.*;

public class SortToMidi {

	// Dynamics
	public static final byte PPPP = 8, PPP = 20, PP = 31, P = 42, MP = 53, MF = 64, F = 80, FF = 96, FFF = 112,
			FFFF = 127;
	// Scales
	public static final byte SCALES[][] = { {}, { 0, 2, 4, 5, 7, 9, 11 }, { 0, 2, 3, 5, 7, 8, 11 },
			{ 0, 2, 3, 5, 7, 8, 10 }, { 0, 2, 3, 5, 7, 9, 11 }, { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 },
			{ 0, 2, 4, 7, 9 }, { 0, 3, 5, 7, 10 }, { 0, 3, 5, 6, 7, 10 } };
	public static final int MAJOR = 1, MINOR_HARMONIC = 2, MINOR_NATURAL = 3, MINOR_MELODIC = 4, CHROMATIC = 5,
			PENTATONIC_MAJOR = 6, PENTATONIC_MINOR = 7, BLUES = 8;

	public static final byte OCT = 12;
	private static byte scale[]; // The array to be sorted
	private static Sort sort;

	private static Scanner in = new Scanner(System.in);

	private static Sequence sequence;
	private static Track track;

	// Fill the scale to be sorted with a specified number of octaves at a
	// particular starting note.
	public static void fillScale(byte notes[], int octaves, int start) {
		scale = new byte[notes.length * octaves + 1];
		for (int i = 0; i < scale.length; i++) {
			scale[i] = (byte) (notes[i % notes.length] + (start + i / notes.length) * OCT);
		}
	}

	// Write the notes from an outputted list of integers to the midi track.
	public static void writeNotes(ArrayList<Integer> output, ArrayList<Integer> outputLength)
			throws InvalidMidiDataException {
		
		ShortMessage sm;
		MidiEvent me;
		
		int time = 0;
		for (int i = 0; i < output.size(); i++) {

			if (output.get(i) >= 0) {
				
				byte note = (byte) (int) output.get(i);

				// Play note
				sm = new ShortMessage();
				sm.setMessage(ShortMessage.NOTE_ON, note, MF);
				me = new MidiEvent(sm, time);
				track.add(me);
				time += outputLength.get(i);

				// Stop note
				sm = new ShortMessage();
				sm.setMessage(ShortMessage.NOTE_OFF, note, MF);
				me = new MidiEvent(sm, time);
				track.add(me);
				
			} else {
				// Negative pitch values denote a pause.
				time += outputLength.get(i);
			}
			
		}

	}

	// Creates the sort-to-midi file. Sequence is at 24 ticks per quarter note, so a
	// length of 6 is equal to a sixteenth note.
	public static void writeMidi(String trackName, int sortType, int scaleType, int len) {

		System.out.println("Writing midi file...");
		fillScale(SCALES[scaleType], 5, 3);

		try {
			// Create midi track
			sequence = new Sequence(Sequence.PPQ, 24);
			track = sequence.createTrack();

			// Fill an array with the scale notes to be passed to the sort
			int arr[] = new int[scale.length];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = scale[i];
			}
			sort = new Sort(arr, len);
			sort.sort(sortType);

			// Write the sort's output to the midi
			writeNotes(sort.getOutput(), sort.getOutputLength());

			// Write midi to file
			File f = new File(trackName + ".mid");
			MidiSystem.write(sequence, 1, f);

		} catch (InvalidMidiDataException | IOException e) {
			e.printStackTrace();
		}

		System.out.println("Complete");
	}

	public static void main(String args[]) {

		// User input
		System.out.println("Enter the name of the track:");
		String str = in.nextLine();
		System.out.println("Choose a sorting algorithm:");
		System.out.println(Sort.BUBBLE_SORT + " - Bubble Sort");
		System.out.println(Sort.INSERTION_SORT + " - Insertion Sort");
		System.out.println(Sort.QUICK_SORT + " - Quick Sort");
		System.out.println(Sort.MERGE_SORT + " - Merge Sort");
		System.out.println(Sort.HEAP_SORT + " - Heap Sort");
		System.out.println(Sort.RADIX_SORT + " - Radix Sort");
		System.out.println(Sort.BOGO_SORT + " - Bogo Sort");
		int sortType = in.nextInt();
		System.out.println("Choose a scale:");
		System.out.println(MAJOR + " - Major");
		System.out.println(MINOR_HARMONIC + " - Harmonic Minor");
		System.out.println(MINOR_NATURAL + " - Natural Minor");
		System.out.println(MINOR_MELODIC + " - Melodic Minor");
		System.out.println(CHROMATIC + " - Chromatic");
		System.out.println(PENTATONIC_MAJOR + " - Pentatonic Major");
		System.out.println(PENTATONIC_MINOR + " - Pentatonic Minor");
		System.out.println(BLUES + " - Blues");
		int scaleType = in.nextInt();
		writeMidi(str, sortType, scaleType, 6);

	}
}