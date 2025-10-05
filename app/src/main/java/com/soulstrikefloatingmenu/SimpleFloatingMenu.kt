package com.soulstrikefloatingmenu

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import de.robv.android.xposed.XposedBridge

class SimpleFloatingMenu(private val context: Context) {
    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var isExpanded = false
    
    // Menu state variables
    private var godMode = false
    private var infiniteAmmo = false
    private var autoAim = false
    private var showFPS = true
    private var playerSpeed = 1.0f
    private var menuAlpha = 0.8f

    fun show() {
        try {
            // Check if we have the required permission
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (!android.provider.Settings.canDrawOverlays(context)) {
                    XposedBridge.log("SoulStrikeFloatingMenu: No overlay permission - cannot show floating window")
                    XposedBridge.log("SoulStrikeFloatingMenu: Please grant 'Display over other apps' permission in Settings")
                    // Try to show anyway - sometimes the permission check is wrong
                }
            }
            
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            
            val inflater = LayoutInflater.from(context)
            floatingView = createFloatingView(inflater)
            
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            )
            
            params.gravity = Gravity.TOP or Gravity.START
            params.x = 100
            params.y = 100
            
            // Ensure we're on the UI thread
            if (context is android.app.Activity) {
                (context as android.app.Activity).runOnUiThread {
                    try {
                        windowManager?.addView(floatingView, params)
                        XposedBridge.log("SoulStrikeFloatingMenu: Floating window shown successfully")
                    } catch (e: Exception) {
                        XposedBridge.log("SoulStrikeFloatingMenu: Error adding view: ${e.message}")
                        e.printStackTrace()
                    }
                }
            } else {
                // For non-Activity contexts, use Handler
                android.os.Handler(android.os.Looper.getMainLooper()).post {
                    try {
                        windowManager?.addView(floatingView, params)
                        XposedBridge.log("SoulStrikeFloatingMenu: Floating window shown successfully")
                    } catch (e: Exception) {
                        XposedBridge.log("SoulStrikeFloatingMenu: Error adding view: ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            XposedBridge.log("SoulStrikeFloatingMenu: Error in show(): ${e.message}")
            e.printStackTrace()
        }
    }
    
    fun hide() {
        try {
            if (floatingView != null && windowManager != null) {
                windowManager?.removeView(floatingView)
                floatingView = null
                XposedBridge.log("SoulStrikeFloatingMenu: Floating window hidden")
            }
        } catch (e: Exception) {
            XposedBridge.log("SoulStrikeFloatingMenu: Error hiding window: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun createFloatingView(@Suppress("UNUSED_PARAMETER") inflater: LayoutInflater): View {
        XposedBridge.log("SoulStrikeFloatingMenu: Creating floating view")
        
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(0x80000000.toInt()) // Semi-transparent black
            setPadding(32, 32, 32, 32)
        }

        // Title
        val title = TextView(context).apply {
            text = "Soul Strike Menu"
            textSize = 18f
            setTextColor(0xFFFFFFFF.toInt())
            gravity = Gravity.CENTER
        }
        layout.addView(title)

        // Collapsed view (just a button)
        val collapsedView = Button(context).apply {
            text = "⚙️ Menu"
            setBackgroundColor(0x80000000.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            setOnClickListener { expandMenu() }
        }

        // Expanded view (full menu)
        val expandedView = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            visibility = View.GONE
        }

        // God Mode toggle
        val godModeCheckBox = CheckBox(context).apply {
            text = "God Mode"
            setTextColor(0xFFFFFFFF.toInt())
            isChecked = godMode
            setOnCheckedChangeListener { _, isChecked ->
                godMode = isChecked
                if (isChecked) applyFeature("God Mode")
            }
        }
        expandedView.addView(godModeCheckBox)

        // Infinite Ammo toggle
        val infiniteAmmoCheckBox = CheckBox(context).apply {
            text = "Infinite Ammo"
            setTextColor(0xFFFFFFFF.toInt())
            isChecked = infiniteAmmo
            setOnCheckedChangeListener { _, isChecked ->
                infiniteAmmo = isChecked
                if (isChecked) applyFeature("Infinite Ammo")
            }
        }
        expandedView.addView(infiniteAmmoCheckBox)

        // Auto Aim toggle
        val autoAimCheckBox = CheckBox(context).apply {
            text = "Auto Aim"
            setTextColor(0xFFFFFFFF.toInt())
            isChecked = autoAim
            setOnCheckedChangeListener { _, isChecked ->
                autoAim = isChecked
                if (isChecked) applyFeature("Auto Aim")
            }
        }
        expandedView.addView(autoAimCheckBox)

        // Show FPS toggle
        val showFPSCheckBox = CheckBox(context).apply {
            text = "Show FPS"
            setTextColor(0xFFFFFFFF.toInt())
            isChecked = showFPS
            setOnCheckedChangeListener { _, isChecked ->
                showFPS = isChecked
                if (isChecked) applyFeature("Show FPS")
            }
        }
        expandedView.addView(showFPSCheckBox)

        // Speed slider
        val speedLabel = TextView(context).apply {
            text = "Player Speed: ${playerSpeed.toInt()}x"
            setTextColor(0xFFFFFFFF.toInt())
        }
        expandedView.addView(speedLabel)

        val speedSeekBar = SeekBar(context).apply {
            max = 300 // 0.5x to 3.0x speed
            progress = ((playerSpeed - 0.5f) * 100).toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    playerSpeed = (progress / 100f) + 0.5f
                    speedLabel.text = "Player Speed: ${playerSpeed.toInt()}x"
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    applyFeature("Speed: ${playerSpeed.toInt()}x")
                }
            })
        }
        expandedView.addView(speedSeekBar)

        // Menu Alpha slider
        val alphaLabel = TextView(context).apply {
            text = "Menu Alpha: ${(menuAlpha * 100).toInt()}%"
            setTextColor(0xFFFFFFFF.toInt())
        }
        expandedView.addView(alphaLabel)

        val alphaSeekBar = SeekBar(context).apply {
            max = 100
            progress = (menuAlpha * 100).toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    menuAlpha = progress / 100f
                    alphaLabel.text = "Menu Alpha: ${progress}%"
                    layout.alpha = menuAlpha
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
        expandedView.addView(alphaSeekBar)

        // Apply All button
        val applyAllButton = Button(context).apply {
            text = "Apply All"
            setBackgroundColor(0x8000FF00.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            setOnClickListener { applyAllFeatures() }
        }
        expandedView.addView(applyAllButton)

        // Reset button
        val resetButton = Button(context).apply {
            text = "Reset All"
            setBackgroundColor(0x80FF0000.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            setOnClickListener { resetAllSettings() }
        }
        expandedView.addView(resetButton)

        // Close button
        val closeButton = Button(context).apply {
            text = "Close"
            setBackgroundColor(0x80000000.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            setOnClickListener { collapseMenu() }
        }
        expandedView.addView(closeButton)

        // Add both views to main layout
        layout.addView(collapsedView)
        layout.addView(expandedView)

        // Store references for later use
        @Suppress("UNCHECKED_CAST")
        val views = mapOf(
            "collapsed" to collapsedView,
            "expanded" to expandedView,
            "godMode" to godModeCheckBox,
            "infiniteAmmo" to infiniteAmmoCheckBox,
            "autoAim" to autoAimCheckBox,
            "showFPS" to showFPSCheckBox,
            "speedSeekBar" to speedSeekBar
        )
        layout.tag = views

        return layout
    }

    private fun expandMenu() {
        val layout = floatingView as? LinearLayout ?: return
        @Suppress("UNCHECKED_CAST")
        val views = layout.tag as? Map<String, View> ?: return
        
        val collapsedView = views["collapsed"]
        val expandedView = views["expanded"]
        
        collapsedView?.visibility = View.GONE
        expandedView?.visibility = View.VISIBLE
        isExpanded = true
    }

    private fun collapseMenu() {
        val layout = floatingView as? LinearLayout ?: return
        @Suppress("UNCHECKED_CAST")
        val views = layout.tag as? Map<String, View> ?: return
        
        val collapsedView = views["collapsed"]
        val expandedView = views["expanded"]
        
        collapsedView?.visibility = View.VISIBLE
        expandedView?.visibility = View.GONE
        isExpanded = false
    }

    private fun applyFeature(feature: String) {
        XposedBridge.log("SoulStrikeFloatingMenu: Applying feature: $feature")
        // Here you would implement the actual game modifications
        // For now, just log the feature
    }

    private fun applyAllFeatures() {
        XposedBridge.log("SoulStrikeFloatingMenu: Applying all features")
        
        if (godMode) applyFeature("God Mode")
        if (infiniteAmmo) applyFeature("Infinite Ammo")
        if (autoAim) applyFeature("Auto Aim")
        if (showFPS) applyFeature("Show FPS")
        
        Toast.makeText(context, "All changes applied!", Toast.LENGTH_SHORT).show()
    }
    
    private fun resetAllSettings() {
        val layout = floatingView as? LinearLayout ?: return
        @Suppress("UNCHECKED_CAST")
        val views = layout.tag as? Map<String, View> ?: return
        
        // Reset all toggles
        (views["godMode"] as? CheckBox)?.isChecked = false
        (views["infiniteAmmo"] as? CheckBox)?.isChecked = false
        (views["autoAim"] as? CheckBox)?.isChecked = false
        (views["showFPS"] as? CheckBox)?.isChecked = true
        (views["speedSeekBar"] as? SeekBar)?.progress = 100
        
        // Reset variables
        godMode = false
        infiniteAmmo = false
        autoAim = false
        showFPS = true
        playerSpeed = 1.0f
        
        Toast.makeText(context, "Settings reset!", Toast.LENGTH_SHORT).show()
    }
}