import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class MoveGenerator {

	/**
	 * CHESS BOARD Pawn = P/p Knight = K/k Bishop = B/b Rook = R/r Queen = Q/q
	 * King = A/a
	 */
	static String chessBoard[][] = {
			{ "r", "k", "b", "q", "a", "b", "k", "r" },
			{ "p", "p", "p", "p", "p", "p", "p", "p" },
			{ " ", " ", " ", " ", " ", " ", " ", " " },
			{ " ", " ", " ", " ", " ", " ", " ", " " },
			{ " ", " ", " ", " ", " ", " ", " ", " " },
			{ " ", " ", " ", " ", " ", " ", " ", " " },
			{ "P", "P", "P", "P", "P", "P", "P", "P" },
			{ "R", "K", "B", "Q", "A", "B", "K", "R" } };

	static String kingAPos = new String("74"); // Position of King A
	static String kingaPos = new String("04"); // Position of King a
	static int globalDepth = 4; // Global Depth
	private static int friendlyPawns;
	private static int friendlyRooks;
	private	static int friendlyKnights;
	private	static int friendlyBishops;
	private	static int friendlyQueen;
	private	static int enemyPawns;
	private	static int enemyRooks;
	private	static int enemyKnights;
	private	static int enemyBishops;
	private	static int enemyQueen;
	public static int friendlyScore;
	public static int enemyScore;

	/**
	 * Alpha-Beta Algorithm String Format = x1y1x2y2capturedPiece%%%%% Move a
	 * piece from x1,y1 to x2,y2 and captured piece is capturedPiece Move score
	 * is %%%%%
	 * @param  int depth, int beta, int alpha, String move, int player
	 * @return move + alpha or move + beta
	 */
	public static String alphaBeta(int depth, int beta, int alpha, String move,
			int player) {
		String list = getPossibleMoves();

		// If max depth is reached or no possible moves
		if (depth == 0 || list.length() == 0) {
			return move
					+ (Evaluate.evaluate(list.length(), depth) * (2 * player - 1));
		}

		// Move ordering to improve search time
		list = sortMoves(list);

		player = 1 - player; // either 1 or 0

		String[] moves = list.split(";");
		int length = moves.length;
		for (int i = 0; i < length; i++) {

			// Make a move
			makeMove(moves[i]);
			// Flip the board
			flipBoard();

			String str = alphaBeta(depth - 1, beta, alpha, moves[i], player);
			// Get the value of the move
			int value = Integer.valueOf(str.substring(5));

			// Flip the board
			flipBoard();
			// Undo the move
			undoMove(moves[i]);

			if (player == 0) {
				if (value <= beta) {
					beta = value;
					if (depth == getGlobalDepth()) {
						move = str.substring(0, 5);
					}
				}
			} else {
				if (value > alpha) {
					alpha = value;
					if (depth == getGlobalDepth()) {
						move = str.substring(0, 5);
					}
				}
			}
			if (alpha >= beta) {
				if (player == 0) {
					return move + beta;
				} else {
					return move + alpha;
				}
			}
		}
		if (player == 0) {
			return move + beta;
		} else {
			return move + alpha;
		}
	}

	/**
	 * Move Ordering
	 * @param  String list
	 * @return newListA, newListB
	 */
	public static String sortMoves(String list) {

		String[] moves = list.split(";");
		int length = moves.length;
		int[] score = new int[length];

		for (int i = 0; i < length; i++) {
			makeMove(moves[i]);
			score[i] = -Evaluate.evaluate(-1, 0);
			undoMove(moves[i]);
		}

		String newListA = new String();
		String newListB = list;
		for (int i = 0; i < Math.min(10, length); i++) { // First 10 moves only
			int max = -1000000;
			int maxInd = 0;
			for (int j = 0; j < length; j++) {
				if (score[j] > max) {
					max = score[j];
					maxInd = j;
				}
			}
			score[maxInd] = -1000000;
			newListA += moves[maxInd] + ";";
			newListB = newListB.replace(moves[maxInd] + ";", "");
		}

		return newListA + newListB;
	}

	/**
	 * Rotate a 2D array by 90 degrees
	 * @param  
	 * @return 
	 */
	public static void rotate() {
		String temp = new String();
		for (int i = 0; i < 8 / 2; i++) {
			for (int j = 0; j < (8 + 1) / 2; j++) {
				temp = chessBoard[i][j];
				chessBoard[i][j] = chessBoard[8 - 1 - j][i];
				chessBoard[8 - 1 - j][i] = chessBoard[8 - 1 - i][8 - 1 - j];
				chessBoard[8 - 1 - i][8 - 1 - j] = chessBoard[j][8 - 1 - i];
				chessBoard[j][8 - 1 - i] = temp;
			}
		}
	}

	/**
	 * Flip the board
	 * @param  
	 * @return 
	 */
	public static void flipBoard() {

		// Rotate by 180 degrees
		rotate();
		rotate();

		// Toggle the cases
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (Character.isLowerCase(chessBoard[i][j].charAt(0))) {
					chessBoard[i][j] = chessBoard[i][j].toUpperCase();
				} else {
					chessBoard[i][j] = chessBoard[i][j].toLowerCase();
				}
			}
		}

		// Swap the King positions
		int p = kingaPos.charAt(0) - '0';
		int q = kingaPos.charAt(1) - '0';
		int r = kingAPos.charAt(0) - '0';
		int s = kingAPos.charAt(1) - '0';
		kingAPos = (7 - p) + "" + (7 - q);
		kingaPos = (7 - r) + "" + (7 - s);
	}

	/**
	 * POSSIBLE MOVES String Format = x1y1x2y2capturedPiece Indicates a piece
	 * can move from x1,y1 to x2,y2 and captured piece is capturedPiece
	 * @param  
	 * @return list
	 */
	public static String getPossibleMoves() {

		String list = new String();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (chessBoard[i][j].equals("P")) {
					list += possibleP(i, j);
				} else if (chessBoard[i][j].equals("K")) {
					list += possibleK(i, j);
				} else if (chessBoard[i][j].equals("B")) {
					list += possibleB(i, j);
				} else if (chessBoard[i][j].equals("R")) {
					list += possibleR(i, j);
				} else if (chessBoard[i][j].equals("Q")) {
					list += possibleQ(i, j);
				} else if (chessBoard[i][j].equals("A")) {
					list += possibleA(i, j);
				}
			}
		}

		return list;
	}

	/**
	 * Print the Chess Board
	 */
	public static void printBoard() {

		for (int i = 0; i < 8; i++) {
			System.out.println("---------------------------------");
			System.out.print("|");
			for (int j = 0; j < 8; j++) {
				System.out.print(" " + chessBoard[i][j] + " |");
			}
			System.out.println();
		}
		System.out.println("---------------------------------\n");
		getPieceNumbers();
		setScores();
		System.out.println(friendlyScore);
		System.out.println(enemyScore);
		NaiveBayes bc = new NaiveBayes();
		String test[][] = {{Integer.toString(friendlyScore),Integer.toString(enemyScore)}};
		bc.prediction(test);
		Main.clbl.setText(bc.prediction);
		NeuralNetwork nn = new NeuralNetwork();
		if (Main.toggleNeural){
		nn.prediction(friendlyScore, enemyScore);
		Main.nlbl.setText(nn.prediction);
		} else {
			Main.nlbl.setText("");
		}
		
	}

	/**
	 * Check if move is allowed
	 * @param  String move
	 * @return boolean
	 */
	public static boolean isMoveAllowed(String move) {
		if (getPossibleMoves().contains(move))
			return true;
		return false;
	}

	/**
	 * Make a move
	 * @param  String str
	 */
	public static void makeMove(String str) {

		// Get the piece source
		int i = str.charAt(0) - '0';
		int j = str.charAt(1) - '0';
		// Get the piece destination
		int u = str.charAt(2) - '0';
		int v = str.charAt(3) - '0';

		// If the move is a pawn promotion, replace the pawn with Q/R/B/K
		if (str.charAt(4) == 'P') {
			chessBoard[1][i] = " ";
			chessBoard[0][j] = String.valueOf(str.charAt(3));
		} else {
			// Move the piece from i,j to u,v
			chessBoard[u][v] = chessBoard[i][j];
			chessBoard[i][j] = " ";

			// If the piece is King, store position
			if (chessBoard[u][v].equals("A")) {
				kingAPos = u + "" + v;
			}
		}
	}

	/**
	 * Undo a move
	 * @param  String str
	 */
	public static void undoMove(String str) {

		// Get the piece source
		int i = str.charAt(0) - '0';
		int j = str.charAt(1) - '0';
		// Get the piece destination
		int u = str.charAt(2) - '0';
		int v = str.charAt(3) - '0';

		// If the move is a pawn promotion, replace the pawn with Q/R/B/K
		if (str.charAt(4) == 'P') {
			chessBoard[1][i] = "P";
			chessBoard[0][j] = String.valueOf(str.charAt(2));
		} else {
			// Move the piece back from u,v to i,j
			chessBoard[i][j] = chessBoard[u][v];
			chessBoard[u][v] = String.valueOf(str.charAt(4));

			// If the piece is King, store position
			if (chessBoard[i][j].equals("A")) {
				kingAPos = i + "" + j;
			}
		}
	}

	/**
	 * Possible Moves of Pawn
	 * @param  int i, int j
	 * @return list.
	 */
	public static String possibleP(int i, int j) {
		String list = new String();

		// If at starting point
		if (i == 6) {
			// Two moves forward
			if (chessBoard[i - 2][j].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, i - 2, j, "P")) {
					list += i + "" + j + "" + (i - 2) + "" + j
							+ chessBoard[i - 2][j] + ";";
				}
			}
			// One move forward
			if (chessBoard[i - 1][j].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, i - 1, j, "P")) {
					list += i + "" + j + "" + (i - 1) + "" + j
							+ chessBoard[i - 1][j] + ";";
				}
			}

			// Diagonal Capture - Left, Upward
			if (j - 1 >= 0
					&& Character
							.isLowerCase(chessBoard[i - 1][j - 1].charAt(0))) {

				// Check if King is safe
				if (isKingSafe(i, j, i - 1, j - 1, "P")) {
					list += i + "" + j + "" + (i - 1) + "" + (j - 1)
							+ chessBoard[i - 1][j - 1] + ";";
				}
			}

			// Diagonal Capture - Right, Upward
			if (j + 1 < 8
					&& Character
							.isLowerCase(chessBoard[i - 1][j + 1].charAt(0))) {

				// Check if King is safe
				if (isKingSafe(i, j, i - 1, j + 1, "P")) {
					list += i + "" + j + "" + (i - 1) + "" + (j + 1)
							+ chessBoard[i - 1][j + 1] + ";";
				}
			}
		}

		// If at non-starting point - one move forward
		else if (i >= 2 && i < 6) {
			if (chessBoard[i - 1][j].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, i - 1, j, "P")) {
					list += i + "" + j + "" + (i - 1) + "" + j
							+ chessBoard[i - 1][j] + ";";
				}
			}

			// Diagonal Capture - Left, Upward
			if (j - 1 >= 0
					&& Character
							.isLowerCase(chessBoard[i - 1][j - 1].charAt(0))) {

				// Check if King is safe
				if (isKingSafe(i, j, i - 1, j - 1, "P")) {
					list += i + "" + j + "" + (i - 1) + "" + (j - 1)
							+ chessBoard[i - 1][j - 1] + ";";
				}
			}

			// Diagonal Capture - Right, Upward
			if (j + 1 < 8
					&& Character
							.isLowerCase(chessBoard[i - 1][j + 1].charAt(0))) {

				// Check if King is safe
				if (isKingSafe(i, j, i - 1, j + 1, "P")) {
					list += i + "" + j + "" + (i - 1) + "" + (j + 1)
							+ chessBoard[i - 1][j + 1] + ";";
				}
			}
		}

		// Promotion
		else if (i == 1) {
			String[] prom = { "Q", "R", "B", "K" };

			for (int k = 0; k < 4; k++) {

				// Promotion move
				if (chessBoard[i - 1][j].equals(" ")) {

					// Check if King is safe
					if (isKingSafe(i, j, i - 1, j, prom[k])) {
						list += j + "" + j + chessBoard[i - 1][j] + prom[k]
								+ "P;";
						chessBoard[i][j] = "P";
					}
				}

				// Diagonal Capture - Left, Upward
				if (j - 1 >= 0
						&& Character.isLowerCase(chessBoard[i - 1][j - 1]
								.charAt(0))) {

					// Check if King is safe
					if (isKingSafe(i, j, i - 1, j - 1, prom[k])) {
						list += j + "" + (j - 1) + chessBoard[i - 1][j - 1]
								+ prom[k] + "P;";
						chessBoard[i][j] = "P";
					}
				}

				// Diagonal Capture - Right, Upward
				if (j + 1 < 8
						&& Character.isLowerCase(chessBoard[i - 1][j + 1]
								.charAt(0))) {

					// Check if King is safe
					if (isKingSafe(i, j, i - 1, j + 1, prom[k])) {
						list += j + "" + (j + 1) + chessBoard[i - 1][j + 1]
								+ prom[k] + "P;";
						chessBoard[i][j] = "P";
					}
				}
			}
		}
		return list;
	}

	/**
	 * Possible Moves of Knight
	 * @param  int i, int j
	 * @return list.
	 */
	public static String possibleK(int i, int j) {
		String list = new String();

		int[] xMove = { 2, 1, -1, -2, -2, -1, 1, 2 };
		int[] yMove = { 1, 2, 2, 1, -1, -2, -2, -1 };

		for (int k = 0; k < 8; k++) {
			int u = i + xMove[k];
			int v = j + yMove[k];

			if ((u >= 0 && u < 8) && (v >= 0 && v < 8)) {

				if (Character.isLowerCase(chessBoard[u][v].charAt(0))
						|| chessBoard[u][v].equals(" ")) {

					// Check if King is safe
					if (isKingSafe(i, j, u, v, "K")) {
						list += i + "" + j + "" + u + "" + v + chessBoard[u][v]
								+ ";";
					}
				}
			}
		}

		return list;
	}

	/**
	 * Possible Moves of Bishop
	 * @param  int i, int j
	 * @return list.
	 */
	public static String possibleB(int i, int j) {
		String list = new String();

		// Check the Diagonal - Left, Upward
		int u = i - 1;
		int v = j - 1;
		while (u >= 0 && v >= 0) {
			if (Character.isLowerCase(chessBoard[u][v].charAt(0))
					|| chessBoard[u][v].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, u, v, "B")) {
					list += i + "" + j + "" + u + "" + v + chessBoard[u][v]
							+ ";";
				}
			}
			if (!chessBoard[u][v].equals(" "))
				break;
			u--;
			v--;
		}

		// Check the Diagonal - Right, Upward
		u = i + 1;
		v = j - 1;
		while (u < 8 && v >= 0) {
			if (Character.isLowerCase(chessBoard[u][v].charAt(0))
					|| chessBoard[u][v].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, u, v, "B")) {
					list += i + "" + j + "" + u + "" + v + chessBoard[u][v]
							+ ";";
				}
			}
			if (!chessBoard[u][v].equals(" "))
				break;
			u++;
			v--;
		}

		// Check the Diagonal - Right, Downward
		u = i + 1;
		v = j + 1;
		while (u < 8 && v < 8) {
			if (Character.isLowerCase(chessBoard[u][v].charAt(0))
					|| chessBoard[u][v].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, u, v, "B")) {
					list += i + "" + j + "" + u + "" + v + chessBoard[u][v]
							+ ";";
				}
			}
			if (!chessBoard[u][v].equals(" "))
				break;
			u++;
			v++;
		}

		// Check the Diagonal - Left, Downward
		u = i - 1;
		v = j + 1;
		while (u >= 0 && v < 8) {
			if (Character.isLowerCase(chessBoard[u][v].charAt(0))
					|| chessBoard[u][v].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, u, v, "B")) {
					list += i + "" + j + "" + u + "" + v + chessBoard[u][v]
							+ ";";
				}
			}
			if (!chessBoard[u][v].equals(" "))
				break;
			u--;
			v++;
		}

		return list;
	}

	/**
	 * Possible Moves of Rook
	 * @param  int i, int j
	 * @return list.
	 */
	public static String possibleR(int i, int j) {
		String list = new String();

		// Check the Row - Forward
		for (int v = j + 1; v < 8; v++) {
			if (Character.isLowerCase(chessBoard[i][v].charAt(0))
					|| chessBoard[i][v].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, i, v, "R")) {
					list += i + "" + j + "" + i + "" + v + chessBoard[i][v]
							+ ";";
				}
			}
			if (!chessBoard[i][v].equals(" "))
				break;
		}

		// Check the Row - Backward
		for (int v = j - 1; v >= 0; v--) {
			if (Character.isLowerCase(chessBoard[i][v].charAt(0))
					|| chessBoard[i][v].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, i, v, "R")) {
					list += i + "" + j + "" + i + "" + v + chessBoard[i][v]
							+ ";";
				}
			}
			if (!chessBoard[i][v].equals(" "))
				break;
		}

		// Check the Column - Upward
		for (int u = i + 1; u < 8; u++) {
			if (Character.isLowerCase(chessBoard[u][j].charAt(0))
					|| chessBoard[u][j].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, u, j, "R")) {
					list += i + "" + j + "" + u + "" + j + chessBoard[u][j]
							+ ";";
				}
			}
			if (!chessBoard[u][j].equals(" "))
				break;
		}

		// Check the Column - Downward
		for (int u = i - 1; u >= 0; u--) {
			if (Character.isLowerCase(chessBoard[u][j].charAt(0))
					|| chessBoard[u][j].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, u, j, "R")) {
					list += i + "" + j + "" + u + "" + j + chessBoard[u][j]
							+ ";";
				}
			}
			if (!chessBoard[u][j].equals(" "))
				break;
		}

		return list;
	}

	/**
	 * Possible Moves of Queen
	 * @param  int i, int j
	 * @return list.
	 */
	public static String possibleQ(int i, int j) {
		String list = new String();

		// Check the Row - Forward
		for (int v = j + 1; v < 8; v++) {
			if (Character.isLowerCase(chessBoard[i][v].charAt(0))
					|| chessBoard[i][v].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, i, v, "Q")) {
					list += i + "" + j + "" + i + "" + v + chessBoard[i][v]
							+ ";";
				}
			}
			if (!chessBoard[i][v].equals(" "))
				break;
		}

		// Check the Row - Backward
		for (int v = j - 1; v >= 0; v--) {
			if (Character.isLowerCase(chessBoard[i][v].charAt(0))
					|| chessBoard[i][v].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, i, v, "Q")) {
					list += i + "" + j + "" + i + "" + v + chessBoard[i][v]
							+ ";";
				}
			}
			if (!chessBoard[i][v].equals(" "))
				break;
		}

		// Check the Column - Upward
		for (int u = i - 1; u >= 0; u--) {
			if (Character.isLowerCase(chessBoard[u][j].charAt(0))
					|| chessBoard[u][j].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, u, j, "Q")) {
					list += i + "" + j + "" + u + "" + j + chessBoard[u][j]
							+ ";";
				}
			}
			if (!chessBoard[u][j].equals(" "))
				break;
		}

		// Check the Column - Downward
		for (int u = i + 1; u < 8; u++) {
			if (Character.isLowerCase(chessBoard[u][j].charAt(0))
					|| chessBoard[u][j].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, u, j, "Q")) {
					list += i + "" + j + "" + u + "" + j + chessBoard[u][j]
							+ ";";
				}
			}
			if (!chessBoard[u][j].equals(" "))
				break;
		}

		// Check the Diagonal - Left, Upward
		int u = i - 1;
		int v = j - 1;
		while (u >= 0 && v >= 0) {
			if (Character.isLowerCase(chessBoard[u][v].charAt(0))
					|| chessBoard[u][v].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, u, v, "Q")) {
					list += i + "" + j + "" + u + "" + v + chessBoard[u][v]
							+ ";";
				}
			}
			if (!chessBoard[u][v].equals(" "))
				break;
			u--;
			v--;
		}

		// Check the Diagonal - Right, Upward
		u = i - 1;
		v = j + 1;
		while (u >= 0 && v < 8) {
			if (Character.isLowerCase(chessBoard[u][v].charAt(0))
					|| chessBoard[u][v].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, u, v, "Q")) {
					list += i + "" + j + "" + u + "" + v + chessBoard[u][v]
							+ ";";
				}
			}
			if (!chessBoard[u][v].equals(" "))
				break;
			u--;
			v++;
		}

		// Check the Diagonal - Right, Downward
		u = i + 1;
		v = j + 1;
		while (u < 8 && v < 8) {
			if (Character.isLowerCase(chessBoard[u][v].charAt(0))
					|| chessBoard[u][v].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, u, v, "Q")) {
					list += i + "" + j + "" + u + "" + v + chessBoard[u][v]
							+ ";";
				}
			}
			if (!chessBoard[u][v].equals(" "))
				break;
			u++;
			v++;
		}

		// Check the Diagonal - Left, Downward
		u = i + 1;
		v = j - 1;
		while (u < 8 && v >= 0) {
			if (Character.isLowerCase(chessBoard[u][v].charAt(0))
					|| chessBoard[u][v].equals(" ")) {

				// Check if King is safe
				if (isKingSafe(i, j, u, v, "Q")) {
					list += i + "" + j + "" + u + "" + v + chessBoard[u][v]
							+ ";";
				}
			}
			if (!chessBoard[u][v].equals(" "))
				break;
			u++;
			v--;
		}

		return list;
	}

	/**
	 * Possible Moves of King
	 * @param  int i, int j
	 * @return list.
	 */
	public static String possibleA(int i, int j) {

		String list = new String();

		// Get the min and max
		int minI = i - 1 < 0 ? i : i - 1;
		int maxI = i + 1 > 7 ? i : i + 1;
		int minJ = j - 1 < 0 ? j : j - 1;
		int maxJ = j + 1 > 7 ? j : j + 1;

		// Add all the possible moves to the list
		for (int u = minI; u <= maxI; u++) {
			for (int v = minJ; v <= maxJ; v++) {
				if (u == i && v == j)
					continue;

				if (Character.isLowerCase(chessBoard[u][v].charAt(0))
						|| chessBoard[u][v].equals(" ")) {

					// Store the King's position
					kingAPos = u + "" + v;

					// Check if King is safe
					if (isKingSafe(i, j, u, v, "A")) {
						list += i + "" + j + "" + u + "" + v + chessBoard[u][v]
								+ ";";
					}

					// Store the King's position
					kingAPos = i + "" + j;
				}
			}
		}

		return list;
	}

	/**
	 * Check if King is safe if piece is moved from I,J to U,V
	 * @param  int I, int J, int U, int V, String piece
	 * @return boolean.
	 */
	public static boolean isKingSafe(int I, int J, int U, int V, String piece) {

		// Move the Piece to U,V
		String temp = chessBoard[U][V];
		chessBoard[U][V] = piece;
		chessBoard[I][J] = " ";

		int i = kingAPos.charAt(0) - '0';
		int j = kingAPos.charAt(1) - '0';

		/* Knight */
		int[] xMove = { 2, 1, -1, -2, -2, -1, 1, 2 };
		int[] yMove = { 1, 2, 2, 1, -1, -2, -2, -1 };

		for (int k = 0; k < 8; k++) {
			int u = i + xMove[k];
			int v = j + yMove[k];
			if ((u >= 0 && u < 8) && (v >= 0 && v < 8)) {
				if (chessBoard[u][v].equals("k")) {
					// Move back the Piece and piece at U,V
					chessBoard[I][J] = piece;
					chessBoard[U][V] = temp;

					return false;
				}
			}
		}

		/* Queen, Bishop */
		// Check the Diagonal - Left, Upward
		int u = i - 1;
		int v = j - 1;
		while (u >= 0 && v >= 0) {
			if (chessBoard[u][v].equals("q") || chessBoard[u][v].equals("b")) {
				// Move back the Piece and piece at U,V
				chessBoard[I][J] = piece;
				chessBoard[U][V] = temp;

				return false;
			}
			if (!chessBoard[u][v].equals(" "))
				break;
			u--;
			v--;
		}
		// Check the Diagonal - Right, Upward
		u = i - 1;
		v = j + 1;
		while (u >= 0 && v < 8) {
			if (chessBoard[u][v].equals("q") || chessBoard[u][v].equals("b")) {
				// Move back the Piece and piece at U,V
				chessBoard[I][J] = piece;
				chessBoard[U][V] = temp;

				return false;
			}
			if (!chessBoard[u][v].equals(" "))
				break;
			u--;
			v++;
		}
		// Check the Diagonal - Right, Downward
		u = i + 1;
		v = j + 1;
		while (u < 8 && v < 8) {
			if (chessBoard[u][v].equals("q") || chessBoard[u][v].equals("b")) {
				// Move back the Piece and piece at U,V
				chessBoard[I][J] = piece;
				chessBoard[U][V] = temp;

				return false;
			}
			if (!chessBoard[u][v].equals(" "))
				break;
			u++;
			v++;
		}
		// Check the Diagonal - Left, Downward
		u = i + 1;
		v = j - 1;
		while (u < 8 && v >= 0) {
			if (chessBoard[u][v].equals("q") || chessBoard[u][v].equals("b")) {
				// Move back the Piece and piece at U,V
				chessBoard[I][J] = piece;
				chessBoard[U][V] = temp;

				return false;
			}
			if (!chessBoard[u][v].equals(" "))
				break;
			u++;
			v--;
		}

		/* Queen, Rook */
		// Check the Row - Forward
		for (v = j + 1; v < 8; v++) {
			if (chessBoard[i][v].equals("q") || chessBoard[i][v].equals("r")) {
				// Move back the Piece and piece at U,V
				chessBoard[I][J] = piece;
				chessBoard[U][V] = temp;

				return false;
			}
			if (!chessBoard[i][v].equals(" "))
				break;
		}
		// Check the Row - Backward
		for (v = j - 1; v >= 0; v--) {
			if (chessBoard[i][v].equals("q") || chessBoard[i][v].equals("r")) {
				// Move back the Piece and piece at U,V
				chessBoard[I][J] = piece;
				chessBoard[U][V] = temp;

				return false;
			}
			if (!chessBoard[i][v].equals(" "))
				break;
		}
		// Check the Column - Upward
		for (u = i - 1; u >= 0; u--) {
			if (chessBoard[u][j].equals("q") || chessBoard[u][j].equals("r")) {
				// Move back the Piece and piece at U,V
				chessBoard[I][J] = piece;
				chessBoard[U][V] = temp;

				return false;
			}
			if (!chessBoard[u][j].equals(" "))
				break;
		}
		// Check the Column - Downward
		for (u = i + 1; u < 8; u++) {
			if (chessBoard[u][j].equals("q") || chessBoard[u][j].equals("r")) {
				// Move back the Piece and piece at U,V
				chessBoard[I][J] = piece;
				chessBoard[U][V] = temp;

				return false;
			}
			if (!chessBoard[u][j].equals(" "))
				break;
		}

		/* King, Pawn */
		int minI = i - 1 < 0 ? i : i - 1;
		int maxI = i + 1 > 7 ? i : i + 1;
		int minJ = j - 1 < 0 ? j : j - 1;
		int maxJ = j + 1 > 7 ? j : j + 1;
		for (u = minI; u <= maxI; u++) {
			for (v = minJ; v <= maxJ; v++) {
				if (u == i && v == j)
					continue;

				// King
				if (chessBoard[u][v].equals("a")) {
					// Move back the Piece and piece at U,V
					chessBoard[I][J] = piece;
					chessBoard[U][V] = temp;

					return false;
				}

				// Pawn
				if (u == minI && v == minJ && chessBoard[u][v].equals("p")) {
					// Move back the Piece and piece at U,V
					chessBoard[I][J] = piece;
					chessBoard[U][V] = temp;

					return false;
				}
				if (u == minI && v == maxJ && chessBoard[u][v].equals("p")) {
					// Move back the Piece and piece at U,V
					chessBoard[I][J] = piece;
					chessBoard[U][V] = temp;

					return false;
				}
				if (u == maxI && v == minJ && chessBoard[u][v].equals("p")) {
					// Move back the Piece and piece at U,V
					chessBoard[I][J] = piece;
					chessBoard[U][V] = temp;

					return false;
				}
				if (u == maxI && v == maxJ && chessBoard[u][v].equals("p")) {
					// Move back the Piece and piece at U,V
					chessBoard[I][J] = piece;
					chessBoard[U][V] = temp;

					return false;
				}
			}
		}

		// Move back the Piece and piece at U,V
		chessBoard[I][J] = piece;
		chessBoard[U][V] = temp;

		return true;
	}
	
	/**
	 * Check the frequency of the pieces on the board.
	 * @param  Strin [][] array, String string
	 * @return freq
	 */
	public static int findFreqOfPieces(String[][] array, String string) {
		int freq = 0;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				if (array[i][j].equals(string)) {
					freq++;
				}

			}

		}

		return freq;

	}
	
	/**
	 * Get the chess board array 
	 * @return chessBoard
	 */
	public static String[][] getChessBoard() {
		return chessBoard;
	}

	/**
	 * Find the number of each piece in the array using the frequency method.
	 */
	public static void getPieceNumbers(){
		setFriendlyPawns(findFreqOfPieces(chessBoard, "P" ));
		setFriendlyRooks(findFreqOfPieces(chessBoard, "R" ));
		setFriendlyKnights(findFreqOfPieces(chessBoard, "K" ));
		setFriendlyBishops(findFreqOfPieces(chessBoard, "B" ));
		setFriendlyQueen(findFreqOfPieces(chessBoard, "Q" ));
		setEnemyPawns(findFreqOfPieces(chessBoard, "p" ));
		setEnemyRooks(findFreqOfPieces(chessBoard, "r" ));
		setEnemyKnights(findFreqOfPieces(chessBoard, "k" ));
		setEnemyBishops(findFreqOfPieces(chessBoard, "b" ));
		setEnemyQueen(findFreqOfPieces(chessBoard, "q" ));	
	
	}

	/**
	 * Get the global depth of the AlphaBeta
	 * @return globalDepth
	 */
	public static int getGlobalDepth() {
		return globalDepth;
	}

	/**
	 * Set the global depth.
	 * @param  int globalDepth
	 */
	public void setGlobalDepth(int globalDepth) {
		this.globalDepth = globalDepth;
	}

	/**
	 * Return the amount of friendly pawns on the board
	 * @return friendlyPawns
	 */
	public static int getFriendlyPawns() {
		return friendlyPawns;
	}
	
	/**
	 * Set the amount of friendly pawns on the board
	 * @param int num
	 */
	public static void setFriendlyPawns(int num) {
		friendlyPawns = num;
	}

	/**
	 * Return the amount of friendly knights on the board
	 * @return friendlyKights
	 */
	public static int getFriendlyKnights() {
		return friendlyKnights;
	}
    
	/**
	 * Set the amount of friendly knights on the board
	 * @param int num
	 */
	public static void setFriendlyKnights(int num) {
	 friendlyKnights  = num;
	}

	/**
	 * Return the amount of friendly Rooks on the board
	 * @return friendlyRooks
	 */
	public static int getFriendlyRooks() {
		return friendlyRooks;
	}

	/**
	 * Set the amount of friendly rooks on the board
	 * @param int num
	 */
	public static void setFriendlyRooks(int num) {
		friendlyRooks = num;
	}

	/**
	 * Return the amount of friendly bishops on the board
	 * @return friendlyBishops
	 */
	public static int getFriendlyBishops() {
		return friendlyBishops;
	}

	/**
	 * Set the amount of friendly bishops on the board
	 * @param int num
	 */
	public static void setFriendlyBishops(int num) {
		 friendlyBishops = num;
	}
	
	/**
	 * Return the amount of friendly queen on the board
	 * @return friendlyQueen
	 */
	public static int getFriendlyQueen() {
		return friendlyQueen;
	}

	/**
	 * Set the amount of friendly queen on the board
	 * @param int num
	 */
	public static void setFriendlyQueen(int num) {
		 friendlyQueen = num;
	}

	/**
	 * Return the amount of enemy pawns on the board
	 * @return enemyPawns
	 */
	public static int getEnemyPawns() {
		return enemyPawns;
	}

	/**
	 * Set the amount of enemy pawns on the board
	 * @param int num
	 */
	public static void setEnemyPawns(int num) {
	 enemyPawns = num;
	}

	/**
	 * Return the amount of enemy rooks on the board
	 * @return enemyRooks
	 */
	public static int getEnemyRooks() {
		return enemyRooks;
	}

	/**
	 * Set the amount of enemy rooks on the board
	 * @param int num
	 */
	public static void setEnemyRooks(int num) {
		enemyRooks = num;
	}

	/**
	 * Return the amount of enemy knights on the board
	 * @return enemyKnights
	 */
	public static int getEnemyKnights() {
		return enemyKnights;
	}
	
	/**
	 * Set the amount of enemy knights on the board
	 * @param int num
	 */
	public static void setEnemyKnights(int num) {
		enemyKnights = num;
	}

	/**
	 * Return the amount of enemy bishops on the board
	 * @return enemyBishops
	 */
	public static int getEnemyBishops() {
		return enemyBishops;
	}

	/**
	 * Set the amount of enemy bishops on the board
	 * @param int num
	 */
	public static void setEnemyBishops(int num) {
		 enemyBishops = num;
	}

	/**
	 * Return the amount of enemy queen on the board
	 * @return enemyQueen
	 */
	public static int getEnemyQueen() {
		return enemyQueen;
	}

	/**
	 * Set the amount of enemy queen on the board
	 * @param int num
	 */
	public static void setEnemyQueen(int num) {
		 enemyQueen = num;
	}
	
	/**
	 * Set the scores for both friendly and enemy pieces
	 */
	public static void setScores()
	{
		
		int fPawnScore = getFriendlyPawns();
		int fKnightScore = getFriendlyKnights() * 3;
		int fBishopScore = getFriendlyBishops() * 3;
		int fRookScore = getFriendlyRooks() * 5;
		int fQueenScore = getFriendlyQueen() * 9;		
		friendlyScore = fPawnScore + fKnightScore + fBishopScore + fRookScore + fQueenScore;
		
		int ePawnScore = getEnemyPawns();
		int eKnightScore = getEnemyKnights() * 3;
		int eBishopScore = getEnemyBishops() * 3;
		int eRookScore = getEnemyRooks() * 5;
		int eQueenScore = getEnemyQueen() * 9;		
		enemyScore = ePawnScore + eKnightScore + eBishopScore + eRookScore + eQueenScore;
	}
	
}
