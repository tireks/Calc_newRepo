package com.example.calc_code
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.example.calc_code.databinding.ActivityMainBinding
import com.example.calc_code.databinding.PopupWindowBinding
import com.example.calc_code.utilits.AppButtonEnums
import com.example.calc_code.utilits.resultController


class MainActivity : AppCompatActivity(),  View.OnClickListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val bindingPop by lazy { PopupWindowBinding.inflate(layoutInflater) }
    private var blocker: Blocker = Blocker()
    private var cheker: Cheker = Cheker()
    private var calculator : ExpressionSolver = ExpressionSolver()
    private val normalButtons = mutableMapOf<AppButtonEnums, Button>()
    //private var operationButtons =

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //blocker.startInputBlock()  todo dont forget to turn dat on
        binding.equButton.setOnClickListener(this) /// todo fix it
        //private val numberButtons = mapOf<AppButtonEnums, ActivityMainBinding>()
        mapButtonsSetter(normalButtons, binding)
        normalButtons.forEach { (appButtonEnums, button) ->
            button.setOnClickListener {
                addSymbol(appButtonEnums)
            }
        }

    }

    private fun mapButtonsSetter(normalButtons: MutableMap<AppButtonEnums, Button>, binding: ActivityMainBinding) {
        normalButtons[AppButtonEnums.B0] = binding.Button0
        normalButtons[AppButtonEnums.B1] = binding.Button1
        normalButtons[AppButtonEnums.B2] = binding.Button2
        normalButtons[AppButtonEnums.B3] = binding.Button3
        normalButtons[AppButtonEnums.B4] = binding.Button4
        normalButtons[AppButtonEnums.B5] = binding.Button5
        normalButtons[AppButtonEnums.B6] = binding.Button6
        normalButtons[AppButtonEnums.B7] = binding.Button7
        normalButtons[AppButtonEnums.B8] = binding.Button8
        normalButtons[AppButtonEnums.B9] = binding.Button9
        normalButtons[AppButtonEnums.Bdot] = binding.dotButton
        normalButtons[AppButtonEnums.Bplus] = binding.plusButton
        normalButtons[AppButtonEnums.Bmultyply] = binding.multiplyButton
        normalButtons[AppButtonEnums.Bminus] = binding.minusButton
        normalButtons[AppButtonEnums.Bdivide] = binding.divideButton
        normalButtons[AppButtonEnums.BleftBracket] = binding.bracketLeftButton
        normalButtons[AppButtonEnums.BrightBracket] = binding.bracketRightButton
    }

    /*fun normalButtonOnClick(view: View){
        if (!blocker.isBlocked(view) && view is Button) {
            blocker.smartBlocker(view.text.toString())
            blocker.smartUnblocker(view.text.toString())
            binding.TVInput.append(view.text)
        }

    }*/

    fun addSymbol(enumValue: AppButtonEnums){
        if (!blocker.isBlocked(enumValue)) {
            /*blocker.smartBlocker(view.text.toString())
            blocker.smartUnblocker(view.text.toString())*/ // todo its temporary turned off, only for testing
            binding.TVInput.append(enumValue.symbol.toString())
        }

    }



    fun acButtonOnClick(view: View){
        if (view is Button){
            binding.TVInput.text = ""
            binding.TVResult.text = ""
        }
        blocker.startInputBlock()
    }
    fun delButtonOnClick(view: View){
        if (binding.TVInput.text.length > 1) {
            var length : Int = binding.TVInput.length()
            var delSymbol : String = binding.TVInput.text.substring(length - 1, length)
            if (view is ImageButton){
                binding.TVInput.text = binding.TVInput.text.substring(0,length - 1)
            }
            length = binding.TVInput.length()
            //val newLastSymbol : String =
            blocker.symbolicBlocker(binding.TVInput.text.substring(length - 1, length), delSymbol)
        } else {
            acButtonOnClick(binding.acButton)
        }
        binding.TVResult.text = ""
    }

    private fun PopupWindow.dimBehind() {
        val container = contentView.rootView
        val context = contentView.context
        val wm = context.getSystemService(WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.9f
        wm.updateViewLayout(container, p)
    }

    private fun showPopUp(warnString: String, view: View, errorCode : Int) : PopupWindow{
        bindingPop.myTV.text = warnString
        val wid = LinearLayout.LayoutParams.WRAP_CONTENT
        val high = LinearLayout.LayoutParams.WRAP_CONTENT
        val focus = true
        val popupWindow = PopupWindow(bindingPop.root, wid, high, false)
        popupWindow.showAtLocation(view, Gravity.CENTER, 0,0)
        popupWindow.dimBehind()
        popupWindow.isOutsideTouchable = false
        popupWindow.isTouchable = true
        bindingPop.OkButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                popupWindow.dismiss()
            }
        })
        bindingPop.FixButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                binding.TVInput.text = cheker.fixInputString()
                //binding.TVInput.text = "123"
                popupWindow.dismiss()
            }
        })
        return popupWindow
    }

    override fun onClick(view: View) {
        var popupWindow : PopupWindow = PopupWindow()
        /*if (!blocker.isBlocked(view)){
            cheker.setTestableStr(binding.TVInput.text.toString())
            when (val errorCode = cheker.fullCheck()){
                0 -> {
                    //binding.TVResult.text = calculator.solveExpression(binding.TVInput.text.toString()).toString()
                    binding.TVResult.text = resultController(calculator.solveExpression(binding.TVInput.text.toString()))
                    //binding.TVResult.text = calculator.solveExpression(binding.TVInput.text.toString())
                }
                1,2 -> {
                    var warnString = ""
                    warnString = if (errorCode == 1){
                        "sorry, the quantity '(' and ')' doesn't seem to match"
                    }else {
                        "sorry, it seems the expression contains unnecessary brackets"
                    }
                    popupWindow = showPopUp(warnString, view, errorCode)
                }
            }

        }*/
    }

}
//test change
