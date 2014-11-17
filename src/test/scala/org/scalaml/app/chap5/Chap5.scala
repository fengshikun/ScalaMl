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
package org.scalaml.app.chap5

import org.scalaml.app.ScalaMlTest


final class Chap5 extends ScalaMlTest {
   val chapter: String = "Chap5"
  	 
   test(s"$chapter Binomial Naive Bayes evaluation") {
  	  evaluate(BinomialBayesEval, Array[String]("0.5", "8"))
  	  
  	  val TRAIN_VALIDATION_RATIO = "0.8"
	  evaluate(BinomialBayesEval, Array[String]("NEM", TRAIN_VALIDATION_RATIO, "4"))
	  evaluate(BinomialBayesEval, Array[String]("NEM", TRAIN_VALIDATION_RATIO, "12"))
	  evaluate(BinomialBayesEval, Array[String]("NEM", TRAIN_VALIDATION_RATIO, "36"))
	  
	  evaluate(BinomialBayesEval, Array[String]("GE", TRAIN_VALIDATION_RATIO, "4"))
	  evaluate(BinomialBayesEval, Array[String]("GE", TRAIN_VALIDATION_RATIO, "12"))
	  evaluate(BinomialBayesEval, Array[String]("GE", TRAIN_VALIDATION_RATIO, "36"))
	  
	  evaluate(BinomialBayesEval, Array[String]("BAC", TRAIN_VALIDATION_RATIO, "4"))
	  evaluate(BinomialBayesEval, Array[String]("BAC", TRAIN_VALIDATION_RATIO, "12"))
	  evaluate(BinomialBayesEval, Array[String]("BAC", TRAIN_VALIDATION_RATIO, "36"))
   }
   
   test(s"$chapter Naive Bayes text analysis evaluation") {
  	   evaluate(TextBayesEval)
   }
   
   test(s"$chapter Naive Bayes and function classification") {
  	   	evaluate(FunctionClassificationEval)
   }
}


// -----------------------------  EOF ------------------------------------