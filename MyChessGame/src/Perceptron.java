import java.util.*;

public class Perceptron {

	    private int inputNumer = 2;
	    private double[] inputLayer = new double[inputNumer+1]; 
        public double[] weights = new double[inputNumer+1];
        private int activationFunction;
        
        
   /**
   * Creates the perceptron
   * @param int inputNumber, int function
   */
  public Perceptron(int inputNumber, int function)
  {

    Random rand = new Random();

    for(int i = 0; i < inputNumber+1; i++) 
        weights[i] = (( (double)rand.nextInt(10) / ((double)(10)+(double)(1)) )) - 0.5; 

    activationFunction = function;

  }

  /**
   * Creates the perceptron
   * @param int inputPos, double d
   */
  public void inputAt(int inputPos,double d)
  {
    inputLayer[inputPos] = d;
  }

  /**
   * calculates the net
   * @return action
   */
  public double calculateNet()
  {
    double action = 0.0;

    for(int i =0; i < this.inputNumer + 1; i ++)
    {
        action += inputLayer[i] * weights[i];
    }

    switch(activationFunction)
    {
        case 0: 
            if(action >= 0)
                action = 1;
            else
                action = 0;
            break;
        case 1: 
            action = 1.0/(1.0 + Math.exp(-action));
            break;
        case 2: 
            action = (Math.exp(2*action)-1)/(Math.exp(2*action)+1);
            break;
    }

    return action;
   }

  /**
   * Adjust the weights accoriding to the teacher
   * @param double tEACHINGSTEP, double output, double target
   */
   public void adjustWeights(double tEACHINGSTEP, double output, double target)
   {    
    for(int i =0 ; i < this.inputNumer + 1; i++)
      {
        weights[i] = weights[i] + tEACHINGSTEP*(target-output)* inputLayer[i];
      }
   }

   /**
    * Recalls the the inputs to the system and claculates the net wiht those inputs.
    * @param double d, double e
    * @return calculateNet()
    */
   public double recall(double d, double e)
   {
     inputLayer[0] = d;
     inputLayer[1] = e;
     inputLayer[2] = 1.0;

     return calculateNet();
   }
}
