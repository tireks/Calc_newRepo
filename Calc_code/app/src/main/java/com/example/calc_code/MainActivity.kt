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


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val bindingPop by lazy { PopupWindowBinding.inflate(layoutInflater) }
    private var blocker: Blocker = Blocker()
    private var cheker: Cheker = Cheker()
    private var calculator : ExpressionSolver = ExpressionSolver()
    private val normalButtons = mutableMapOf<AppButtonEnums, Button>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        blocker.startInputBlock()
        binding.equButton.setOnClickListener{
            equButtonHandler(AppButtonEnums.Bequ)
        }
        binding.delButton.setOnClickListener {
            delButtonHandler()
        }
        binding.acButton.setOnClickListener {
            acButtonHandler()
        }
        mapButtonsSetter(normalButtons, binding)
        normalButtons.forEach { (appButtonEnums, button) ->
            button.setOnClickListener {
                addSymbol(appButtonEnums)
            }
        }

    }

    private fun acButtonHandler() {
        binding.TVInput.text = ""
        binding.TVResult.text = ""
        blocker.startInputBlock()
    }

    private fun delButtonHandler() {
        if (binding.TVInput.text.length > 1) {
            var length : Int = binding.TVInput.length()
            var delSymbol : String = binding.TVInput.text.substring(length - 1, length)
            binding.TVInput.text = binding.TVInput.text.substring(0,length - 1)
            length = binding.TVInput.length()
            blocker.symbolicBlocker(binding.TVInput.text.substring(length - 1, length), delSymbol)
        } else {
            acButtonHandler()
        }
        binding.TVResult.text = ""
    }

    private fun equButtonHandler(enumValue: AppButtonEnums) {
        var popupWindow : PopupWindow = PopupWindow()
        if (!blocker.isBlocked(enumValue)){
            cheker.setTestableStr(binding.TVInput.text.toString())
            when (val errorCode = cheker.fullCheck()){
                0 -> {
                    binding.TVResult.text = resultController(calculator.solveExpression(binding.TVInput.text.toString()))
                }
                1,2 -> {
                    var warnString = ""
                    warnString = if (errorCode == 1){
                        "sorry, the quantity '(' and ')' doesn't seem to match"
                    }else {
                        "sorry, it seems the expression contains unnecessary brackets"
                    }
                    popupWindow = showPopUp(warnString, errorCode)
                }
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

    fun addSymbol(enumValue: AppButtonEnums){
        if (!blocker.isBlocked(enumValue)) {
            blocker.smartBlocker(enumValue.symbol.toString())
            blocker.smartUnblocker(enumValue.symbol.toString())
            binding.TVInput.append(enumValue.symbol.toString())
        }

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

    private fun showPopUp(warnString: String, errorCode : Int) : PopupWindow{
        bindingPop.myTV.text = warnString
        val wid = LinearLayout.LayoutParams.WRAP_CONTENT
        val high = LinearLayout.LayoutParams.WRAP_CONTENT
        val focus = true
        val popupWindow = PopupWindow(bindingPop.root, wid, high, false)
        popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0,0)
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
                popupWindow.dismiss()
            }
        })
        return popupWindow
    }

}
