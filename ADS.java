import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ADS {
	public String fileName1;
	public String fileName2;

	public static void main(String[] args) {
		// checks whether we have passed the file names as arguments or not
		if (args.length != 3) {
			System.out.println("Please enter the paths for 2 input files and 1 output file");
		} else {
			// store the arguments(path names) in string values
			String filePath1 = args[0];
			String filePath2 = args[1];
			String s = "";
			String t = "";
			try {
				// Using BufferedReader to read the files and input them to a
				// string
				BufferedReader inputStream1 = new BufferedReader(
						new FileReader(filePath1));
				BufferedReader inputStream2 = new BufferedReader(
						new FileReader(filePath2));
				s = inputStream1.readLine();
				t = inputStream2.readLine();
			} catch (Exception ex) {
				System.out.println(ex.toString());

			}

			// Calling the methods for Objective1(ned()), Objective2(lcs()) and
			// Objective3(recLCS())
			float line1 = ned(s, t);
			String line2 = lcs(s, t);
			String line3 = recLcs(s, t);
			try {
				// using BufferedWriter to write the outputs to a text file
				BufferedWriter bw = new BufferedWriter(new FileWriter(args[2]));
				bw.write(Float.toString(line1));
				bw.newLine();
				bw.write(line2);
				bw.newLine();
				bw.write(line3);
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static float ned(String s, String t) {
		// Objective 1
		// storing lengths of the input strings
		int m = s.length();
		int n = t.length();

		// storing the default values of the first row in table
		int[] firstRow = new int[m + 1];
		for (int i = 0; i <= m; i++) {
			firstRow[i] = i;
		}

		// storing the default first value in second row of the table
		int[] secondRow = new int[m + 1];
		secondRow[0] = 1;

		// until the end of the second string, we compare the characters and
		// compute the second row based on the values from first row and then
		// move the values of second row to the first row
		while (secondRow[0] != n + 1) {
			for (int i = 1; i <= m; i++) {
				if (s.charAt(i - 1) == t.charAt(secondRow[0] - 1)) {
					secondRow[i] = firstRow[i - 1];
				} else {
					secondRow[i] = Math.min(firstRow[i], secondRow[i - 1]) + 1;
				}
			}
			for (int i = 0; i <= m; i++) {
				firstRow[i] = secondRow[i];
			}
			secondRow[0]++;
		}

		// computing the normalized edit distance
		float x = m + n;
		float y = firstRow[m];
		float ned = (x - y) / x;

		// printing the normalized edit distance and returning the value
		System.out.println(ned);
		return ned;
	}

	public static String lcs(String s, String t) {
		// Objective 2
		// storing lengths of the input strings
		int m = s.length();
		int n = t.length();

		// Defining the table and assigning the default values to first row and
		// first column
		int[][] d = new int[m + 1][n + 1];
		for (int i = 0; i <= m; i++) {
			d[i][0] = i;
		}
		for (int j = 0; j <= n; j++) {
			d[0][j] = j;
		}

		// computing the values of the remaining rows based on the values of the
		// previous row
		for (int j = 1; j <= n; j++) {
			for (int i = 1; i <= m; i++) {
				if (s.charAt(i - 1) == t.charAt(j - 1)) {
					d[i][j] = d[i - 1][j - 1];
				} else {
					d[i][j] = Math.min(d[i - 1][j], d[i][j - 1]) + 1;
				}
			}
		}

		int i = m;
		int j = n;

		// comparing the strings and pushing the similar characters to stack
		// from the end of the strings
		Stack<Character> stack = new Stack<Character>();
		while (i != 0 && j != 0) {
			if (s.charAt(i - 1) == t.charAt(j - 1)) {
				stack.push(s.charAt(i - 1));
				i--;
				j--;
			} else {
				if (d[i - 1][j] < d[i][j - 1]) {
					i--;
				} else {
					j--;
				}
			}
		}

		// obtaining the values of the stack and adding them to the string
		String lcsOut = "";
		while (!stack.empty()) {
			lcsOut = lcsOut + stack.pop() + " ";
		}

		// printing the longest common sequence and returning the string
		System.out.println(lcsOut);
		return lcsOut;
	}

	public static String recLcs(String s1, String t1) {
		// Objective 3
		List<String> lcs = new ArrayList<String>();

		// Calling the method that splits the table into quarters
		quarterTables(s1, t1, lcs);

		// Printing the longest common sequence and returning the string
		String lcsOut = "";
		for (int i = 0; i < lcs.size(); i++) {
			lcsOut = lcsOut + lcs.get(i).toString();
			System.out.print(lcs.get(i).toString());
		}
		return lcsOut;
	}

	public static void quarterTables(String s1, String t1, List<String> lcs) {
		// storing the lengths of the input strings
		int m = s1.length();
		int n = t1.length();

		// if the length of the first string is one, we compare the character to
		// the characters in second string and add it to lcs if found in second
		// string
		if (m == 1) {
			for (int i = 0; i < n; i++) {
				if (s1.charAt(0) == t1.charAt(i)) {
					lcs.add(s1 + " ");
				}
			}
		}

		// if the length of the second string is one, we compare the character
		// to
		// the characters in first string and add it to lcs if found in first
		// string
		else if (n == 1) {
			for (int i = 0; i < m; i++) {
				if (t1.charAt(0) == s1.charAt(i)) {
					lcs.add(t1 + " ");
				}
			}
		} else {
			String s2 = "";
			String t2 = "";
			String t3 = "";

			// Dividing the second string in half and assigning it to a new
			// string
			Stack<Character> stack = new Stack<Character>();
			for (int i = 0; i < n / 2; i++) {
				t2 = t2 + t1.charAt(i);
			}

			// Assigning the other half of the second string to another new
			// string based on whether the length of the second string is even
			// or odd
			if (n % 2 == 0) {
				for (int i = (n - (n / 2)); i < n; i++) {
					stack.push(t1.charAt(i));
				}
				while (!stack.isEmpty()) {
					t3 = t3 + stack.pop();
				}
			} else {
				for (int i = (n - (n / 2) - 1); i < n; i++) {
					stack.push(t1.charAt(i));
				}
				while (!stack.isEmpty()) {
					t3 = t3 + stack.pop();
				}
			}

			// Assigning the reverse of the first string to a new string
			for (int i = 0; i < m; i++) {
				stack.push(s1.charAt(i));
			}
			while (!stack.isEmpty()) {
				s2 = s2 + stack.pop();
			}

			// Calling twoRows method to determine the edit distance of the
			// complete table
			int[] originalFinalRow = twoRows(s1, t1);

			// Calling twoRows method to compute the forward middle row
			int[] forwardRow = twoRows(s1, t2);

			// Calling twoRows method to compute the reverse middle row
			int[] reverseRow = twoRows(s2, t3);

			// Using stack to reverse the reverse middle row so that the values
			// of the row can be added to forward middle row
			Stack<Integer> intStack = new Stack<Integer>();
			for (int x : reverseRow)
				intStack.push(x);
			int j = 0;
			while (!intStack.isEmpty()) {
				reverseRow[j] = intStack.pop();
				j++;
			}

			// Adding the forwardRow and reverseRow and assigning the values to
			// a new array
			int[] addTwoRows = new int[forwardRow.length];
			for (int i = 0; i < forwardRow.length; i++) {
				addTwoRows[i] = forwardRow[i] + reverseRow[i];
			}

			// comparing the values of the addTowRows array with the edit
			// distance of the complete table to obtain the location of the
			// vertical split
			int location = 0;
			for (int x = 0; x < addTwoRows.length; x++) {
				if (addTwoRows[x] == originalFinalRow[originalFinalRow.length - 1])
					location = x;
			}
			String temp = "";
			try {
				// if location is zero, it means that only one character has to
				// be deleted from either of the two rows
				// So, the other string can be directly added to lcs
				// we compare the lengths of the first two strings and then add
				// the unique characters of the smallest string to lcs
				if (location == 0) {
					if (s1.length() < t1.length()) {
						for (int i = 0; i < s1.length(); i++) {
							for (int k = 0; k < t1.length(); k++) {
								if (s1.charAt(i) == t1.charAt(k)) {
									if (temp.indexOf(s1.charAt(i)) < 0)
										temp = temp + s1.charAt(i) + " ";
								}
							}
						}
						lcs.add(temp);
					}
					if (t1.length() < s1.length()) {
						for (int i = 0; i < t1.length(); i++) {
							for (int k = 0; k < s1.length(); k++) {
								if (t1.charAt(i) == s1.charAt(k)) {
									if (temp.indexOf(t1.charAt(i)) < 0)
										temp = temp + t1.charAt(i) + " ";
								}
							}
						}
						lcs.add(temp);
					}
				} else {
					// dividing the strings in half to compute the quarters of
					// the table
					s2 = s1.substring(location);
					s1 = s1.substring(0, location);
					t3 = t1.substring(t2.length());

					// calling the recursive methods with the four
					// strings(obtained from the two original strings) as inputs
					quarterTables(s1, t2, lcs);
					quarterTables(s2, t3, lcs);
				}
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		}
	}

	public static int[] twoRows(String s, String t) {
		// storing the lengths of the input strings
		int m = s.length();
		int n = t.length();

		// storing the default values of the first row in table
		int[] firstRow = new int[m + 1];
		for (int i = 0; i <= m; i++) {
			firstRow[i] = i;
		}

		// storing the default first value in second row of the table
		int[] secondRow = new int[m + 1];
		secondRow[0] = 1;

		// until the end of the second string, we compare the characters and
		// compute the second row based on the values from first row and then
		// move the values of second row to the first row
		while (secondRow[0] != n + 1) {
			for (int i = 1; i <= m; i++) {
				if (s.charAt(i - 1) == t.charAt(secondRow[0] - 1)) {
					secondRow[i] = firstRow[i - 1];
				} else {
					secondRow[i] = Math.min(firstRow[i], secondRow[i - 1]) + 1;
				}
			}
			for (int i = 0; i <= m; i++) {
				firstRow[i] = secondRow[i];
			}
			secondRow[0]++;
		}
		
		//Returning the last row of the table
		return firstRow;
	}
}
