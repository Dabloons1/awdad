package com.soulstrikefloatingmenu

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HookEntry : IXposedHookLoadPackage {
    private var floatingMenu: SimpleFloatingMenu? = null

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.com2usholdings.soulstrike.android.google.global.normal") {
            return
        }

        XposedBridge.log("SoulStrikeFloatingMenu: Hook loaded for ${lpparam.packageName}")

        // Hook Activity creation to show ImGui menu
        XposedHelpers.findAndHookMethod(
            Activity::class.java,
            "onCreate",
            Bundle::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val activity = param.thisObject as Activity
                    if (activity.javaClass.name.contains("MainActivity") || 
                        activity.javaClass.name.contains("GameActivity") ||
                        activity.javaClass.name.contains("UnityPlayerActivity")) {
                        
                        XposedBridge.log("SoulStrikeFloatingMenu: Activity created, showing floating menu")
                        showFloatingMenu(activity)
                    }
                }
            }
        )

        // Hook Unity's main activity for better compatibility
        try {
            XposedHelpers.findAndHookMethod(
                "com.unity3d.player.UnityPlayerActivity",
                lpparam.classLoader,
                "onCreate",
                Bundle::class.java,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val activity = param.thisObject as Activity
                        XposedBridge.log("SoulStrikeFloatingMenu: Unity activity created, showing floating menu")
                        showFloatingMenu(activity)
                    }
                }
            )
        } catch (e: Exception) {
            XposedBridge.log("SoulStrikeFloatingMenu: Unity activity hook failed: ${e.message}")
        }

        // Hook Application onCreate to ensure we can show the menu
        XposedHelpers.findAndHookMethod(
            "android.app.Application",
            lpparam.classLoader,
            "onCreate",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    XposedBridge.log("SoulStrikeFloatingMenu: Application onCreate hooked")
                    val app = param.thisObject as android.app.Application
                    
                    // Only show menu if not running through security proxy
                    if (!app.javaClass.name.contains("Proxy") && !app.javaClass.name.contains("Security")) {
                        showFloatingMenu(app)
                    } else {
                        XposedBridge.log("SoulStrikeFloatingMenu: Skipping menu creation due to security proxy")
                    }
                }
            }
        )
        
        // Hook any activity that might be the main game activity
        XposedHelpers.findAndHookMethod(
            "android.app.Activity",
            lpparam.classLoader,
            "onResume",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val activity = param.thisObject as Activity
                    if (activity.javaClass.name.contains("MainActivity") || 
                        activity.javaClass.name.contains("GameActivity") ||
                        activity.javaClass.name.contains("UnityPlayerActivity") ||
                        activity.javaClass.name.contains("SoulStrike")) {
                        XposedBridge.log("SoulStrikeFloatingMenu: Game activity resumed, showing floating menu")
                        showFloatingMenu(activity)
                    }
                }
            }
        )

        // Hook game-specific functions for feature implementation
        hookGameFunctions(lpparam)
    }

    private fun showFloatingMenu(context: Context) {
        try {
            if (floatingMenu == null) {
                XposedBridge.log("SoulStrikeFloatingMenu: Creating floating menu for context: ${context.javaClass.name}")
                
                // Check if we're running through a security proxy
                if (context.javaClass.name.contains("Proxy") || context.javaClass.name.contains("Security")) {
                    XposedBridge.log("SoulStrikeFloatingMenu: WARNING - Running through security proxy: ${context.javaClass.name}")
                    XposedBridge.log("SoulStrikeFloatingMenu: This may interfere with overlay permissions")
                }
                
                // Ensure we have a valid context
                val appContext = context.applicationContext ?: context
                floatingMenu = SimpleFloatingMenu(appContext)
                
                // Add a small delay to ensure the activity is fully initialized
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    try {
                        floatingMenu?.show()
                        XposedBridge.log("SoulStrikeFloatingMenu: Floating menu creation attempted")
                    } catch (e: Exception) {
                        XposedBridge.log("SoulStrikeFloatingMenu: Error showing floating menu: ${e.message}")
                        e.printStackTrace()
                    }
                }, 1000) // 1 second delay
            }
        } catch (e: Exception) {
            XposedBridge.log("SoulStrikeFloatingMenu: Error creating floating menu: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun hookGameFunctions(lpparam: XC_LoadPackage.LoadPackageParam) {
        // Temporarily disable game-specific hooks to prevent ClassNotFoundException
        // These hooks will be re-enabled once we identify the correct class names
        XposedBridge.log("SoulStrikeFloatingMenu: Game-specific hooks disabled to prevent crashes")
        XposedBridge.log("SoulStrikeFloatingMenu: Focus on floating menu display first")
        
        // TODO: Re-enable these hooks once we have the correct class names from the game
        // For now, the floating menu should work without these hooks
    }
}
