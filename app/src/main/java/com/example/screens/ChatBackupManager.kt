package com.example.screens

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatBackupManager(private val context: Context) {

    suspend fun exportChatHistoryToLocal(): Boolean = withContext(Dispatchers.IO) {
        try {
            val backupArray = JSONArray()

            // Export Chat List
            for (chat in mockChats) {
                val chatObj = JSONObject()
                chatObj.put("id", chat.id)
                chatObj.put("name", chat.name)
                chatObj.put("lastMessage", chat.lastMessage)
                chatObj.put("time", chat.time)
                backupArray.put(chatObj)
            }

            // In a real app we would iterate all messages for all chats.
            // For now, we backup the mock structure.
            val rootObj = JSONObject()
            rootObj.put("timestamp", System.currentTimeMillis())
            rootObj.put("chats", backupArray)

            // Write to local storage (Internal files dir)
            val backupFile = File(context.filesDir, "chat_backup.json")
            val writer = FileWriter(backupFile)
            writer.use {
                it.write(rootObj.toString(4))
            }
            
            Log.d("ChatBackup", "Successfully exported backup to: ${backupFile.absolutePath}")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ChatBackup", "Failed to export backup", e)
            false
        }
    }
    
    fun getLastBackupTime(): String {
        val backupFile = File(context.filesDir, "chat_backup.json")
        if (!backupFile.exists()) return "Never"
        
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(backupFile.lastModified()))
    }
}
