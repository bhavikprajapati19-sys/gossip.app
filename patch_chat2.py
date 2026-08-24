content = open('app/src/main/java/com/example/screens/ChatScreen.kt').read()

content = content.replace(
    '''    val historicBackgrounds = listOf(
        com.example.R.drawable.img_historic_india_1_1787147522050,
        com.example.R.drawable.img_historic_india_2_1787147543595,
        com.example.R.drawable.img_historic_india_3_1787147566220
    )''',
    '''    val historicBackgrounds = listOf(
        com.example.R.drawable.img_royal_bg_1787146791490,
        com.example.R.drawable.img_modern_3d_bg_1787146333038
    )'''
)

open('app/src/main/java/com/example/screens/ChatScreen.kt', 'w').write(content)
