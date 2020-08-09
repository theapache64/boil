package com.theapache64.boil.utils

import com.winterbe.expekt.should
import org.junit.Test


/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 14:10
 */
class GradleUtilsTest {
    @Test
    fun `gradle project packageName`() {
        val boilDir = System.getProperty("user.dir")
        val packageName = GradleUtils.getProjectPackageName(boilDir)
        packageName.should.equal("com.theapache64.boil")
    }

    @Test
    fun `android project packageName`() {
        val nemoDir = "/home/theapache64/Documents/projects/nemo"
        val packageName = GradleUtils.getProjectPackageName(nemoDir)
        packageName.should.equal("com.theapache64.nemo")
    }

    @Test
    fun `no package name`() {
        val nemoDir = "/home/theapache64/Documents/projects"
        val packageName = GradleUtils.getProjectPackageName(nemoDir)
        packageName.should.`null`
    }
}