package com.example.calc_code

class Cheker {
    private var testableString : String = "";
    var errorCode : Int = 0;
    var bracketCounter : Int = 0;

    fun setTestableStr(String: String){
        testableString = String
        errorCode = 0;
        bracketCounter = 0;
    }
    fun fullCheck() : Int {
        val notEnoughBrackets : Boolean = notEnoughBrackets()
        val unnecessaryBrackets: Boolean = unnecessaryBracketsControl(false)
        if (notEnoughBrackets || unnecessaryBrackets){
            if (notEnoughBrackets){
                errorCode = 1;
            }
            if (unnecessaryBrackets){
                errorCode = 2;
            }
        }else{
            errorCode = 0
        }
        return  errorCode
    }

    private fun notEnoughBrackets() : Boolean{
        for (char in testableString.toCharArray()){
            if (char.toString() == "("){
                bracketCounter++
            }
            if (char.toString() == ")"){
                bracketCounter--
            }
        }
        return bracketCounter != 0
    }

    private fun unnecessaryBracketsControl(toFix : Boolean) : Boolean {
        var bracketCheck: Boolean = false;
        var bracketsPositions : IntArray = IntArray(2)
        var errorCheck: Boolean = false;
        var stringBuilder: StringBuilder
        stringBuilder = StringBuilder(testableString)
        bracketsPositions[0] = 0;
        bracketsPositions[1] = 0;
        do {
            errorCheck = false;
            bracketCheck = false;
            for (char in testableString.indices){
                when (testableString[char].toString()){
                    "(" -> {
                        if (!bracketCheck){
                            bracketCheck = true;
                        }
                        if (toFix) {
                            if(!errorCheck){
                                bracketsPositions[0] = char
                            }
                        }
                    }
                    "-", "+", "x", "/" -> {
                        bracketCheck = false;
                    }
                    ")" -> {
                        if (toFix) {
                            if(!errorCheck){
                                bracketsPositions[1] = char
                            }
                        }
                        if (bracketCheck){
                            errorCheck = true;
                            if (!toFix) {
                                return true
                            }
                        }
                    }
                }
            }
            if (errorCheck) {
                stringBuilder = stringBuilder.deleteAt(bracketsPositions[0])
                bracketCounter--;
                stringBuilder = stringBuilder.deleteAt(bracketsPositions[1] - 1)
                bracketCounter++;
                testableString = stringBuilder.toString()
            }
        } while (errorCheck)
        return false
    }

    fun fixInputString() : String {
        when (errorCode) {
            1 -> {
                when {
                    (bracketCounter > 0) -> {
                        while (bracketCounter > 0) {
                            testableString = testableString.plus(")")
                            bracketCounter--
                        }
                    }
                }
            }
            2 -> unnecessaryBracketsControl(true)
        }
        return testableString
    }
}