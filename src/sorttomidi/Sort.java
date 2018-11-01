package sorttomidi;

import java.util.ArrayList;

public class Sort {

	private int len; // Length of notes
	private int arr[]; // The array to be sorted
	private ArrayList<Integer> output; // Pitches of notes will be put here
	private ArrayList<Integer> outputLength; // Lengths of notes
	// Indices of sorting algorithms
	public static final int BUBBLE_SORT = 1, INSERTION_SORT = 2, QUICK_SORT = 3, MERGE_SORT = 4, HEAP_SORT = 5,
			RADIX_SORT = 6, BOGO_SORT = 7;

	public Sort(int arr[], int len) {
		for (int i = 0; i < arr.length; i++) {
			int j = (int) (Math.random() * arr.length);
			int tmp = arr[i];
			arr[i] = arr[j];
			arr[j] = tmp;
		}
		this.arr = arr;
		this.len = len;
		this.output = new ArrayList<Integer>();
		this.outputLength = new ArrayList<Integer>();
	}

	public ArrayList<Integer> getOutput() {
		return output;
	}

	public ArrayList<Integer> getOutputLength() {
		return outputLength;
	}

	public void sort(int n) {
		switch (n) {
		case BUBBLE_SORT:
			bubbleSort();
			break;
		case INSERTION_SORT:
			insertionSort();
			break;
		case QUICK_SORT:
			quickSort();
			break;
		case MERGE_SORT:
			mergeSort();
			break;
		case HEAP_SORT:
			heapSort();
			break;
		case RADIX_SORT:
			radixSort();
			break;
		case BOGO_SORT:
			bogoSort();
			break;
		}
		if (isSorted()) {
			addNote(-1, len * 2); // add pause
			// add ending glissando
			for (int i = 0; i < arr.length; i++) {
				addNote(arr[i], len / 2);
			}
		}
	}

	// Adds a note to the output
	private void addNote(int pitch, int length) {
		output.add(pitch);
		outputLength.add(length);
	}

	// Swaps elements at indices x and y
	private void swap(int x, int y) {
		addNote(arr[x], len);
		addNote(arr[y], len);
		int tmp = arr[x];
		arr[x] = arr[y];
		arr[y] = tmp;
	}

	/*
	 * Bubble Sort
	 */

	public void bubbleSort() {
		int i, j;
		for (i = 0; i < arr.length - 1; i++) {
			for (j = 0; j < arr.length - i - 1; j++) {
				if (arr[j] > arr[j + 1]) {
					swap(j, j + 1);
				}
			}
		}
	}

	/*
	 * Insertion Sort
	 */

	public void insertionSort() {
		for (int i = 1; i < arr.length; i++) {
			int val = arr[i], j = i - 1;
			while (j >= 0 && arr[j] > val) {
				addNote(arr[j], len);
				arr[j + 1] = arr[j];
				j = j - 1;
			}
			addNote(val, len);
			arr[j + 1] = val;
		}
	}

	/*
	 * Quick Sort
	 */

	public void quickSort() {
		quickSort(0, arr.length - 1);
	}

	private void quickSort(int l, int r) {
		if (l < r) {
			int partition = quickSortPartition(l, r);
			quickSort(l, partition - 1);
			quickSort(partition + 1, r);
		}
	}

	private int quickSortPartition(int l, int r) {
		int pivot = arr[r];
		int i = l;
		for (int j = l; j <= r - 1; j++) {
			if (arr[j] <= pivot) {
				swap(i, j);
				i++;
			}
		}
		swap(i, r);
		return i;
	}

	/*
	 * Merge Sort
	 */

	public void mergeSort() {
		mergeSort(0, arr.length - 1);
	}

	private void merge(int l, int m, int r) {

		int n1 = m - l + 1, n2 = r - m;
		int L[] = new int[n1], R[] = new int[n2];

		for (int i = 0; i < n1; i++) {
			addNote(arr[l + i], len);
			L[i] = arr[l + i];
		}
		for (int i = 0; i < n2; i++) {
			addNote(arr[m + 1 + i], len);
			R[i] = arr[m + 1 + i];
		}

		int i = 0, j = 0;
		for (int k = l; k <= r; k++) {
			if (i < n1 && (j >= n2 || L[i] <= R[j])) {
				addNote(L[i], len);
				arr[k] = L[i];
				i++;
			} else {
				addNote(R[j], len);
				arr[k] = R[j];
				j++;
			}
		}
	}

	private void mergeSort(int l, int r) {
		if (l < r) {
			int m = (l + r) / 2;
			mergeSort(l, m);
			mergeSort(m + 1, r);
			merge(l, m, r);
		}
	}

	/*
	 * Heap Sort
	 */

	public void heapSort() {
		for (int i = arr.length / 2 - 1; i >= 0; i--) {
			heapify(arr.length, i);
		}
		for (int i = arr.length - 1; i >= 0; i--) {
			swap(0, i);
			heapify(i, 0);
		}
	}

	private void heapify(int n, int i) {
		int largest = i;
		int l = 2 * i + 1, r = 2 * i + 2;
		if (l < n && arr[l] > arr[largest]) {
			largest = l;
		}
		if (r < n && arr[r] > arr[largest]) {
			largest = r;
		}
		if (largest != i) {
			swap(i, largest);
			heapify(n, largest);
		}
	}

	/*
	 * Radix Sort (LSD)
	 */

	public void radixSort() {
		int maxN = arr[0];
		for (int i = 1; i < arr.length; i++) {
			maxN = Math.max(maxN, arr[i]);
		}
		int base = 10;
		for (int exp = 1; maxN / exp > 0; exp *= base) {
			countSort(exp, base);
		}
	}

	private void countSort(int exp, int base) {
		int tmp[] = new int[arr.length];
		int count[] = new int[base];

		for (int i = 0; i < arr.length; i++) {
			count[(arr[i] / exp) % base]++;
		}
		for (int i = 1; i < base; i++) {
			count[i] += count[i - 1];
		}
		for (int i = arr.length - 1; i >= 0; i--) {
			addNote(arr[i], len);
			tmp[count[(arr[i] / exp) % base] - 1] = arr[i];
			count[(arr[i] / exp) % base]--;
		}
		for (int i = 0; i < arr.length; i++) {
			addNote(tmp[i], len);
			arr[i] = tmp[i];
		}
	}

	/*
	 * Bogo Sort
	 */

	public void bogoSort() {
		for (int i = 1; i <= 10 && !isSorted(); i++) {
			shuffle(arr);
		}
	}

	private boolean isSorted() {
		for (int i = 0; i < arr.length - 1; i++) {
			if (arr[i] > arr[i + 1]) {
				return false;
			}
		}
		return true;
	}

	public void shuffle(int arr[]) {
		for (int i = 0; i < arr.length; i++) {
			int j = (int) (Math.random() * arr.length);
			swap(i, j);
		}
	}

}
