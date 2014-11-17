/**
 * Copyright 2013, 2014  by Patrick Nicolas - Scala for Machine Learning - All rights reserved
 *
 * The source code in this file is provided by the author for the sole purpose of illustrating the 
 * concepts and algorithms presented in "Scala for Machine Learning" ISBN: 978-1-783355-874-2 Packt Publishing.
 * Unless required by applicable law or agreed to in writing, software is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * Version 0.95e
 */
package org.scalaml.app.chap9


import org.scalaml.supervised.nnet._
import org.scalaml.core.XTSeries
import org.scalaml.core.types.ScalaMl._
import scala.util.{Random, Try, Success, Failure}
import org.apache.log4j.Logger
import org.scalaml.util.Display
import org.scalaml.app.Eval


object MLPConfigEval extends Eval {
   val name: String = "MLPConfigEval"
  	 
   final val ALPHA = 0.9
   final val ETA = 0.1
   final val SIZE_HIDDEN_LAYER = 5
   final val TEST_SIZE = 30
   final val NUM_EPOCHS = 250
   final val NOISE_RATIO = 0.7
   final val EPS = 1e-4
   private val logger = Logger.getLogger(name)
   
   def run(args: Array[String]): Int =  {
     Display.show(s"$name Evaluation of MLP configuration parameters for ${args(0)}", logger)
     
     val noise = () => NOISE_RATIO*Random.nextDouble
     val f1 = (x: Double) => x*(1.0 + noise())
     val f2 = (x: Double) => x*x*(1.0 + noise())

 	 def vec1(x: Double): DblVector = Array[Double](f1(x), noise(), f2(x), noise())
 	 def vec2(x: Double): DblVector = Array[Double](noise(), noise())
 	 
     val x = XTSeries[DblVector](Array.tabulate(TEST_SIZE)(vec1(_)))
     val y = XTSeries[DblVector](Array.tabulate(TEST_SIZE)(vec2(_) ))
     
      		// Normalization._
      val features: XTSeries[DblVector] = XTSeries.normalize(x).get
      val labels = XTSeries.normalize(y).get.toArray
      
      if(args != null && args.size > 0) {
      	 args(0) match {
      		case "alpha" => eval(-1.0, ETA, features, labels)
      		case "eta" =>  eval(ALPHA, -1, features, labels)
      		case _ => eval(-1.0, -1.0, features, labels)
      	 }
      }
      else 
      	 eval(-1.0, -1.0, features, labels)
   }
   
   private def eval(alpha: Double, eta: Double, features: XTSeries[DblVector], labels: DblMatrix): Int = 
  	  _eval(alpha, eta, features, labels)
  	 
   private def _eval(alpha: Double, eta: Double, features: DblMatrix, labels: DblMatrix): Int = {
  	  implicit val mlpObjective = new MLP.MLPBinClassifier
  	         
  	  Try {
        (0.001 until 0.01 by 0.002).foreach( x =>  {
        	val _alpha = if(alpha < 0.0)  x else ALPHA
        	val _eta = if(eta < 0.0) x else ETA
  	        val config = MLPConfig(_alpha, _eta, Array[Int](SIZE_HIDDEN_LAYER), NUM_EPOCHS, EPS)
     
  	        val mlp = MLP[Double](config, features, labels)
	        if( mlp.model == None ) {
	           throw new IllegalStateException(s"$name run failed for eta = $eta and alpha = $alpha") 
	        }
	        else 
	           Display.show(s"$name run for eta = $eta and alpha = $alpha ${mlp.model.get.toString}", logger)
         })
         1
       } match {
         case Success(n) => n
         case Failure(e) => Display.error(s"$name run", logger, e)
       }
   }
}


object MLPConfigEvalApp extends App {
	MLPConfigEval.run(Array[String]("alpha"))
}


// ---------------------------------  EOF --------------------------------------------