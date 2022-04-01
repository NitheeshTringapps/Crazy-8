package automaticCardGame;

/**
 * @author Nitheesh
 * Created an new game for playerOne and playerTwo then
 * starts a new tournament by calling AutomaticGameEngine.
 */

/*
 * Note: Check GameEngine class of cardgame package to see the implementation
 * of playing n number of players individually & manually
 * without any automation
 */
public class Crazy8Game {
	/**]
	 * Starts a new Game and prints the winner and their score
	 */
	public static void main(String[] args) {
		AutomaticGameEngine automaticGameEngine = new AutomaticGameEngine();
		PlayerOne playerOne = new PlayerOne();
		PlayerTwo playerTwo = new PlayerTwo();
		String result = automaticGameEngine.start(playerOne, playerTwo);
		System.out.println(result);
	}
}
