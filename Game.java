import java.util.ArrayList;
import java.util.Random;

//Enumerated type that describes the direction pent moves 
enum Direction
{
UP, DOWN, LEFT, RIGHT
}

public class Game
{
	//suggestion: use classes implementing interface to set, store and modify timer
	//add to pending changes file, once it exists
	public final long mFALL_TIME = 1500;
	//Constructors
	public Game(Board initBoard, /*Hscore currentHScores,*/ ArrayList<Pentomino> pieces)
	{
		fallTimer = new Timer (mFALL_TIME);
		field = initBoard.clone();
		//look for nicer solution
		blocks = (ArrayList<Pentomino>)pieces.clone();
		//highScore = currentHScores;
		pentPicker();
	}
	
	
	Play method
	//@return Gives the highscore including the current game
	//Plays the game
	
	public HScore play()
	{
		//generate random number for simulated user input
		Random genMove = new Random();
		//stores wheter game is over
		boolean isOver = false;
		//repeats while game is not over
		while (!isOver)
		{
			//if pentomino is at the bottom
			if (!checkMove (Direction.DOWN))
			{
				rowClearer();
				pentPicker();
				if (gameOverChecker())
					isOver = true;
			}
			if (!isOver)
			{
				pentFaller();
				//get user input once player class is done
				//select random move instead
				int randMove = genMove.nextInt(3);
				Direction randDirec;
				switch (randMove)
				{
				case 0: randDirec = Direction.LEFT;
				break;
				case 1: randDirec = Direction.RIGHT;
				break;
				case 2: randDirec = Direction.DOWN;
				break;
				}
				assert (randDirec != null);
				//apply move
				if (randMove < 2 && checkMove (randDirec))
					move (randDirec);
				else if (randMove == 2)
					fallPlace();
					
			}
		}
		
		System.out.println ("Oh no, the game is over!");
	}
	
	
	//Checking Methods
	/**@param direc Takes the direction game wants to move the pent 
	 * @return True if move is valid, false if not
	 */
	public boolean checkMove(Direction direc)
	{	
		assert (direc == Direction.LEFT || direc == Direction.RIGHT || direc == Direction.DOWN);
		Pentomino pentClone = pentUsed.clone();	
		//Moving to the right
		if (direc == Direction.RIGHT)
		{
			//Get the int position of left top if moved to the right
			Position right = new Position (pentPosition.getX(), pentPosition.getY());
			right.addX (1);
			int rPos = right.getPosNum(field.getHeight());
			//Check if it fits
			if (field.pentFits(pentClone, rPos))
			{
				return true;
			}
			return false;
		}
		//Moving to the left
		if (direc == Direction.LEFT)
		{
			//Get the int position of left top if moved to the left
			Position left = new Position (pentPosition.getX(), pentPosition.getY());
			left.addX (-1);
			int lPos = left.getPosNum(field.getHeight());
			//Check if it fits
			if (field.pentFits(pentClone, lPos))
			{
				return true;
			}
			return false;
		}
		
		//Moving downwards
		if (direc == Direction.DOWN)
		{
			//Get int position if pent moved downwards
			Position below = new Position (pentPosition.getX(), pentPosition.getY());
			below.addY (1);
			if (below.getY() >= field.getHeight() && 
				field.pentFits(pentClone, below.getPosNum (field.getHeight())))
				return true;
		}
		return false;
	}
	
	/**@param direc Takes the direction game wants to rotate the pent 
	 * @return True if move is valid, false if not
	 */
	public boolean checkRotate(Direction direc)
	{
		if (direc == Direction.UP)
		{
			//Clone Pentomino and rotate clockwise
			Pentomino pentClone = pentUsed.clone();
			pentClone.rotate();
			//Check if this rotated pent can fit
			if (field.pentFits (pentClone, pentPosition.getPosNum(field.getHeight())))
			{
				return true;
			}
			return false;
		}
		return false;
	}
	
	/** @return True if game over, else false
	 * If any overlap, game over
	 */
	public boolean gameOverChecker()
	{
		//This method should return the cell where the left top of the petomino should be placed when it appears on the board
		int pos = pentPosition.getPosNum(field.getHeight());
		//Checks if the pentomino can even be placed
		if (field.pentFits(pentUsed, pos))
		{
			return false;
		}
		return true;
	}

	/**@return void
	 * Makes the pentomino fall by 1 block after a set amount of time has elapsed
	 */
	private void pentFaller()
	{
		if (fallTimer.hasElapsed())
		{
			if (this.checkMove (Direction.DOWN))
				this.move (Direction.DOWN);
		}
	}
	
	/** @param direc indicates the direction the pentomino should move
	 * @return void
	 * Moves the block in a direction
	 */
	private void move(Direction direc)
	{
		assert direc == Direction.LEFT || direc == Direction.RIGHT || direc == Direction.DOWN;

		if (direc == Direction.LEFT && checkMove(Direction.LEFT)==true)
			pentPosition.addX(-1);
			
		else if (direc == Direction.RIGHT && checkMove(Direction.RIGHT)==true)
			pentPosition.addX(1);
		else if (direc == Direction.DOWN){
			pentPosition.addY(1);
		}
	}
	
