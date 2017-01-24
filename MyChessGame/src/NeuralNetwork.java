public class NeuralNetwork {

    private static int CLASS_WHITE = 1;
	private static int CLASS_BLACK = 0;
	public static  String prediction;
	
    
    private static int ourInput[][] = {      
       {22,             26,			CLASS_BLACK},
       {15,             16,			CLASS_BLACK},
       {36,             12,			CLASS_WHITE},
       {0,              39,			CLASS_BLACK},
       {21,             0,			CLASS_WHITE},
       {23,             39,			CLASS_BLACK},
       {3,              9,			CLASS_BLACK},
       {35,             15,			CLASS_WHITE},
       {22,             8,			CLASS_WHITE},
       {17,             29,			CLASS_BLACK},
       {14,             37,			CLASS_BLACK},
       {24,             17,			CLASS_WHITE},
       {17,             32,			CLASS_BLACK},
       {6,              4,			CLASS_WHITE},
       {36,             26,			CLASS_WHITE}
     }; 

    private static int inputPatterns = 15;      
    private static double LEASTMEANSQUAREERROR = 0.001;
    private static double TEACHINGSTEP = 0.01;
    
	/**
	 * Normalizes the data
	 * @param double x
	 * @return a value
	 */
    public static double normalize(double x)  {
    	
    	return 0.00392*x; 
    }
 
    /**
	 * Normalizes the data
	 * @param double n
	 * @return n
	 */
    public static double fabs(double n) {
        if(n >= 0)return n; 
        else return 0 - n; //
        }

   /**
	 * Normalizes the data
	 * @param int friendlyScore, int enemyScore
	 * @return a value
    */
    public static void prediction(int friendlyScore, int enemyScore)
    {
        
        double output;
        double result;
        Perceptron ann = new Perceptron(2, 1); 

        double mse = 999;
        int epochs = 0;
        System.out.println("The network is thinking...");

        while(fabs(mse-LEASTMEANSQUAREERROR)>0.002)
        {
            mse = 0;
            double error = 0;             
            
            for(int j= 0; j < inputPatterns; j++)
            {

                for(int k=0; k<2; k++) 
                  ann.inputAt(k, normalize(ourInput[j][k]));   
                
                ann.inputAt(2, 1);          
                
                output = ann.calculateNet();               
                
                error += fabs(ourInput[j][2]-output); 
                ann.adjustWeights(TEACHINGSTEP, output, ourInput[j][2]);
            }         
            
            if ((epochs == 1) || (epochs % 100) == 0)
              for(int i =0 ; i < 2; i++)
              {              
                
              }
            
            mse = error/(double)inputPatterns; 
            epochs++;

        }        

            result = ann.recall(normalize(friendlyScore),normalize(enemyScore));

            if(result > 0.5)
            {
                System.out.println("The network thinks WHITE CLASS");
            	prediction = "white will  be the winner.";
            }
            else
            {
            	System.out.println("The network thinks BLACK CLASS");
           		prediction = "black will  be the winner.";
            }

        }

    }

