package com.example.bitcoininfoapp.utils

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UtilExtensionsTest {

    @Test
    fun viewGone_whenViewIsVisible_shouldMakeViewGone() {
        //Arrange
        val view = View(InstrumentationRegistry.getInstrumentation().targetContext)
        view.visibility = View.VISIBLE
        //Act
        view.gone()
        //Assert
        assertEquals(view.visibility, View.GONE)
    }

    @Test
    fun viewGone_whenViewIsGone_shouldRemainGone() {
        //Arrange
        val view = View(InstrumentationRegistry.getInstrumentation().targetContext)
        view.visibility = View.GONE
        //Act
        view.gone()
        //Assert
        assertEquals(view.visibility, View.GONE)
    }

    @Test
    fun viewVisible_whenViewIsGone_shouldMakeViewVisible() {
        //Arrange
        val view = View(InstrumentationRegistry.getInstrumentation().targetContext)
        view.visibility = View.GONE
        //Act
        view.visible()
        //Assert
        assertEquals(view.visibility, View.VISIBLE)
    }

    @Test
    fun viewVisible_whenViewIsVisible_shouldRemainVisible() {
        //Arrange
        val view = View(InstrumentationRegistry.getInstrumentation().targetContext)
        view.visibility = View.VISIBLE
        //Act
        view.visible()
        //Assert
        assertEquals(view.visibility, View.VISIBLE)
    }

    @Test
    fun longGetDateStringWithFullMonthName_withValidTimeStamp_shouldReturnExpectedDate() {
        //Arrange
        val timestamp = 1571908761000
        //Act
        val result = timestamp.getDateStringWithFullMonthName()
        //Assert
        assertEquals(result, "21 Oct")
    }
}