import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class main {
	public static void main(String[] args) throws FileNotFoundException {
		FileInputStream inputFile = new FileInputStream("Morse_Code.txt");
		Scanner scnr = new Scanner(inputFile);

		TreeNode root;

		String line = "";
		ArrayList<String[]> inputArray = new ArrayList<String[]>();

		// used for getting the morse code and converting it into a BinaryTree
		while (scnr.hasNext()) {
			line = scnr.nextLine();
			String[] tempArr = new String[2];
			StringBuilder tempStr = new StringBuilder();
			for (int x = 0; x < line.length(); x++) {
				if (x == 0) {
					tempArr[0] = String.valueOf(line.charAt(x));
				} else {
					tempStr.append(line.charAt(x));
				}
			}
			tempArr[1] = tempStr.toString();
			inputArray.add(tempArr);
		}
		
		root = buildTreeNode(inputArray);
		
		//creates another scanner that takes in user input
		//calls encode method
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter a sentence to convert to morse Code: ");
		String StringToConvert = scanner.nextLine();

        if(StringToConvert.charAt(0) == '.' || StringToConvert.charAt(0) == '-'){
            // Our program outputs the message with a single space between codes and a double space between words
		    System.out.println("Encoded String: " + StringToConvert);
		    String decodedMsg = decode(StringToConvert, root);
		    System.out.println("Decoded String: " + decodedMsg);
        }
        else{
            // Our program outputs the message with a single space between codes and a double space between words
		    String Morse = encode(StringToConvert, root);
		    System.out.println("Encoded String: " + Morse);
		    String decodedMsg = decode(Morse, root);
		    System.out.println("Decoded String: " + decodedMsg);
        }
        
		// Closes open scanners and files
		scanner.close();
		scnr.close();
	}

	//Erik
	//Builds a tree with the given list from the input file
	public static TreeNode buildTreeNode(ArrayList<String[]> inputList) {
		// root node will be null since a height of 0 leads to no symbols in a string
		TreeNode root = new TreeNode(null);
		ArrayList<String[]> list = inputList;

		int height = 0;
		int amountToGet = 0;
		while (list.size() > 0) {
			height++;

			// this sets the letters that we are looking for and ends the loop for the set
			// height, this is to avoid a infinte loop
			amountToGet = (int) Math.pow(2.0, height);

			int i = 0;
			while (amountToGet > 0 && list.size() > 0) {
				String[] tempArr = list.get(i);
				// if tempArr morse code is the same length as height, then found a node to add
				// to tree
				if (tempArr[1].length() == height) {
					// remove from amount to get and set curTreeNode
					amountToGet--;
					TreeNode curTreeNode = root;
					String str = tempArr[1];

					// only interates the length of the string and determines where the node gets
					// added
					for (int x = 0; x < height; x++) {
						if (str.charAt(x) == '.') {
							if (curTreeNode.left == null) {
								curTreeNode.left = new TreeNode(tempArr);
								list.remove(i);
								break;
							} else {
								curTreeNode = curTreeNode.left;
							}
						} else if (str.charAt(x) == '-') {
							if (curTreeNode.right == null) {
								curTreeNode.right = new TreeNode(tempArr);
								list.remove(i);
								break;
							} else {
								curTreeNode = curTreeNode.right;
							}
						}
					}
				}
				// not the string we are looking for yet add to i
				else {
					i++;
				}

			}
		}
		return root;
	}
	
	//Sonal
	//receives the root and the user input
	//iterates through length of user input
	//calls new method called encodeLetter
	public static String encode(String input, TreeNode root) {
		String letter;
		String Morse = "";
		for (int i=0; i< input.length(); i++) {
			letter = input.substring(i, i+1);
			Morse += encodeLetter(root.left, letter);
			Morse += encodeLetter(root.right, letter);
			Morse = Morse.concat(" ");
		}
		return Morse;
	}
	
	//Takes in the root and the letters of the input string and accounts for empty spaces
	public static String encodeLetter(TreeNode root, String letter) {
		if (letter == " ") {
			return "";
		}
		if (letter.equals(root.val[0])) {
			return root.val[1];
		}else {
			String Code = "";
			if (root.left != null) {
				Code = encodeLetter(root.left, letter);
			}
			if (Code == "" && root.right != null) {
				Code = encodeLetter(root.right, letter);
			}
			return Code;
		}
	}

	//Joey
	// A method that will take in an encoded string with the codes being seperated by a single space and a word is sperated with two spaces
	// Also is expected the Tree as a parameter
	// This method decodes a given encoded String to its english form
	public static String decode(String input, TreeNode root) {
		TreeNode copy = root; // Creates a copy of the root which is the start of the tree
		String[] inpMsg = input.split(" "); // Splits the encoded input at all the spaces which means it will create an array of each code with a blank string for the double space which is the space that would seperate the words
		String decodeMsg = ""; // A string that will be the decoded message
		Character c = ' '; // A character that will be used to get the character at a point in the string
		for (String code : inpMsg) { // For loop to iterate through the array of codes, inpMsg
			if (code.equals("")) { // Checks to see if it is a blank string, if it is that means its a space
				decodeMsg = decodeMsg.concat(" "); // If it is, then add a space to the decoded message
				continue; // Go to next element in the array (iterate the loop)
			}
			else { // If it is not a space
				root = copy; // Set the root equal to the beginning of the tree
				for (int i = 0; i < code.length(); i++) { // Iterate through the code, which will end at the node of the decoded letter
					c = code.charAt(i); // Set a character equal to the character at the index of the string
					if (c.equals('-')) { // Check to see if it is a dash
						root = root.right; // If it is then go to the right branch of the tree
					}
					else if (c.equals('.')){ // Check to see if it is a dot
						root = root.left; // If it is then go to the left branch of the tree
					}
				}
				decodeMsg = decodeMsg.concat(root.val[0]); //concatenate the decodeMsg with the letter that was found, then go to the next code
			}
		}
		return decodeMsg; // Return the completed decoded message
	}
}
