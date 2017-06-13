/**
 * Little program I wrote to solve a Cubra-Cube / Snake-Puzzle-Cube.
 * Wasn't able to solve it myself and couldn't find any solution to my particular version of the cube. 
 * The Program defines a 3x3x3 'Field' in which the snake has to fit. It simply brute-forces every possible
 * move from every possible starting-position within the field. If you  have a different cube you need to 
 * change the 'private int[] snake' variable to correspond to your snake. The first section has to have 
 * a length of two for this program (The first two pieces are set manually before starting the recursion 
 * for the rest). Otherwise you would have to adjust the startPlacing() function. Every number in the 
 * snake-array denotes how many pieces there are until the next "turn". That's why the sections which 
 * have a length of 3 are denoted with 2s here, because the first of the three is already placed by the 
 * previous function call.      
 * 
 * @author Benjamin Welge
 *
 */
public class runIt {
	private static int dim = 3;
	private static int[] dirs = {-1,1};
	// The format of the specific snake cube I was too dumb to solve. 
	// The number always denotes how many cubes there are until the next turn.
	private static int[] snake = {2,1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,1,1,1,2,1,2,1};
	
	/**
	 * Runner function. Will start a recursive call to solve the snake for each position(x, y, z) within the field
	 * and for each possible direction (On x-,y- or z-axis either up or down). So total of x*y*z * 3 * 2 calls.
	 * Could be easily made more efficient by only checking half (or quarter) the starting positions since the cube
	 * is symmetrical. Program only takes a few seconds for 3x3x3 though.   
	 * @param args
	 */
	public static void main(String[] args) {
		for (int xStart = 0; xStart < dim;xStart++) {
			for (int yStart = 0; yStart < dim;yStart++) {
				for (int zStart = 0; zStart < dim;zStart++) {
					for (int axis = 0; axis < dim; axis++) {
						//positive dir.
						Field fld = new Field();
						//System.out.println("Starting From : ("+xStart+","+yStart+","+zStart+" axis: "+axis+"; direction +1");
						startPlacing(fld,0,xStart,yStart,zStart,axis,1);
						
						
						//negative dir.
						Field fld2 = new Field();
						//System.out.println("Starting From : ("+xStart+","+yStart+","+zStart+" axis: "+axis+"; direction -1");
						startPlacing(fld2.clone(),0,xStart,yStart,zStart,axis,-1);	
					}			
				}
			}
		}		
	}
	
	/**
	 * Function will place the first two pieces of the snake. 
	 * Snake's first 'block' has to have a length of 2 for this program to work
	 *  
	 * @param fl field
	 * @param snk position of the snake
	 * @param startx 
	 * @param starty
	 * @param startz
	 * @param axis: x, y or z axis (0, 1, 2)
	 * @param dir	Up or down on axis (-1 or 1)
	 */
	private static void startPlacing(Field fl, int snk, int startx, int starty, int startz, int axis, int dir) {
		
		Field fld = fl.clone();
		fld.placeField(startx, starty, startz);
		if (axis == 0) {
			startx+=dir;
		} else if (axis == 1) {
			starty+=dir;
		} else if (axis == 2) {
			startz+=dir;
		}
		if ( fld.tryPlace(startx, starty, startz)) {
			fld.placeField(startx, starty, startz);
			tryNext(fld,1,
					startx,starty,startz,
					axis,dir);
		}
		
		
	}
	
	/**
	 * Recursive function that is called with the LAST x,y,z position where a piece was placed
	 * and the LAST Axis/direction the snake had. So within the function checks all next possible moves and
	 * makes a recursive call for each.
	 * If the snake is finished the solution is printed out.
	 * 
	 * @param fl the current field
	 * @param snk	current snake position
	 * @param startx
	 * @param starty
	 * @param startz
	 * @param lstAxis
	 * @param lstDir
	 */
	private static void tryNext(Field fl, int snk, int startx, int starty, int startz, int lstAxis, int lstDir) {
		Field fld = (Field) fl.clone();
		
//		System.out.println("trying ("+startx+","+starty+","+startz+") snk: "+ snk);
//		if (snk > 20 ) {
//			System.out.println("Close ONE");
//		}
		Field[][] cache = new Field[3][2];
		for (int axis = 0; axis<3;axis++) {//For x y z
			if (axis != lstAxis) {
				for (int dir = 0; dir < dirs.length;dir++) { // for either up or down on each axis
					//Create cache with 
					cache[axis][dir] = (Field) fld.clone();
					
					//Check if the whole field is filled with 1s / Snake is finished
					if (cache[axis][dir].checkField()) {
						System.out.println("DONE");
						System.out.println("Moves:");
						//If so, output the successful path
						for (int m = 0; m<27;m++) {
							System.out.println(cache[axis][dir].path[m]);
						}
						break;
					}
					//If the next piece of the snake has length of 1
					if (snake[snk] == 1) {
						int startxx = startx;
						int startyy = starty;
						int startzz = startz;
						// Move the current position in the next direction
							//x axis
						if (axis == 0) {
							startxx+=dirs[dir];
							//y axis
						} else if (axis == 1) {
							startyy+=dirs[dir];
							//z axis
						} else if (axis == 2) {
							startzz+=dirs[dir];
						}
						//check if that position is free/within the field.
						if (cache[axis][dir].tryPlace(startxx, startyy, startzz)) {
							//if so, place the piece and recurse.
							cache[axis][dir].placeField(startxx, startyy, startzz);
							tryNext(cache[axis][dir].clone(),(snk+1),startxx,startyy,startzz,axis,dirs[dir]);
						}
						
						//Same thing for the case the current snake sections has a length of 2. 
					} else if (snake[snk] == 2) {
						int startxx = startx;
						int startyy = starty;
						int startzz = startz;
						if (axis == 0) {
							startxx+=dirs[dir];
						} else if (axis == 1) {
							startyy+=dirs[dir];
						} else if (axis == 2) {
							startzz+=dirs[dir];
						}
						if (cache[axis][dir].tryPlace(startxx, startyy, startzz)) {	
							cache[axis][dir].placeField(startxx, startyy, startzz);
							if (axis == 0) {
								startxx+=dirs[dir];
							} else if (axis == 1) {
								startyy+=dirs[dir];
							} else if (axis == 2) {
								startzz+=dirs[dir];
							}
							if (cache[axis][dir].tryPlace(startxx, startyy, startzz)) {
								cache[axis][dir].placeField(startxx, startyy, startzz);
								tryNext(cache[axis][dir].clone(),(snk+1),startxx,startyy,startzz,axis,dirs[dir]);
							}
						}
					}
				}
			}
		}
	}
}
