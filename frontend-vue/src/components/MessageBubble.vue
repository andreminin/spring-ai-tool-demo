<template>
  <div class="message-wrapper" :class="message.role">
    <!-- Avatar -->
    <div class="avatar">
      <img :src="message.role === 'user' ? userAvatar : assistantAvatar" alt="Avatar" />
    </div>

    <!-- Bubble -->
    <div class="bubble">
      <p>{{ message.text }}</p>
      <span class="timestamp">{{ message.time }}</span>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
import userAvatar from '/assets/user.png'
import assistantAvatar from '/assets/assistant.png'

const { message } = defineProps({ message: Object })
</script>

<style scoped>
.message-wrapper {
  display: flex;
  align-items: flex-end;
  margin: 10px 0;
}

/* User bubble: avatar on left, bubble shifted slightly right */
/* User bubble color */
.message-wrapper.user .bubble {
  background-color: rgba(15, 98, 254, 0.85); /* IBM blue with semi-opacity */
  color: #fff;
}


/* Assistant bubble: avatar on left, bubble to the right */
.message-wrapper.assistant {
  flex-direction: row-reverse;
}

/* Avatar */
.avatar {
  width: 40px;   /* Set avatar width */
  height: 40px;
  flex-shrink: 0;
  margin: 0 8px; /* Space between avatar and bubble */
  border-radius: 50%;
  overflow: hidden;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* Bubble */
.bubble {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 16px;
  word-wrap: break-word;
}

/* Colors */
.message-wrapper.user .bubble {
  background-color: #007bff;
  color: #fff;
}

.message-wrapper.assistant .bubble {
  background-color: #f1f1f1;
  color: #000;
}

/* Timestamp */
.timestamp {
  font-size: 0.75rem;
  color: rgba(0, 0, 0, 0.5);
  margin-top: 4px;
  text-align: right;
}
</style>
