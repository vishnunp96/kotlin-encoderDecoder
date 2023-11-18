import java.lang.Exception
import java.lang.NumberFormatException

class EncoderDecoder(private var separator: Char){

    fun getSeparator(): Char{
        return separator
    }

    private fun appendEncoded(ch: Char, count: Int, sb: StringBuilder){
        sb.append(ch).append(separator).append(count)
    }

    private fun appendDecoded(ch: Char, count: Int, sb: StringBuilder){
        sb.append(ch.toString().repeat(count))
    }

    private fun buildEncodedString(str: String, encodedStringBuilder: StringBuilder){
        var currentChar: Char = str[0]
        var charCount = 0
        for((index, chr) in str.withIndex()){
            if(index == 0){
                currentChar = chr
                charCount = 1
                continue
            }

            if(chr == currentChar)
                charCount++
            else{
                appendEncoded(currentChar, charCount, encodedStringBuilder)
                currentChar = chr
                charCount = 1
            }

        }
        appendEncoded(currentChar, charCount, encodedStringBuilder)
    }

    private fun buildDecodedString(splitEncoded: List<String>, decodedStringBuilder: StringBuilder){
        var currentChar: Char = splitEncoded[0][0]
        var charCount: Int
        for((index, splitItem) in splitEncoded.withIndex()){
            if(index == 0)
                continue
            try {
                charCount = if(index == splitEncoded.size - 1) splitItem.toInt()
                else splitItem.dropLast(1).toInt()
            } catch (nfe: NumberFormatException){
                throw IllegalArgumentException("Encoded string provided is invalid.")
            }
            appendDecoded(currentChar, charCount, decodedStringBuilder)
            currentChar = splitItem.last()
        }
    }

    fun encode(str: String) : String{
        if(str.isEmpty())
            return ""
        if(str.contains(separator))
            throw IllegalArgumentException("Encoded string provided is invalid, contains the separator.")

        val encodedStringBuilder = StringBuilder()
        buildEncodedString(str, encodedStringBuilder)

        return encodedStringBuilder.toString()
    }

    fun decode(str: String): String{
        if(str.isEmpty())
            return ""

        val splitEncoded = str.split(separator)
        if(splitEncoded.size < 2)
            throw IllegalArgumentException("Encoded string provided is invalid.")

        if(splitEncoded[0].length != 1)
            throw IllegalArgumentException("Encoded string provided is invalid.")

        val decodedStringBuilder = StringBuilder()
        buildDecodedString(splitEncoded, decodedStringBuilder)

        return decodedStringBuilder.toString()
    }

}

class EncoderDecoderTester(private var encoderDecoder: EncoderDecoder){

    private fun encodeTestInvalidStrings(): Boolean{
        try{
            val inputStr = "AAABBBB#DDDEE".replace('#', encoderDecoder.getSeparator())
            encoderDecoder.encode(inputStr)
        } catch (iae: java.lang.IllegalArgumentException){
            return true
        } catch (e: Exception){
            return false
        }
        return false
    }

    private fun encodeTestEncoding1(): Boolean{
        return try{
            val inputStr = ""
            val expectedStr = ""
            val outputStr = encoderDecoder.encode(inputStr)
            expectedStr == outputStr
        } catch (e: Exception) {
            false
        }
    }

    private fun encodeTestEncoding2(): Boolean{
        return try{
            val inputStr = "AAAAAABBCCCCCCDCCBB3"
            val expectedStr = "A±6B±2C±6D±1C±2B±23±1".replace('±', encoderDecoder.getSeparator())
            val outputStr = encoderDecoder.encode(inputStr)
            expectedStr == outputStr
        } catch (e: Exception) {
            false
        }
    }

    private fun decodeTestInvalidStrings1(): Boolean{
        try{
            val inputStr = "AAABBBC"
            encoderDecoder.decode(inputStr)
        } catch (iae: java.lang.IllegalArgumentException){
            return true
        } catch (e: Exception){
            return false
        }
        return false
    }

    private fun decodeTestInvalidStrings2(): Boolean{
        try{
            val inputStr = "AAA±4B±23C±8D±1"
            encoderDecoder.decode(inputStr)
        } catch (iae: java.lang.IllegalArgumentException){
            return true
        } catch (e: Exception){
            return false
        }
        return false
    }

    private fun decodeTestInvalidStrings3(): Boolean{
        try{
            val inputStr = "A±4BB±23C±8D±1"
            encoderDecoder.decode(inputStr)
        } catch (iae: java.lang.IllegalArgumentException){
            return true
        } catch (e: Exception){
            return false
        }
        return false
    }

    private fun decodeTestDecoding1(): Boolean{
        return try{
            val inputStr = ""
            val expectedStr = ""
            val outputStr = encoderDecoder.decode(inputStr)
            expectedStr == outputStr
        } catch (e: Exception) {
            false
        }
    }

    private fun decodeTestDecoding2(): Boolean{
        return try{
            val inputStr = "A±6B±2C±6D±1C±2B±23±1".replace('±', encoderDecoder.getSeparator())
            val expectedStr = "AAAAAABBCCCCCCDCCBB3"
            val outputStr = encoderDecoder.decode(inputStr)
            expectedStr == outputStr
        } catch (e: Exception) {
            false
        }
    }
    private fun verifyTests(coder: String, testResults: List<Boolean>, testDesc: List<String>){
        if (testResults.all { it }){
            println("All $coder tests passed successfully.\n" +
                    "${testResults.size}/${testResults.size} tests passed.")
        }else{
            println("There are test failures. ${testResults.count { it }}/${testResults.size} passed.")
            for((index, result) in testResults.withIndex()){
                if(!result)
                    println("Test failed:\t" + testDesc[index])
            }
        }
    }

    fun runEncoderUnitTests(){
        val testResults = listOf(   encodeTestInvalidStrings(),
                                    encodeTestEncoding1(),
                                    encodeTestEncoding2())
        val testDesc = listOf(  "Invalid string test",
                                "Correct encoding test 1",
                                "Correct encoding test 2")

        verifyTests("encoder", testResults, testDesc)
    }
    fun runDecoderUnitTests(){
        val testResults = listOf(   decodeTestInvalidStrings1(),
                                    decodeTestInvalidStrings2(),
                                    decodeTestInvalidStrings3(),
                                    decodeTestDecoding1(),
                                    decodeTestDecoding2())
        val testDesc = listOf(  "Invalid string test 1",
                                "Invalid string test 2",
                                "Invalid string test 3",
                                "Correct decoding test 1",
                                "Correct decoding test 2")

        verifyTests("decoder", testResults, testDesc)
    }
}

fun main() {
    val encoderDecoder = EncoderDecoder('±')

    val tester = EncoderDecoderTester(encoderDecoder)
    tester.runEncoderUnitTests()
    tester.runDecoderUnitTests()

    val sampleStrDecoded = "AAAAABBB#####"
    val sampleStrEncoded = "A±5B±3#±5"
    println("Lets try encoding and decoding the sample string:\t$sampleStrDecoded")
    println("Input String:\t$sampleStrDecoded\t\tEncoded Result:\t${encoderDecoder.encode(sampleStrDecoded)}")
    println("Input String:\t$sampleStrEncoded\t\tDecoded Result:\t${encoderDecoder.decode(sampleStrEncoded)}")
}