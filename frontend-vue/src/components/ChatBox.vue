<script setup>
import { ref, watch, nextTick } from 'vue'
import MessageBubble from './MessageBubble.vue'
import { askAI } from '../api'

const input = ref('')
const loading = ref(false)
const messages = ref([])
const messagesBox = ref(null)

function getTime() {
  return new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

const sendMessage = async () => {
  const text = input.value.trim()
  if (!text || loading.value) return

  // Add user message
  messages.value.push({ role: 'user', text, time: getTime() })
  input.value = ''
  loading.value = true

  await nextTick(() => {
    messagesBox.value.scrollTop = messagesBox.value.scrollHeight
  })

  try {
    const answer = await askAI(text)
    messages.value.push({ role: 'assistant', text: answer, time: getTime() })
  } catch (e) {
    messages.value.push({ role: 'assistant', text: `Error: ${e.message}`, time: getTime() })
  } finally {
    loading.value = false
    await nextTick(() => {
      messagesBox.value.scrollTop = messagesBox.value.scrollHeight
    })
  }
}

// Auto-scroll
watch(messages, async () => {
  await nextTick(() => {
    messagesBox.value.scrollTop = messagesBox.value.scrollHeight
  })
})
</script>

<template>
  <div class="chat-container">
    <div class="messages" ref="messagesBox">
      <MessageBubble
        v-for="(message, index) in messages"
        :key="index"
        :message="message"
      />
      <div v-if="loading" class="typing-indicator">Assistant is typing...</div>
    </div>
    <form class="message-input" @submit.prevent="sendMessage">
      <textarea
        v-model="input"
        placeholder="Type your message..."
        rows="4"
        class="input-box"
        @keydown.enter.exact.prevent="sendMessage"
      ></textarea>
      <button type="submit" :disabled="loading">Send</button>
    </form>
  </div>
</template>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 80vh;        /* Full viewport height */
  min-width: 50vw;
  max-width: 90vw;
  margin: 20px auto;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.messages {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
  min-height: 600px;    /* Minimum box height */
  background-color: #ffffff;
}


.typing-indicator {
  font-style: italic;
  color: #888;
  text-align: center;
}

.message-input {
  display: flex;
  padding: 10px 20px;
  border-top: 1px solid #ddd;
  gap: 10px;
}

.input-box {
  flex: 1;
  border: 1px solid #ddd;
  border-radius: 20px;
  padding: 12px;
  font-size: 1rem;
  resize: none;
}

.message-input button {
  background-color: rgba(15, 98, 254, 0.9);  /* IBM blue semi-opacity */
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 20px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s;
}

.message-input button:hover {
  background-color: rgba(15, 98, 254, 1); /* slightly brighter on hover */
}

.message-input button:disabled {
  background-color: #ccc;
}

@media (max-width: 768px) {
  .chat-container {
    max-width: 100%;
    border-radius: 0;
  }
  .input-box {
    font-size: 0.95rem;
  }
  .message-input button {
    padding: 10px 20px;
  }
}
</style>
