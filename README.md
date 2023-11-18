# kotlin-encoderDecoder
As part of jet brains application, built a single file Kotlin application for encoding and decoding a string, complete with unit tests and error handling.

JetBrains problem statement:
Implement a compression algorithm that saves space by storing repeated data as a single value, along with the number of times it's repeated. 
For example, the string "AAAAABBB#####" could result in "A±5B±3#±5". 
The ± symbol acts as a separator between the frequency and the character itself. 
Your task is to create a program that can both compress and decompress data. Your program should be memory and time-efficient. 
Protect your algorithm from being manipulated by extraordinarily anomalous data. 
Your code should be covered with unit tests and should be written in either Java or Kotlin, without additional libraries.
