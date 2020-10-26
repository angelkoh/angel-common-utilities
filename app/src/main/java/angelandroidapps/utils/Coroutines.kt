package angelandroidapps.utils

import androidx.annotation.Keep
import kotlinx.coroutines.*

// * Created by Angel on 11/12/2019 10:59 AM.  
// * Originally created for project "Mvvm Test".
// * Copyright (c) 2019 Angel. All rights reserved.
@Suppress("unused")
@Keep
object Coroutines {

    const val TAG = "Angel: Coroutine"

    //generic function
    fun <T : Any> ioThenMain(
        work: (() -> T?),
        callback: (T?) -> Unit,
        exception: ((String) -> Unit)? = null
    ): Job =
        //    Log.d(TAG, "LAUNCH at thread ${Thread.currentThread().name}")
        CoroutineScope(Dispatchers.Main).launch {
            try {
//                Log.d(TAG, "LAUNCH at thread ${Thread.currentThread().name}")
                withContext(Dispatchers.IO) {
//                    Log.d(TAG, "WITH CONTEXT at thread ${Thread.currentThread().name}")
                    work()
                }.also(callback)
            } catch (e: Exception) {
                exception?.invoke(e.message ?: e.toString())
            }
        }

    /**
     * If you don't need the result from the method called
     * (for example it just send a update or change a color or tracker some
     * information without return)
     * @param work SuspendFunction0<T?>
     * @param exception Function1<String, Unit>?
     * @return Job
     */
    fun <T : Any> ioThenMainLaunch(
        work: suspend (() -> T?),
        exception: ((String) -> Unit)? = null
    ): Job =
        CoroutineScope(Dispatchers.Main).launch {
            //   Log.d(TAG, "LAUNCH at thread ${Thread.currentThread().name}")
            try {
                launch(Dispatchers.IO) {
                    //  Log.d(TAG, "LAUNCH at thread ${Thread.currentThread().name}")
                    work()
                }
            } catch (e: Exception) {
                exception?.invoke(e.message ?: e.toString())
            }
        }
}