package com.example.flippybirdgamedevlopment
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class GameViewModel : ViewModel() {

    private val _birdFrame = MutableLiveData<Int>()
    private val _birdPosition = MutableLiveData<Pair<Float, Float>>()
    val birdPosition: LiveData<Pair<Float, Float>>
        get() = _birdPosition

    private val _pipeXPosition = MutableLiveData<Float>()
    val pipeXPosition: LiveData<Float>
        get() = _pipeXPosition

    private val _onCollision = MutableLiveData<Boolean>()
    val onCollision: LiveData<Boolean>
        get() = _onCollision

    private val _gameOver = MutableLiveData<Boolean>()
    val gameOver: LiveData<Boolean>
        get() = _gameOver

    private var gameJob: Job? = null
    private var isFlying = false
    private var birdFrameCount = 4
    private var currentBirdFrame = 0

    init {
        _birdFrame.value = 0
        _birdPosition.value = Pair(0f, 0f)
        _pipeXPosition.value = 1000f
        _onCollision.value = false
        _gameOver.value = false
    }

    fun startGameLoop() {
        gameJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                moveBird()
                movePipes()
                animateBird()
                delay(60) // 60 FPS
            }
        }
    }

    private fun moveBird() {
        val currentBirdPosition = _birdPosition.value ?: Pair(0f, 0f)
        val newY = currentBirdPosition.second + if (isFlying) -20f else 10f

        // Ensure the bird stays within the screen boundaries
        if (newY < 0) {
            _birdPosition.value = Pair(currentBirdPosition.first, 0f)
        } else if (newY > Resources.getSystem().displayMetrics.heightPixels) {  // Use screenHeight instead of 1000
            _birdPosition.value = Pair(currentBirdPosition.first,  Resources.getSystem().displayMetrics.heightPixels.toFloat())
        } else {
            _birdPosition.value = Pair(currentBirdPosition.first, newY)
        }

        checkCollision()
    }



    private fun animateBird() {
        // Update the bird frame for animation
        currentBirdFrame = (currentBirdFrame + 1) % birdFrameCount
        _birdFrame.value = currentBirdFrame
    }
    fun onBirdTouch() {
        isFlying = true
    }

    fun onBirdRelease() {
        isFlying = false
    }

    private fun movePipes() {
        _pipeXPosition.value = (_pipeXPosition.value ?: 1000f) - 10f
        if (_pipeXPosition.value!! < -200f) {
            _pipeXPosition.value = 1000f
        }
    }

    private fun checkCollision() {
        val birdPosition = _birdPosition.value ?: Pair(0f, 0f)
        val pipeX = _pipeXPosition.value ?: 0f

        // Check if the bird hits the pipes
        val pipeTopY = 0f
        val pipeBottomY = 1000f

        if ((birdPosition.second < pipeTopY || birdPosition.second > pipeBottomY) && pipeX < 200) {
            _onCollision.value = true
            _gameOver.value = true
        }
    }

    fun resetGame() {
        _birdPosition.value = Pair(0f, 0f)
        _pipeXPosition.value = 1000f
        _onCollision.value = false
        _gameOver.value = false
        isFlying = false
        currentBirdFrame = 0
        _birdFrame.value = 0
    }

    fun stopGameLoop() {
        gameJob?.cancel()
    }

    fun getBirdFrame(): LiveData<Int> {
        return _birdFrame
    }
}
