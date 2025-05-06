package com.kishore.madd

import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var habitInput: EditText
    private lateinit var addHabitButton: Button
    private lateinit var habitListLayout: LinearLayout
    private lateinit var streakCounter: TextView

    private var streak = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        habitInput = findViewById(R.id.habitInput)
        addHabitButton = findViewById(R.id.addHabitButton)
        habitListLayout = findViewById(R.id.habitListLayout)
        streakCounter = findViewById(R.id.streakCounter)

        updateStreak()

        addHabitButton.setOnClickListener {
            val habitText = habitInput.text.toString().trim()
            if (habitText.isNotEmpty()) {
                addHabitItem(habitText)
                habitInput.text.clear()
            } else {
                Toast.makeText(this, "Please enter a habit", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStreak() {
        streakCounter.text = "Streak: $streak completed"
    }

    private fun addHabitItem(habit: String) {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(8, 16, 8, 16)
        }

        val textView = TextView(this).apply {
            text = habit
            textSize = 18f
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val checkBox = CheckBox(this).apply {
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    streak++
                    updateStreak()
                    Toast.makeText(this@MainActivity, "\"$habit\" marked as complete!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val editButton = Button(this).apply {
            text = "✏️"
            setOnClickListener {
                showEditDialog(textView)
            }
        }

        val deleteButton = Button(this).apply {
            text = "❌"
            setOnClickListener {
                habitListLayout.removeView(layout)
                if (checkBox.isChecked) {
                    streak--
                    updateStreak()
                }
            }
        }

        layout.addView(textView)
        layout.addView(checkBox)
        layout.addView(editButton)
        layout.addView(deleteButton)
        habitListLayout.addView(layout)
    }

    private fun showEditDialog(textView: TextView) {
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(textView.text)

        AlertDialog.Builder(this)
            .setTitle("Edit Habit")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                textView.text = input.text.toString()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
