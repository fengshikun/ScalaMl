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
package org.scalaml.app.chap8

import org.scalaml.supervised.svm.{SVMConfig, SVMExecution, SVM}
import org.scalaml.supervised.svm.formulation.CSVCFormulation
import org.scalaml.supervised.svm.kernel.RbfKernel
import org.scalaml.core.XTSeries
import org.scalaml.core.types.ScalaMl._
import XTSeries._
import org.scalaml.plots.ScatterPlot
import org.scalaml.plots.BlackPlotTheme
import XTSeries._
import org.scalaml.util.Display
import org.apache.log4j.Logger
import org.scalaml.app.Eval




object SVCMarginEval extends Eval {
	import scala.util.Random
	val name: String = "SVCMarginEval"
	
    final val GAMMA = 0.8; val N = 100
    var status: Int = 0
    private val logger = Logger.getLogger(name)
    
    	/**
    	 * Main evaluation routine that consists of two steps:<br>
    	 * Generation of synthetic features<br>
    	 * Computation of the margin for a specific C penalty value
    	 */
	def run(args: Array[String]): Int = {   
        Display.show("SVCMarginEval.run: Evaluation of impact of C penalty on margin of a binary support vector classifier", logger)
    	val values = generate
		Range(0, 50).foreach(i => evalMargin(values._1, values._2, i*0.1))
		status
	}
    
    private def generate: (DblMatrix, DblVector) = {
      val z  = Array.tabulate(N)(i =>{
	    val ri = i*(1.0 + 0.2*Random.nextDouble)	
	    Array[Double](i, ri)
	  }) ++
	  Array.tabulate(N)(i => Array[Double](i, i*Random.nextDouble))
      (z, Array.fill(N)(1) ++ Array.fill(N)(-1))
    }

	private def evalMargin(features: DblMatrix, lbl: DblVector, c: Double): Int = {
		val config = SVMConfig(new CSVCFormulation(c), RbfKernel(GAMMA))
		val svc = SVM[Double](config, XTSeries[DblVector](features), lbl)
		
		svc.margin match {
			case Some(mrgn) => Display.show(s"\nSVCMarginEval: SVRFormulation Margin for C=$c: $mrgn", logger)
			case None => {status = -1; Display.error("SVCMarginEval: SVRFormulation.evalMaring. training failed", logger) }
		}
	}
}


object SVCMarginEvalApp extends App {
	SVCMarginEval.run(Array.empty)
}


// --------------------------- EOF --------------------------------------------------