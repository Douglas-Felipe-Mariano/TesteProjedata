<template>
  <div class="planning-page">
    <div class="container">
      <div class="page-header">
        <h1>{{ t('planning.title') }}</h1>
        <p class="page-subtitle">{{ t('planning.subtitle') }}</p>
      </div>

      <!-- Action Bar -->
      <div class="action-bar">
        <button @click="calculateProduction" class="btn-primary" :disabled="isLoading">
          <span v-if="!isLoading">{{ t('planning.suggestions', 'Generate Suggestion') }}</span>
          <span v-else>{{ t('common.loading', 'Loading...') }}</span>
        </button>
      </div>

      <!-- Results Section -->
      <div v-if="suggestion" class="planning-results">
        <!-- Revenue Card -->
        <div class="card revenue-card">
          <div class="card-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="8" fill="#42b983"/>
              <path d="M12 6v6l4 2" stroke="#ffffff" stroke-width="2" stroke-linecap="round"/>
              <circle cx="12" cy="12" r="1" fill="#ffffff"/>
            </svg>
          </div>
          <div class="card-content">
            <p class="card-label">{{ t('planning.totalRevenue', 'Total Revenue') }}</p>
            <h2 class="card-value">{{ formatCurrency(totalRevenue) }}</h2>
          </div>
        </div>

        <!-- Production Plan Table -->
        <section class="card">
          <div class="section-header">
            <h3>{{ t('planning.productionPlan', 'Production Plan') }}</h3>
          </div>
          <div v-if="suggestion.items.length === 0" class="empty-state">
            <p>{{ t('planning.noData', 'No planning data available') }}</p>
          </div>
          <div v-else class="table-wrapper">
            <table class="planning-table">
              <thead>
                <tr>
                  <th>{{ t('products.name', 'Product Name') }}</th>
                  <th>{{ t('planning.quantity', 'Quantity') }}</th>
                  <th>{{ t('planning.subtotal', 'Subtotal') }}</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in enhancedItems" :key="item.productId || item.prodId || item.id" class="data-row">
                  <td class="product-cell">
                    <span class="product-name">{{ item.displayName }}</span>
                  </td>
                  <td class="quantity-cell">
                    <span class="qty-badge">{{ formatNumber(item.displayQuantity) }} {{ item.unitsText }}</span>
                  </td>
                  <td class="revenue-cell">
                    {{ formatCurrency(item.displaySubtotal) }}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>
      </div>

      <!-- Empty State -->
      <div v-else class="empty-planning">
        <div class="empty-icon">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
            <rect x="3" y="12" width="4" height="8" fill="#62748a"/>
            <rect x="9" y="8" width="4" height="12" fill="#2c3e50"/>
            <rect x="15" y="4" width="4" height="16" fill="#42b983"/>
          </svg>
        </div>
        <h3>{{ t('planning.noData', 'No Data Available') }}</h3>
        <p>{{ t('planning.subtitle', 'Optimize your production schedule') }}</p>
        <button @click="calculateProduction" class="btn-primary">
          {{ t('planning.generateFirst', 'Generate Your First Suggestion') }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useI18n } from '../../composables/useI18n'
import api from '../../services/api'

const { t, formatCurrency, formatNumber, getUnitsText } = useI18n()

const suggestion = ref(null)
const isLoading = ref(false)

// Computed property to calculate subtotals if not provided by API
const enhancedItems = computed(() => {
  if (!suggestion.value?.items) return []
  
  return suggestion.value.items.map(item => {
    const quantity = item.quantityToProduce || item.quantity || 0
    // Use totalRevenue directly from API
    const revenue = item.totalRevenue || 0
    // Convert from "52,50" to 52.50 for proper numeric display
    const subtotalValue = typeof revenue === 'string' ? 
      parseFloat(revenue.replace(',', '.')) : 
      parseFloat(revenue) || 0
    
    return {
      ...item,
      displayName: item.prodName || item.productName || 'Unknown Product',
      displayQuantity: quantity,
      displaySubtotal: subtotalValue,
      unitsText: getUnitsText(quantity)
    }
  })
})

// Total revenue computed property
const totalRevenue = computed(() => {
  if (!suggestion.value?.totalValue) return 0
  
  const total = suggestion.value.totalValue
  return typeof total === 'string' ? 
    parseFloat(total.replace(',', '.')) : 
    parseFloat(total) || 0
})

const calculateProduction = async () => {
  try {
    isLoading.value = true
    const res = await api.get('/production-planning/suggestion')
    suggestion.value = res.data
  } catch (error) {
    console.error('Error calculating production:', error)
    suggestion.value = null
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped src="./Planning.css"></style>