	/**@param direc indicates the direction the pentomino should move
	 * @return void
	 * Turns the block in a direction
	 */
	private void turn(Direction direc)
	{
		if (direc == Direction.LEFT && checkRotate(Direction.LEFT)==true){
			this.pentUsed.rotate();
			this.pentUsed.rotate();
			this.pentUsed.rotate();
		}
		else if (direc == Direction.RIGHT && checkRotate(Direction.RIGHT)==true){
			this.pentUsed.rotate();
		}
		
	}
	
	/**@return void
	 * Places the block in the bottom most row (with a block)
	 */
	private void fallPlace()
	{
		//not desired:
		
		/*if (field.pentFits(this.pentUsed, pentPosition.getPosNum(field.getHeight()))) 
		{
			for (int i=0; i< this.pentUsed.getWidth(); i++) 
			{
				for (int j=0; j< this.pentUsed.getHeight(); j++) 
				{
					if (this.pentUsed.getElement(i,j) != 0) 
					{
						mMatrix.setCell(i + pentPosition.getX(), j + pentPosition.getY(), this.pentUsed.getElement(i,j));
					}
				}
			}
			blocks.add (pentUsed.clone());
		}*/
		/*what this method does:
		check whether pentomino fits in current position
		iterate through cells of pentomino and set value of an uninitialized data field mMatrix 
		to value of pentomino at respective position
		what this method should do:
		while pentomino may fall down by one row make it fall down
		once the respective bottom has been reached, place the pentomino on the Board ('field')*/
		
		
		//make pentomino fall down until it reaches the bottom
		while (this.checkMove(Direction.DOWN))
			this.move(Direction.DOWN);
		//place pentomino on the board
		field.placePent(pentUsed, pentPosition.getPosNum(field.getHeight()));
	}
	
	/**@return void
	 * Chooses from 1 of the pentominos and places it at the top of the board
	 */
	//Maybe consider "smart placing" of initial pentomino
	private void pentPicker()
	{
		fallTimer.reset();
		Random random = new Random();
		int index = random.nextInt(blocks.size());
		pentUsed = blocks.get(index);
		pentPosition = new Position((int)Math.ceil(field.getWidth() / 2),0);
	}
	
	
	/**@return void
	 * Checks for filled rows (bottom up) and removes them
	 */
	private void rowClearer()
	{
		int cRow = field.getHeight() - 1;
		while (cRow >= 0 && !field.isRowEmpty(cRow))
		{
			while (field.isRowFilled (cRow))
			{
				field.clearRow (cRow);
				rowMover (cRow);
			}
			--cRow;
		}
	}
	
	/**@return void
	 * After a row is cleared, replaces cleared rows with above rows.
	 * Does this until rows with all zeros is going to be moved
	 */
	private void rowMover(int index)
	{
		while (index > 0 && field.isRowEmpty (index) == false)
		{
			field.moveRow (index);
			index--;
		}
	}
	
	/**@param int Takes left top cell of the matrix the pentomino is in (not the left top cell of the pentomino) as an int
	 * @return new postion of left top after 'smart' rotating
	 */
	public Position smartRotate()
	{
		//Get Pent Position
		int moveLeft = 0;
		int moveUp = 0;
		//Get x-coordinate of the Pentomino
		int xCoor = pentPosition.getX();					
		int xCoorMax = field.getWidth();
			
		int yCoor = pentPosition.getY();
		int yCoorMax = field.getHeight();
		//How many columns are to the right
		int columnCounter = xCoorMax-xCoor;
		//How may rows are there to the bottom of the board
		int rowCounter = yCoorMax-yCoor;
		
		//How many times should we move the pent to the left
		if ((pentUsed.getHeight() - 1 - columnCounter)>0)
		{
			moveLeft = (pentUsed.getHeight() - 1) - columnCounter;
		}
		
		//How many times should we move the pent up
		if ((pentUsed.getWidth() - 1 - rowCounter)>0)
		{
			moveUp = (pentUsed.getWidth() - 1) - rowCounter;
		}
		
		//Get new position that adjust for overlap
		Position adjustedPent = pentPosition;
		adjustedPent.addX(-1*moveLeft);
		adjustedPent.addY(-1*moveUp);
		return adjustedPent;
	}

	//Contains a Board
	private Board field;
	
	//Contains the list of highscores
	//private HScore highScore;
	
	//Contains all the tetris pieces
	private ArrayList<Pentomino> blocks;
	
	//Contains the current Pentomino the game is using
	private Pentomino pentUsed;
	
	//Contains the (x,y) of a Pentomino
	private Position pentPosition;
	
	//Timer for the falling of tetris block
	private Timer fallTimer;

	private MatrixHandler mMatrix;

}
