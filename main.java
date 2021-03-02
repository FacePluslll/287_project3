import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class main {
	private static TreeNode root;

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
		String Morse = encode(StringToConvert, root);
		System.out.println(Morse);
		
		scanner.close();
		scnr.close();
		// all other code should happen past this point...

	}

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
			Morse += encodeLetter(root.left, letter) + " ";
			Morse += encodeLetter(root.right, letter) + " ";
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
	}
