package com.example.calc_code
import android.graphics.Color
//import com.tirex_projs.Calc_new.databinding.ActivityMainBinding;
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.text.isDigitsOnly
import com.example.calc_code.utilits.AppButtonEnums

class Blocker {
    private val blockedList : ArrayList<String> = arrayListOf();
    private var bracketCounter : Int = 0;
    private var canDotBeUnblocked : Boolean = true;
    //var buttonSet = ButtonSet

    private fun viewConverter(view: View){

    }
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

    fun smartBlocker(viewContent: String) {
        if (viewContent.isDigitsOnly()) {
            blockButton("(")
        } else when (viewContent) {
            "." -> {
                blockButton("(")
                blockButton(")")
                blockButton("+")
                blockButton("-")
                blockButton("x")
                blockButton("/")
                blockButton("=")
                blockButton(".")
            }
            "(" -> {
                blockButton("+")
                blockButton("x")
                blockButton("/")
                blockButton("=")
                blockButton(".")
                blockButton("(")
                bracketCounter++;
            }
            ")" -> {
                blockButton(".")
                blockButton("(")
                blockNumsButtons()
                bracketCounter--;
            }
            "x" -> {
                blockButton(".")
                blockButton("=")
                blockButton("+")
                blockButton("-")
                blockButton("/")
                blockButton("x")
                blockButton(")")

            }
            "/" -> {
                blockButton(".")
                blockButton("=")
                blockButton("+")
                blockButton("-")
                blockButton("X")
                blockButton("/")
                blockButton(")")
            }
            "+" -> {
                blockButton(".")
                blockButton("=")
                blockButton("-")
                blockButton("x")
                blockButton("/")
                blockButton("+")
                blockButton(")")
            }
            "-" -> {
                blockButton(".")
                blockButton("=")
                blockButton("+")
                blockButton("x")
                blockButton("/")
                blockButton("-")
                blockButton(")")
            }
        }
        if (bracketCounter <= 0) {
            blockButton(")")
        }
    }

    fun smartUnblocker(viewContent: String) {
        if (viewContent.isDigitsOnly()) {
            unblockButton("=")
            unblockButton("/")
            unblockButton("x")
            unblockButton("+")
            unblockButton("-")
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
                    unblockButton("/")
                    unblockButton("x")
                    unblockButton("+")
                    unblockButton("-")
                    unblockButton("=")
                    if (bracketCounter > 0) {
                        unblockButton(")")
                    }
                }
                "+" -> {
                    unblockButton("(")
                    unblockNumsButtons()
                    canDotBeUnblocked = true
                }
                "-" -> {
                    unblockButton("(")
                    unblockNumsButtons()
                    canDotBeUnblocked = true
                }
                "x" -> {
                    unblockButton("(")
                    unblockNumsButtons()
                    canDotBeUnblocked = true
                }
                "/" -> {
                    unblockButton("(")
                    unblockNumsButtons()
                    canDotBeUnblocked = true
                }
            }
        }

    }

    fun startInputBlock(){
        blockedList.clear()
        blockButton(".")
        blockButton("=")
        blockButton("+")
        blockButton("x")
        blockButton("/")
        blockButton(")")
        unblockNumsButtons()
        unblockButton("(")
        bracketCounter = 0;
        canDotBeUnblocked = true;
    }

    fun isBlocked(enumValue : AppButtonEnums) : Boolean {
        return blockedList.contains(enumValue.symbol.toString())

    }

    private fun blockNumsButtons(){
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

    private fun unblockNumsButtons(){
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