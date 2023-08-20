package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.ArithmeticException
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    private var tvInput : TextView? = null
    private var lastNumeric : Boolean = false
    private var lastDot : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvInput = findViewById(R.id.tvInput_Output)
    }

    fun onDigit(view: View){
        tvInput?.append((view as Button).text)
        lastNumeric=true
        lastDot=false
    }

    fun onClear(view: View){
        tvInput?.text=""
    }

    fun onDecimalPoint(view: View){
        if(lastNumeric && !lastDot){
            var tvValue = tvInput?.text.toString()
            if(tvValue.contains(".")){
                tvInput?.text=tvValue
            }else if(!tvValue.contains(".")){
                tvValue+="."
                tvInput?.text=tvValue
                lastDot=true
                lastNumeric=false
            }

        }
    }



    fun onOperators(view : View){

        tvInput?.text?.let {
            if(lastNumeric && !isOperatorAdded(it.toString())){
                tvInput?.append((view as Button).text)
                lastNumeric = false
                lastDot = false
            }
        }
    }
    fun onNegate(view: View) {
        val tvInputValue = tvInput?.text.toString()
        if (tvInputValue.isNotEmpty()) {

            var result = BigDecimal(tvInputValue)
            result = result.multiply(BigDecimal(-1))
            tvInput?.text = result.toString()
        } else {
            tvInput?.append("")
        }
    }


    fun oneBackClear(view : View){
        if(lastNumeric || lastDot) {
            var result = tvInput?.text.toString()
            result = result.substring(0, result.length - 1)
            tvInput?.text = result
        }
    }



    fun onEqual(view: View) {
        if (lastNumeric) {
            var tvValue = tvInput?.text.toString()
            try {

                // Handling the Starting "-"

                var prefix = false

                if (tvValue.startsWith("-")) {
                    prefix = true
                    tvValue = tvValue.substring(1)
                }

                // Check for percentage "%"
                if (tvValue.contains("%")) {
                    val splitValue = tvValue.split("%")
                    if (splitValue.size == 1) {
                        // If the input is just a single number followed by "%"
                        var numericalValue = splitValue[0].toBigDecimal()
                        if (prefix) {
                            numericalValue = numericalValue.negate()
                        }

                        val result = numericalValue.divide(BigDecimal(100))
                        tvInput?.text = result.toString()
                    } else if (splitValue.size == 2) {
                        // If there are two parts (first number and percentage), handle percentage accordingly
                        val firstNumber = splitValue[0].toBigDecimal()
                        val percentage = splitValue[1].toBigDecimal()
                        val result = firstNumber.multiply(percentage.divide(BigDecimal(100)))
                        tvInput?.text = result.toString()
                    }
                }

                // Check for Subtraction "-"

                else if (tvValue.contains("-")) {
                    val splitValue = tvValue.split("-")


                    var one = BigDecimal(splitValue[0])
                    var two = BigDecimal(splitValue[1])

                    if (prefix) {
                        one = one.multiply(BigDecimal(-1))
                    }

                    // Perform the subtraction operation
                    var result = one.subtract(two)
                    tvInput?.text = result.toString()
                }

                // Check for Addition "+"

                else if (tvValue.contains("+")) {
                    val splitValue = tvValue.split("+")

                    var one: BigDecimal = BigDecimal(splitValue[0])
                    var two: BigDecimal = BigDecimal(splitValue[1])

                    if (prefix) {
                        one = one.multiply(BigDecimal(-1))
                    }

                    var result = one.add(two)
                    tvInput?.text = result.toString()
                }

                // Check for Multiplication "x"

                else if (tvValue.contains("x")) {
                    val splitValue = tvValue.split("x")

                    var one: BigDecimal = BigDecimal(splitValue[0])
                    var two: BigDecimal = BigDecimal(splitValue[1])

                    if (prefix) {
                        one = one.multiply(BigDecimal(-1))
                    }

                    var result = one.multiply(two)
                    tvInput?.text = result.toString()
                }

                // Check for Division "/"
                else if (tvValue.contains("÷")) {
                    val splitValue = tvValue.split("÷")

                    var one: BigDecimal = BigDecimal(splitValue[0])
                    var two: BigDecimal = BigDecimal(splitValue[1])

                    if (prefix) {
                        one = one.multiply(BigDecimal(-1))
                    }

                    var result = one.divide(two, 10, RoundingMode.HALF_UP)
                    tvInput?.text = result.toString()
                }
            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }
        }
    }


    private fun isOperatorAdded(value : String ) :Boolean{
        return if(value.startsWith("-")){
            false
        }else{
            value.contains("-")
                    ||value.contains("+")
                    ||value.contains("x")
                    ||value.contains("÷")
                    ||value.contains("%")
        }

    }
}