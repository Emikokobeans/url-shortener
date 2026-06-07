<template>
  <main class="container">
    <h1>URL Shortener MVP</h1>

    <form @submit.prevent="handleSubmit" class="form">
      <label for="inputUrl">Full URL</label>
      <input
        id="inputUrl"
        v-model="inputUrl"
        type="url"
        placeholder="https://www.tpximpact.com/about"
        required
      />

      <button type="submit" :disabled="loading">
        {{ loading ? 'Shortening...' : 'Shorten URL' }}
      </button>
    </form>

    <p v-if="error" class="error">{{ error }}</p>

    <section v-if="result" class="result">
      <h2>Short URL Created</h2>
      <p><strong>Alias:</strong> {{ result.alias }}</p>
      <p>
        <strong>Short URL:</strong>
        <a :href="result.shortUrl" target="_blank" rel="noreferrer">{{ result.shortUrl }}</a>
      </p>
      <p><strong>Original URL:</strong> {{ result.inputUrl }}</p>
    </section>
  </main>
</template>

<script setup>
import { ref } from 'vue'
import { shortenUrl } from './utils/urlService'

const inputUrl = ref('')
const result = ref(null)
const error = ref('')
const loading = ref(false)

async function handleSubmit() {
  error.value = ''
  result.value = null
  loading.value = true

  try {
    result.value = await shortenUrl(inputUrl.value)
    inputUrl.value = ''
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.container {
  max-width: 680px;
  margin: 40px auto;
  padding: 24px;
}

.form {
  display: grid;
  gap: 12px;
  margin-bottom: 24px;
}

input,
button {
  padding: 12px;
  font-size: 16px;
}

.result {
  border: 1px solid #d1d5db;
  border-radius: 8px;
  padding: 16px;
  background: white;
}

.error {
  color: #b91c1c;
}
</style>