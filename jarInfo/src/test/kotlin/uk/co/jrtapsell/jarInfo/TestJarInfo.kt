package uk.co.jrtapsell.jarInfo

import org.testng.Assert
import org.testng.annotations.Test

class TestJarInfo {
    @Test(dataProviderClass = JarProvider::class, dataProvider = "jars")
    fun `Checks that none of the test jars have 0 files in`(tj: TestJar) {
        val info = JarInfo(tj.path)
        val ret = info.walkFiles().toList()
        Assert.assertNotEquals(0, ret.size)
    }

    @Test(dataProviderClass = JarProvider::class, dataProvider = "jars")
    fun `Checks that directory listings work for META-INF`(tj: TestJar) {
        val info = JarInfo(tj.path)
        val ret = info.listDirectory("META-INF").toList()
        Assert.assertNotEquals(0, ret.count())
    }

    @Test(dataProviderClass = JarProvider::class, dataProvider = "jars")
    fun `Checks the jars signiture statuses are as expected`(tj: TestJar) {
        val info = JarInfo(tj.path)
        val ret = info.getTotalSigners()
        val shouldBeSigned = tj.signed
        val isSigned = !ret.isEmpty()
        Assert.assertFalse(shouldBeSigned xor isSigned)
    }
}
