#!/bin/bash
sed -i 's/selectedTab == 0/selectedBottomTab == 0/g' app/src/main/java/com/example/screens/ChatListScreen.kt
sed -i 's/selectedTab == 1/selectedBottomTab == 1/g' app/src/main/java/com/example/screens/ChatListScreen.kt
sed -i 's/selectedTab == 2/selectedBottomTab == 2/g' app/src/main/java/com/example/screens/ChatListScreen.kt
sed -i 's/when (selectedTab)/when (selectedBottomTab)/g' app/src/main/java/com/example/screens/ChatListScreen.kt
