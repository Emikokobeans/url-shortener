<template>
  <main class="container">
    <h1>URL Shortener</h1>

    <form @submit.prevent="handleSubmit" class="form">
      <label for="fullUrl">Paste the URL to be shortened</label>
      <input
        id="fullUrl"
        v-model="fullUrl"
        type="url"
        placeholder="Enter the link here"
        required
      />

      <label for="customAlias">Custom alias (optional)</label>
      <input
        id="customAlias"
        v-model="customAlias"
        type="text"
        placeholder="my-custom-alias"
      />

      <button type="submit" :disabled="loading">
        {{ loading ? 'Shortening...' : 'Shorten URL' }}
      </button>
    </form>

    <p v-if="error" class="error">{{ error }}</p>

    <section v-if="result" class="result">
      <h2>Short URL Created</h2>
      <p>
        <strong>Short URL:</strong>
        <a :href="result.shortenedUrl" target="_blank" rel="noreferrer">{{ result.shortenedUrl }}</a>
      </p>
    </section>

     <section class="list-section">
      <div class="list-header">
        <h2>Existing URLs</h2>
        <button type="button" class="secondary" @click="loadUrls" :disabled="loadingList">
          {{ loadingList ? 'Refreshing...' : 'Refresh' }}
        </button>
      </div>

      <ul v-if="urls.length" class="url-list">
        <li v-for="item in urls" :key="item.alias" class="url-item">
          <div>
            <div><strong>{{ item.alias }}</strong></div>
            <div class="list-item">{{ item.fullUrl }}</div>
            <div class="list-item">
              <a :href="item.shortenedUrl" target="_blank" rel="noreferrer">{{ item.shortenedUrl }}</a>
            </div>
          </div>
          <button type="button" class="danger" @click="handleDelete(item.alias)">Delete</button>
        </li>
      </ul>

      <p v-else class="list-item">No shortened URLs yet.</p>
    </section>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { deleteUrl, listUrls, shortenUrl } from './utils/urlService'

const fullUrl = ref('')
const customAlias = ref('')
const result = ref<shortenResult | null>(null)
const error = ref('')
const loading = ref(false)
const loadingList = ref(false)
const urls = ref<UrlItem[]>([])

interface shortenResult {
  shortenedUrl: string
}

interface UrlItem {
  alias: string
  fullUrl: string
  shortenedUrl: string
}

async function handleSubmit() {
  error.value = ''
  result.value = null
  loading.value = true

  try {
    result.value = await shortenUrl(fullUrl.value, customAlias.value)
    fullUrl.value = ''
    customAlias.value = ''
    await loadUrls()
  } catch (err) {
    error.value = (err as Error).message
  } finally {
    loading.value = false
  }
}

async function loadUrls() {
  loadingList.value = true
  try {
    urls.value = await listUrls()
  } catch (err) {
    error.value = (err as Error).message
  } finally {
    loadingList.value = false
  }
}

async function handleDelete(alias: string) {
  error.value = ''
  try {
    await deleteUrl(alias)
    await loadUrls()
  } catch (err) {
    error.value = (err as Error).message
  }
}

onMounted(loadUrls)
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

.result,
.list-section  {
  border: 1px solid #d1d5db;
  border-radius: 8px;
  padding: 16px;
  background: white;
  margin-top: 16px;
}

.error {
  color: #b91c1c;
}

.list-item {
  color: #6b7280;
  font-size: 14px;
  word-break: break-all;
}

.url-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: grid;
  gap: 12px;
}

.url-item {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  padding: 12px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

button.secondary {
  background: #f3f4f6;
}
</style>