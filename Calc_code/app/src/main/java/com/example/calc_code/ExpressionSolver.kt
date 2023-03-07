package com.example.calc_code

import kotlin.math.nextDown
import kotlin.math.nextTowards

class ExpressionSolver {
    private var inputStr : String = ""

    private fun setInputString (input: String) {
        inputStr = input
    }
    private fun unaryMinusController(){
        var stringBuilder = StringBuilder(inputStr)
        for(charInd in inputStr.indices){
            if (charInd == 0){
                if (inputStr[charInd].toString() == "-"){
                    inputStr = "~" + inputStr.substring(1)
                }
            }
            else if ((inputStr[charInd].toString() == "-") &&
                (inputStr[charInd-1].toString() == "(")){
                inputStr = inputStr.substring(0, charInd) + "~" + inputStr.substring(charInd + 1)
            }
        }
    }
    fun solveExpression(inputExpression: String): Double { // string- временно, пока тестирую и нет вычислений
        setInputString(inputExpression)
        /*var PRNexpression: String = makePRN()
        return calculate(PRNexpression)*/
        unaryMinusController()
        return calculate(makePRN())
        //return  makePRN()
    }

    private fun makePRN() : String{
        var output = ""
        val operStack: ArrayDeque<String> = ArrayDeque()
        var flagOnDigit : Boolean = false
        var tempChar = ""
        for (charIndex in inputStr.indices){
            tempChar = inputStr[charIndex].toString()
            if (isDigitOrDot(tempChar)){
                if (!flagOnDigit){
                    flagOnDigit = true
                }
                output += tempChar
            }
            if (isOperator(tempChar)){
                if (flagOnDigit){
                    flagOnDigit = false
                    output += " "
                }
                if (operStack.isEmpty()) {
                    operStack.addFirst(tempChar)
                } else when (tempChar){
                    "(" -> {
                        operStack.addFirst(tempChar)
                    }
                    ")" -> {
                        while (operStack.first() != "("){
                            output += operStack.first()
                            output += " "
                            operStack.removeFirst()
                        }
                        operStack.removeFirst()
                    }
                    else -> {
                        while ((operStack.isNotEmpty())&&(getPriority(tempChar) <= getPriority(operStack.first()))){
                            output += operStack.first()
                            output += " "
                            operStack.removeFirst()
                        }
                        operStack.addFirst(tempChar)
                    }
                }
            }
        }
        while (!operStack.isEmpty()){
            output += " "
            output += operStack.first()
            output += " "
            operStack.removeFirst()
        }
        return output
    }

    private fun calculate(input: String) : Double{
        val digitalStack: ArrayDeque<Double> = ArrayDeque()
        var flagOnDigit : Boolean = false
        var tempChar = " "
        var tempDoubleLast = 0.0
        var tempStringConvert = ""
        var tempDoubleFirst = 0.0
        fun operationExecutor (operation: String) {
            tempDoubleLast = digitalStack.first()
            digitalStack.removeFirst()
            if (operation != "~") {
                tempDoubleFirst = digitalStack.first()
                digitalStack.removeFirst()
            }
            when (operation) {
                "+" -> {
                    tempDoubleFirst += tempDoubleLast
                }
                "-" -> {
                    tempDoubleFirst -= tempDoubleLast
                }
                "x" -> {
                    tempDoubleFirst *= tempDoubleLast
                }
                "/" -> {
                    tempDoubleFirst /= tempDoubleLast
                }
                "~" -> {
                    tempDoubleFirst = tempDoubleLast
                    tempDoubleFirst *= -1
                }
            }
            digitalStack.addFirst(tempDoubleFirst)
        }
        for (charInd in input.indices){
            tempChar = input[charInd].toString()
            if (!isSplitter(tempChar)){
                if (isDigitOrDot(tempChar)){
                    if (!flagOnDigit){
                        flagOnDigit = true
                    }
                    tempStringConvert += tempChar
                } else {
                    operationExecutor(tempChar)
                }
            } else {
                if (flagOnDigit){
                    digitalStack.addFirst(tempStringConvert.toDouble())
                    tempStringConvert = ""
                    flagOnDigit = false
                }
            }
        }
        return digitalStack.first()
    }

    private fun isSplitter(inputChar: String) : Boolean{
        return (inputChar == " ")
    }

    private fun isOperator(inputChar: String): Boolean {
        return inputChar.contains("[+x/()~-]".toRegex())
    }

    private fun isDigitOrDot(inputChar: String) : Boolean{
        return inputChar.contains("[1234567890.]".toRegex())
    }

    private fun getPriority(inputChar: String) : Int{
        when (inputChar){
            "(" -> {
                return -1
            }
            ")" -> {
                return 0
            }
            "+" -> {
                return 1
            }
            "-" -> {
                return 1
            }
            "x" -> {
                return 2
            }
            "/" -> {
                return 2
            }
            "~" -> {
                return 3
            }
            else -> {
                return -100
            }
        }
    }
}