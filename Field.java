import java.lang.reflect.Array;
import java.util.ArrayList;
/**
 * Class that holds the 3x3x3 'field' in which the snake needs to fit. 
 * @author Benjamin Welge
 *
 */
public class Field {
 private int[][][] field;
 
 //variable that holds the amount of already placed pieces of the snake within this field.
 public int counter = 0;
 
 //holds the path of the snake within the field ( sorted list of coordinates of each placed piece)
 public String[] path;
 
 /**
  * Simply initiate field as 3x3x3 filled with '0's.
  */
 protected  Field() {
	 field = new int[3][3][3];
	 for (int i=0;i<field.length;i++) {
		 for (int j=0;j<field.length;j++) {
			 for (int k=0;k<field.length;k++) {
				 field[i][j][k] = 0;
			 }
		 }
	 }
	 path = new String[27];


	 
 }

public int[][][] getField() {
	return field;
}

/**
 * Deep copy the 3-dimensional array.
 */
@Override
public Field clone() {
	Field ff = new Field();
	int[][][] f = new int[3][3][3];
	for (int i=0;i<3;i++) {
		for (int j=0;j<3;j++) {
			for (int k=0;k<3;k++) {
				f[i][j][k] = field[i][j][k];
			}
		}
	}
	ff.setField(f);
	ff.path = path.clone();
	ff.counter = counter;
	return ff;
}

/**
 * function to place a snake piece in the field.
 * @param x
 * @param y
 * @param z
 */
public void placeField(int x,int y,int z) {
	this.path[counter] = "("+x+";"+y+";"+z+")";
	counter++;
	this.field[x][y][z] = 1;
}

/**
 * function to check whether pos(x,y,z) is free and within range of the field.
 * @param x
 * @param y
 * @param z
 * @return
 */
public boolean tryPlace(int x, int y, int z) {
	if (x > -1 && x < 3 && y > -1 && y < 3 && z > -1 && z < 3) {
		if (this.field[x][y][z] == 0) {
			return true;
		} else {
			return false;
		}
		
	}
	return false;
	
}

public void setField(int[][][] field) {
	this.field = field;
}

/**
 * Check if field is completely filled with 1s / Snake is finished.
 * @return
 */
public boolean checkField() {
	 for (int i=0;i<field.length;i++) {
		 for (int j=0;j<field.length;j++) {
			 for (int k=0;k<field.length;k++) {
				 if (field[i][j][k] == 0 || field[i][j][k] != 1) {
					 return false;
				 }
			 }
		 }
	 }
	return true;
}
}
