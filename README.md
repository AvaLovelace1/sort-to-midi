# sort-to-midi

A program that converts sorting algorithms to midi melodies. Inspired by [Sound of Sorting](http://panthema.net/2013/sound-of-sorting/) (but is much more musical!).

An array is first filled with 5 octaves of a chosen scale and shuffled. It is then sorted using a chosen sorting algorithm, playing the corresponding note each time an element is moved. The results can be very beautiful or very weird (or both).

If you import the resulting midi file into a DAW (Logic Pro in this instance), you can clearly see the patterns created by the sort. It's quite an interesting visualization.

![Logic Pro Screenshot](https://github.com/AvaLovelace1/sort-to-midi/blob/master/sound-of-sorting.png?raw=true "sound-of-sorting")

7 sorting algorithms are currently supported:
1. Bubble Sort
2. Insertion Sort
3. Quick Sort
4. Merge Sort
5. Heap Sort
6. Radix Sort (LSD)
7. Bogo Sort

8 scales are currently supported:
1. Major
2. Harmonic Minor
3. Natural Minor
4. Melodic Minor
5. Chromatic
6. Pentatonic Major
7. Pentatonic Minor
8. Blues

## Usage

Run the program with `java -classpath ./bin sorttomidi.SortToMidi` and follow the prompts in the console. You can also import this project into Eclipse (`File > Open Projects from File System...`).
