package com.example.flippybirdgamedevlopment
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.flippybirdgamedevlopment.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnTouchListener {

    private lateinit var gameViewModel: GameViewModel
    private lateinit var birdImageView: ImageView
    private lateinit var pipeTopImageView: ImageView
    private lateinit var pipeBottomImageView: ImageView
    private var gameJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        birdImageView = binding.birdImageView
        pipeTopImageView = binding.pipeTopImageView
        pipeBottomImageView = binding.pipeBottomImageView

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        binding.root.setOnTouchListener(this)

        observeViewModel()
        startGameLoop()
    }

    private fun observeViewModel() {
        observeBirdFrame()
        gameViewModel.birdPosition.observe(this, Observer { birdPosition ->
            birdImageView.translationY = birdPosition.second
        })

        gameViewModel.pipeXPosition.observe(this, Observer { pipeX ->
            pipeTopImageView.translationX = pipeX
            pipeBottomImageView.translationX = pipeX
        })

        gameViewModel.onCollision.observe(this, Observer {
            if (it) {
                gameViewModel.stopGameLoop()
                showGameOverToast()
            }
        })
    }

    private fun showGameOverToast() {
        Toast.makeText(this, "Game Over! You lose.", Toast.LENGTH_SHORT).show()
    }

    private fun startGameLoop() {
        gameJob = GlobalScope.launch(Dispatchers.Main) {
            gameViewModel.startGameLoop()
        }
    }

    private fun observeBirdFrame() {
        gameViewModel.getBirdFrame().observe(this, Observer { frame ->
            // Assuming you have four image frames (0 to 3)
            val resourceId = when (frame) {
                0 -> R.drawable.bird_frame_0
                1 -> R.drawable.bird_frame_1
                2 -> R.drawable.bird_frame_2
                3 -> R.drawable.bird_frame_3
                else -> R.drawable.bird_frame_0
            }
            birdImageView.setImageResource(resourceId)
        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                gameViewModel.onBirdTouch()
            }
            MotionEvent.ACTION_UP -> {
                gameViewModel.onBirdRelease()
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        gameViewModel.stopGameLoop()
        gameJob?.cancel()
    }
}
