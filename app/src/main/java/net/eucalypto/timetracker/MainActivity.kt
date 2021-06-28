package net.eucalypto.timetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.eucalypto.timetracker.databinding.MainActivityBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
