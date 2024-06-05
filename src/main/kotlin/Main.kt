package com.xeniac

import kotlin.math.cos
import kotlin.math.sin

fun main() {
    var a = 0.0f
    var b = 0.0f
    val width = 80
    val height = 22
    val zBuffer = FloatArray(size = width * height)
    val output = CharArray(size = width * height)
    val luminanceChars = ".,-~:;=!*#$@"

    print("\u001b[2J") // Clear the screen

    while (true) {
        // Clear output and z-buffer
        output.fill(element = ' ')
        zBuffer.fill(element = 0f)

        var j = 0f
        while (j < 6.28f) {
            var i = 0f
            while (i < 6.28f) {
                val sinA = sin(a)
                val cosA = cos(a)
                val sinB = sin(b)
                val cosB = cos(b)
                val sinI = sin(i)
                val cosI = cos(i)
                val sinJ = sin(j)
                val cosJ = cos(j)

                val h = cosJ + 2
                val d = 1 / (sinI * h * sinA + sinJ * cosA + 5)
                val t = sinI * h * cosA - sinJ * sinA

                val x = (40 + 30 * d * (cosI * h * cosB - t * sinB)).toInt()
                val y = (12 + 15 * d * (cosI * h * sinB + t * cosB)).toInt()
                val o = x + width * y
                val luminanceIndex = (8 * (
                        (sinJ * sinA - sinI * cosJ * cosA) * cosB
                                - sinI * cosJ * sinA - sinJ * cosA - cosI * cosJ * sinB)
                        ).toInt()

                if (y in 1 until height && x > 0 && width > x && d > zBuffer[o]) {
                    zBuffer[o] = d
                    output[o] = luminanceChars[maxOf(a = luminanceIndex, b = 0)]
                }
                i += 0.02f
            }
            j += 0.07f
        }

        // Move cursor to top-left
        print("\u001b[H")
        for (k in output.indices) {
            print(if (k % width != 0) output[k] else "\n")
        }

        a += 0.04f
        b += 0.02f

        Thread.sleep(16) // Roughly 60 FPS
    }
}