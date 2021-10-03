package com.theapache64.boil.util

import com.winterbe.expekt.should
import org.junit.Test


/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 14:10
 */
class GradleUtilsTest {
    @Test
    fun `gradle project packageName`() {
        val boilDir = System.getProperty("user.dir")
        val packageName = GradleUtils.getProjectPackageName("SomeFile.kt", boilDir)
        packageName?.second.should.equal("com.theapache64.boil")
    }

    @Test
    fun `android project packageName`() {
        val nemoDir = "/home/theapache64/Documents/projects/nemo"
        val packageName = GradleUtils.getProjectPackageName("SomeFile.kt", nemoDir)
        packageName?.second.should.equal("com.theapache64.nemo")
    }

    @Test
    fun `kts project packageName`() {
        val composeDesktopDir = "/home/theapache64/Documents/projects/decompose-desktop-example"
        val packageName = GradleUtils.getProjectPackageName("SomeFile.kt", composeDesktopDir)
        packageName?.second.should.equal("com.theapache64.dde")
    }

    @Test
    fun `no package name`() {
        val nemoDir = "/home/theapache64/Documents/projects"
        val packageName = GradleUtils.getProjectPackageName("SomeFile.kt", nemoDir)
        packageName.should.`null`
    }
}