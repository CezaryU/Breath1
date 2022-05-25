package com.wel.wat.breathalyser

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wel.wat.breathalyser.database.Measurement
import com.wel.wat.breathalyser.database.MeasurementViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var measurementViewModel: MeasurementViewModel

    var sexValue: Double = 0.7
    var weightValue: Double = 0.0
    var alcoholValue: Double = 0.0
    var percentageValue: Double = 0.0
    var millilitersValue: Double = 0.0
    var alcohol: Double = 0.0
    var sober: Double = 0.0
    var unitPerHour: Double = 0.0
    var percentage: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        measurementViewModel = ViewModelProvider(this).get(MeasurementViewModel::class.java)
        val alcoholButton: Button = findViewById(R.id.add_alcohol)
        val measurementButton: Button = findViewById(R.id.new_measurement)
        val infoButton: ImageView = findViewById(R.id.info)

        alcoholButton.setOnClickListener {
            val weightEdit: EditText = findViewById(R.id.weight)
            val mlEdit: EditText = findViewById(R.id.milliliters)
            val percentageEditText: EditText = findViewById(R.id.procentage)
            when {
                weightEdit.text.toString().isEmpty() -> {
                    weightEdit.error = "You need to enter your weight!"
                }
                mlEdit.text.toString().isEmpty() -> {
                    mlEdit.error = "You need to enter amount of alcohol"
                }
                percentageEditText.text.toString().isEmpty() -> {
                    percentageEditText.error = "You need to enter percentages of alcohol"
                }
                else -> {
                    weightValue = weightEdit.text.toString().toDouble()
                    percentageValue = percentageEditText.text.toString().toDouble()
                    millilitersValue = mlEdit.text.toString().toDouble()
                    alcoholValue = (millilitersValue*(percentageValue/100))*0.8
                    addAlcohol()
                    sober()
                    changeColor()
                    insertDataToDatabase()
                    recycleMeasure()
                }
            }

        }

        measurementButton.setOnClickListener {
            newMeasurement()
            deleteAll()
        }

        infoButton.setOnClickListener {
            symptoms()
        }

    }

    private fun deleteAll() {
        measurementViewModel.deleteAllData()
        Toast.makeText(applicationContext,"Starting new measurement",Toast.LENGTH_SHORT).show()
    }

    private fun recycleMeasure() {
        val adapter = ListAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.recycler)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        measurementViewModel.readAllData.observe(this, Observer { measurement ->
            adapter.setData(measurement)
        })
    }

    private fun insertDataToDatabase() {
        val alcoholMl = millilitersValue
        val alcoholPr = percentageValue
        val percent = percentage

        val measurement = Measurement(0, alcoholMl, alcoholPr, percent)
        measurementViewModel.addMeasurement(measurement)
    }

    private fun symptoms() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Common symptoms")
        when {
            percentage<=0.3 -> {
                alert.setMessage("distracted attention")
                alert.show()
            }
            percentage<=0.8 -> {
                alert.setMessage("excitability, impaired eye-hand coordination, decreased criticism ")
                alert.show()
            }
            percentage<=1.5 -> {
                alert.setMessage("balance disorders, errors in logical thinking, delayed reaction time, aggressiveness, bravado ")
                alert.show()
            }
            percentage<=2.0 -> {
                alert.setMessage("speech disorders, drowsiness, decreased control of behavior and movement ")
                alert.show()
            }
            percentage<=4.0 -> {
                alert.setMessage("drowsiness, possibility of falling into a coma")
                alert.show()
            }
            percentage>=4.1 -> {
                alert.setMessage("poisoning, death ")
                alert.show()
            }
        }
    }

    private fun changeColor() {
        val resultText: TextView = findViewById(R.id.result)
        val p: TextView = findViewById(R.id.textView2)

        when {
            percentage<=0.3 -> {
                resultText.setTextColor(Color.parseColor("#30B700"))
                p.setTextColor(Color.parseColor("#30B700"))
            }
            percentage<=0.8 -> {
                resultText.setTextColor(Color.parseColor("#DEC20B"))
                p.setTextColor(Color.parseColor("#DEC20B"))
            }
            percentage<=1.5 -> {
                resultText.setTextColor(Color.parseColor("#FFC600"))
                p.setTextColor(Color.parseColor("#FFC600"))
            }
            percentage<=2.0 -> {
                resultText.setTextColor(Color.parseColor("#F6BE00"))
                p.setTextColor(Color.parseColor("#F6BE00"))
            }
            percentage<=3.0 -> {
                resultText.setTextColor(Color.parseColor("#CB6015"))
                p.setTextColor(Color.parseColor("#CB6015"))
            }
            percentage<=4.0 -> {
                resultText.setTextColor(Color.parseColor("#FA4616"))
                p.setTextColor(Color.parseColor("#FA4616"))
            }
            percentage>=4.1 -> {
                resultText.setTextColor(Color.parseColor("#000000"))
                p.setTextColor(Color.parseColor("#000000"))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun newMeasurement() {
        val resultText: TextView = findViewById(R.id.result)
        val soberText: TextView = findViewById(R.id.sober)
        this.alcohol = 0.0
        resultText.text = "0.00"
        soberText.text = ""
    }

    @SuppressLint("SetTextI18n")
    private fun addAlcohol() {
        val resultText: TextView = findViewById(R.id.result)
        this.alcohol = this.alcohol+alcoholValue
        percentage = this.alcohol/(sexValue*weightValue)
        resultText.text = "$percentage"
    }

    @SuppressLint("SetTextI18n")
    private fun sober(){
        val soberText: TextView = findViewById(R.id.sober)
        if(sexValue==0.6) {
            unitPerHour = 8 / (sexValue * weightValue)
        }
            else {
            unitPerHour = 10 / (sexValue * weightValue)
        }
        sober = percentage/unitPerHour
        val sober2precision = Math.round(sober * 100.0) / 100.0
        soberText.text = "You are going to be sober in $sober2precision hours"
//        val timer = object: CountDownTimer(percentage.toLong(), unitPerHour.toLong()) {
//            override fun onTick(millisUntilFinished: Long) {
//                soberText.text = "You are going to be sober in $sober hours"
//            }
//
//            override fun onFinish() {
//                soberText.text = "You are sober!!"
//
//            }
//        }
//        timer.start()
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.getId()) {
                R.id.Male ->
                    if (checked) {
                        sexValue = 0.7
                    }
                R.id.Female ->
                    if (checked) {
                        sexValue = 0.6
                    }
            }
        }

}
}