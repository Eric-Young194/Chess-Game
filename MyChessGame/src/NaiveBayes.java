import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class NaiveBayes {
	
	
	private static String trainingdata[][] = {
	{"39", "10",  "white"},
	{"10", "20",  "black"},
	{"30", "20",  "white"},
	{"5",  "15",  "black"},
	{"14",  "6",  "white"},
	{"37", "25",  "white"},
	{"25", "30",  "black"},
	{"25", "10",  "white"},
	{"9",  "12",  "black"},
	{"5",  "9",   "black"},
	{"30", "39",  "black"},
	{"21", "30",  "black"},
	{"36", "25",  "white"},
	{"39", "33",  "white"},
	{"36", "39",  "black"},
	{"36", "36",  "white"},
	{"39", "0",   "white"},
	{"22", "33",  "black"},
	{"38", "36",  "white"},
	{"33", "26",  "white"},
	{"13", "25",  "black"},
	{"19", "14",  "white"},
	{"14", "12",  "white"},
	{"25", "28",  "black"},
	{"8",  "11",  "black"},
	{"6",  "4",   "white"},
	{"22", "15",  "white"},
	{"21", "25",  "black"},
	{"27", "38",  "black"},
	{"23", "19",  "white"},
	{"39", "38",  "white"},
	{"36", "33",  "white"},
	{"25", "28",  "black"},
	{"35", "23",  "white"},
	{"14", "22",  "black"},
	{"26", "27",  "black"},
	{"29", "15",  "white"},
	{"36", "15",  "white"},
	{"24", "32",  "black"},
	{"4",  "34",  "black"},
	{"37", "28",  "white"},
	{"0",  "39",  "black"},
	{"39", "4",   "white"},
	{"15", "13",  "white"}

	 
};

private static double m;
private static double p;
private static int num_attr = 2;
private static int train_size = 44;
private static int num_category = 2;
private static int test_size = 1;
public static  String prediction;

public NaiveBayes() {

 m = 2.0;
 p = 0.5;
 
}

/**
 * Calculates the probability to determine the category the data belongs too
 * @param String [] test, String category 
 * @return p_category
 */
public static double calculateProbability(String[] test, String category) {

int count[] = new int[num_attr];
for (int i=0; i<num_attr; i++)
count[i] = 0;

double p_category = 0.0;
int num_category = 0;

for (int j=0; j<train_size; j++)	
if (category.equals(trainingdata[j][num_attr]))
    	num_category ++;

//System.out.println(category + ": " + num_category);
p_category = (double)num_category/(double)train_size;

for (int i=0; i<num_attr; i++)
{
for (int j=0; j<train_size; j++)	
    if ((test[i].equals(trainingdata[j][i])) && (category.equals(trainingdata[j][num_attr])))
    	count[i] ++;


p_category *=  ((double)count[i] + m * p)/((double)num_category + m);


}

return p_category;

}



/**
 * Calculate the prediction based on the probability previously calculated.
 * @param String[][] test 
 */
public static void prediction(String[][] test)
{
	NaiveBayes nb = new NaiveBayes();
	
	//Array[] input = new Array{friendly, enemy};
	
 double result[] = new double[num_category];
 String category[] = {"white", "black"};
 
 for (int k = 0; k<test_size; k++)
 {
   for (int i=0; i<num_category; i++)
	 {
	   result[i] = calculateProbability(test[k], category[i]);
	 
	 }
 
   double max = -1000.0;
   int max_position = -1;
   for (int i=0; i<num_category; i++)
	 if (result[i]> max)
	 { 
		 max_position = i;
		 max = result[i];
	 
	 }
   
   System.out.println("Naive Bayes predicts \n" + category[max_position] + " will be the\nwinner.");
   
  prediction = category[max_position] + " will  be the winner.";
  
  String newData[] = {Integer.toString(MoveGenerator.friendlyScore),Integer.toString(MoveGenerator.enemyScore)};
  
 }
   
}




 
}