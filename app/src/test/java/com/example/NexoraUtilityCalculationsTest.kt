package com.example

import com.example.data.catalog.ToolCatalog
import org.junit.Assert.*
import org.junit.Test
import kotlin.math.pow

class NexoraUtilityCalculationsTest {

    @Test
    fun testToolCatalogCoverage() {
        assertTrue("Catalog should contain over 40 tools", ToolCatalog.tools.size >= 40)
        assertNotNull("Standard calculator must exist", ToolCatalog.getToolById("calc_standard"))
        assertNotNull("GST calculator must exist", ToolCatalog.getToolById("calc_gst"))
        assertNotNull("EMI calculator must exist", ToolCatalog.getToolById("calc_emi"))
        assertNotNull("BMI calculator must exist", ToolCatalog.getToolById("calc_bmi"))
        assertNotNull("Password generator must exist", ToolCatalog.getToolById("text_password"))
        assertNotNull("QR generator must exist", ToolCatalog.getToolById("qr_generator"))
    }

    @Test
    fun testGSTCalculations() {
        val amount = 1000.0
        val rate = 18.0
        // Exclusive
        val gstExclusive = (amount * rate) / 100.0
        assertEquals(180.0, gstExclusive, 0.001)
        assertEquals(1180.0, amount + gstExclusive, 0.001)

        // Inclusive
        val net = (amount * 100.0) / (100.0 + rate)
        val gstInclusive = amount - net
        assertEquals(847.4576, net, 0.01)
        assertEquals(152.5423, gstInclusive, 0.01)
    }

    @Test
    fun testEMICalculation() {
        val p = 1000000.0
        val annualRate = 8.5
        val years = 15.0

        val r = (annualRate / 12.0) / 100.0
        val n = years * 12.0
        val emi = (p * r * (1.0 + r).pow(n)) / ((1.0 + r).pow(n) - 1.0)

        // Standard 10 Lakh at 8.5% for 15 yrs is ~9847
        assertEquals(9847.4, emi, 1.0)
    }

    @Test
    fun testBMICalculation() {
        val weight = 70.0
        val heightCm = 175.0
        val hM = heightCm / 100.0
        val bmi = weight / (hM * hM)

        assertEquals(22.85, bmi, 0.01)
        assertTrue("BMI 22.85 should be normal weight", bmi in 18.5..24.9)
    }

    @Test
    fun testSearchCatalog() {
        val results = ToolCatalog.searchTools("gst")
        assertTrue("Search for 'gst' should find GST calculator", results.any { it.id == "calc_gst" })

        val mathResults = ToolCatalog.searchTools("calculate")
        assertTrue("Search for 'calculate' should yield results", mathResults.isNotEmpty())
    }
}
