package com.example.calc_code

import androidx.core.text.isDigitsOnly
import com.example.calc_code.utilits.AppButtonEnums

class Blocker {
    private val blockedList : ArrayList<String> = arrayListOf();
    private var bracketCounter : Int = 0;
    private var canDotBeUnblocked : Boolean = true;

    private fun blockButton(buttonContent: String){
        if (!blockedList.contains(buttonContent)){
            blockedList.add(buttonContent)
        }
    }
    private fun unblockButton(buttonContent: String){
        if (blockedList.contains(buttonContent)){
            blockedList.remove(buttonContent)
        }
    }

   fun repetativeBlockController(block : String,
                            leftBrListener : Boolean,
                            rightBrListener : Boolean,
                            equListener : Boolean,
                            dotListener : Boolean){
        when (block){
            "nums" -> {
                blockButton("0")
                blockButton("1")
                blockButton("2")
                blockButton("3")
                blockButton("4")
                blockButton("5")
                blockButton("6")
                blockButton("7")
                blockButton("8")
                blockButton("9")
            }
            "ops" -> {
                blockButton("+")
                blockButton("-")
                blockButton("x")
                blockButton("/")
                if (leftBrListener){
                    blockButton("(")
                }
                if (rightBrListener){
                    blockButton(")")
                }
                if (equListener){
                    blockButton("=")
                }
                if (dotListener){
                    blockButton(".")
                }
            }
        }
    }
    fun repetativeUnblockController(block : String,
                                  leftBrListener : Boolean,
                                  rightBrListener : Boolean,
                                  equListener : Boolean,
                                  dotListener : Boolean){
        when (block){
            "nums" -> {
                unblockButton("0")
                unblockButton("1")
                unblockButton("2")
                unblockButton("3")
                unblockButton("4")
                unblockButton("5")
                unblockButton("6")
                unblockButton("7")
                unblockButton("8")
                unblockButton("9")
            }
            "ops" -> {
                unblockButton("+")
                unblockButton("-")
                unblockButton("x")
                unblockButton("/")
                if (leftBrListener){
                    unblockButton("(")
                }
                if (rightBrListener){
                    unblockButton(")")
                }
                if (equListener){
                    unblockButton("=")
                }
                if (dotListener){
                    unblockButton(".")
                }
            }
        }
    }

    fun smartBlocker(viewContent: String) {
        if (viewContent.isDigitsOnly()) {
            blockButton("(")
        } else when (viewContent) {
            "." -> {
                repetativeBlockController("ops", leftBrListener = true, rightBrListener = true, equListener = true, dotListener = true)
            }
            "(" -> {
                repetativeBlockController("ops", leftBrListener = true, rightBrListener = false, equListener = true, dotListener = true)
                unblockButton("-")
                bracketCounter++;
            }
            ")" -> {
                blockButton(".")
                blockButton("(")
                repetativeBlockController("nums", leftBrListener = false, rightBrListener = false, equListener = false, dotListener = false)
                bracketCounter--;
            }
            "x" -> {
                repetativeBlockController("ops", leftBrListener = false, rightBrListener = true, equListener = true, dotListener = true)

            }
            "/" -> {
                repetativeBlockController("ops", leftBrListener = false, rightBrListener = true, equListener = true, dotListener = true)
            }
            "+" -> {
                repetativeBlockController("ops", leftBrListener = false, rightBrListener = true, equListener = true, dotListener = true)
            }
            "-" -> {
                repetativeBlockController("ops", leftBrListener = false, rightBrListener = true, equListener = true, dotListener = true)
            }
        }
        if (bracketCounter <= 0) {
            blockButton(")")
        }
    }

    fun smartUnblocker(viewContent: String) {
        if (viewContent.isDigitsOnly()) {
            repetativeUnblockController("ops", leftBrListener = false, rightBrListener = false, equListener = true, dotListener = false)
            if (canDotBeUnblocked) {
                unblockButton(".")
                canDotBeUnblocked = false
            }
            if (bracketCounter > 0) {
                unblockButton(")")
            }
        } else {
            when (viewContent) {
                "(" -> {
                    unblockButton("-")
                    unblockButton("(")
                }
                ")" -> {
                    repetativeUnblockController("ops", leftBrListener = false, rightBrListener = false, equListener = true, dotListener = false)
                    if (bracketCounter > 0) {
                        unblockButton(")")
                    }
                }
                "+" -> {
                    unblockButton("(")
                    repetativeUnblockController("nums", leftBrListener = false, rightBrListener = false, equListener = false, false)
                    canDotBeUnblocked = true
                }
                "-" -> {
                    unblockButton("(")
                    repetativeUnblockController("nums", leftBrListener = false, rightBrListener = false, equListener = false, false)
                    canDotBeUnblocked = true
                }
                "x" -> {
                    unblockButton("(")
                    repetativeUnblockController("nums", leftBrListener = false, rightBrListener = false, equListener = false, false)
                    canDotBeUnblocked = true
                }
                "/" -> {
                    unblockButton("(")
                    repetativeUnblockController("nums", leftBrListener = false, rightBrListener = false, equListener = false, false)
                    canDotBeUnblocked = true
                }
            }
        }

    }

    fun startInputBlock(){
        blockedList.clear()
        repetativeBlockController("ops", leftBrListener = false, rightBrListener = true, equListener = true, dotListener = true)
        repetativeUnblockController("nums", leftBrListener = false, rightBrListener = false, equListener = false, false)
        unblockButton("(")
        unblockButton("-")
        bracketCounter = 0;
        canDotBeUnblocked = true;
    }

    fun isBlocked(enumValue : AppButtonEnums) : Boolean {
        return blockedList.contains(enumValue.symbol.toString())

    }

    fun symbolicBlocker(symbol: String, symbolDeleted: String) {
        if ((symbolDeleted == "+") or (symbolDeleted == "-")
            or (symbolDeleted == "x") or (symbolDeleted == "/"))
        {
            canDotBeUnblocked = false
        } else when (symbolDeleted) {
            "." -> {
                canDotBeUnblocked = true
            }
            "(" -> {
                bracketCounter--
            }
            ")" -> {
                bracketCounter++
            }
        }
        if (symbol.isDigitsOnly()) {
            smartBlocker("0") //no matter which num button it should be
            smartUnblocker("0") //no matter which num button it should be
        } else when (symbol){
            "+" -> {
                smartBlocker("+")
                smartUnblocker("+")
            }
            "-" -> {
                smartBlocker("-")
                smartUnblocker("-")
            }
            "x" -> {
                smartBlocker("x")
                smartUnblocker("x")
            }
            "/" -> {
                smartBlocker("/")
                smartUnblocker("/")
            }
            "(" -> {
                bracketCounter--;
                smartBlocker("(")
                smartUnblocker("(")
            }
            ")" -> {
                bracketCounter++;
                smartBlocker(")")
                smartUnblocker(")")
            }

        }

    }
}