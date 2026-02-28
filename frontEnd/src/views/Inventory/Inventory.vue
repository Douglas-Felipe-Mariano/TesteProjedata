<template>
  <div class="dashboard-page">
    <div class="container">
      <div class="page-header">
        <h1>{{ t('dashboard.title') }}</h1>
        <p class="page-subtitle">{{ t('dashboard.subtitle') }}</p>
      </div>

      <!-- KPI Cards Row -->
      <div class="kpi-grid">
        <div class="kpi-card">
          <div class="kpi-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
              <rect x="3" y="12" width="18" height="8" rx="2" fill="#42b983"/>
              <rect x="3" y="8" width="12" height="4" rx="1" fill="#2c3e50"/>
              <rect x="3" y="4" width="8" height="4" rx="1" fill="#62748a"/>
            </svg>
          </div>
          <div class="kpi-content">
            <h3>{{ t('dashboard.totalProducts') }}</h3>
            <p class="kpi-value">{{ totalProducts }}</p>
          </div>
        </div>
        <div class="kpi-card">
          <div class="kpi-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="8" fill="#42b983"/>
              <circle cx="12" cy="12" r="4" fill="#2c3e50"/>
              <circle cx="12" cy="8" r="2" fill="#ffffff"/>
              <circle cx="8" cy="14" r="1" fill="#ffffff"/>
              <circle cx="16" cy="14" r="1" fill="#ffffff"/>
            </svg>
          </div>
          <div class="kpi-content">
            <h3>{{ t('dashboard.totalMaterials') }}</h3>
            <p class="kpi-value">{{ totalMaterials }}</p>
          </div>
        </div>
        <div class="kpi-card">
          <div class="kpi-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
              <rect x="4" y="6" width="16" height="2" rx="1" fill="#42b983"/>
              <rect x="4" y="10" width="12" height="2" rx="1" fill="#2c3e50"/>
              <rect x="4" y="14" width="8" height="2" rx="1" fill="#62748a"/>
              <rect x="2" y="18" width="6" height="1" rx="0.5" fill="#2c3e50"/>
            </svg>
          </div>
          <div class="kpi-content">
            <h3>{{ t('dashboard.totalUnits') }}</h3>
            <p class="kpi-value">{{ totalUnits }}</p>
          </div>
        </div>
      </div>

      <!-- Main Content Grid -->
      <div class="dashboard-grid">
        <!-- Recent Products -->
        <section class="card dashboard-section">
          <div class="section-title">
            <h3>{{ t('nav.products') }}</h3>
            <router-link to="/products" class="section-link">View all →</router-link>
          </div>
          <div v-if="recentProducts.length === 0" class="empty-placeholder">
            <p>{{ t('products.empty') }}</p>
          </div>
          <div v-else class="product-list">
            <div v-for="product in recentProducts" :key="product.prodId" class="product-item">
              <div class="product-info">
                <p class="product-name">{{ product.prodName }}</p>
                <p class="product-price">{{ formatCurrency(product.prodPrice) }}</p>
              </div>
              <span class="comp-count">{{ product.compositions?.length || 0 }} items</span>
            </div>
          </div>
        </section>

        <!-- Recent Materials -->
        <section class="card dashboard-section">
          <div class="section-title">
            <h3>{{ t('dashboard.totalMaterials') }}</h3>
            <router-link to="/raw-materials" class="section-link">View all →</router-link>
          </div>
          <div v-if="recentMaterials.length === 0" class="empty-placeholder">
            <p>{{ t('rawMaterials.empty') }}</p>
          </div>
          <div v-else class="material-list">
            <div v-for="material in recentMaterials" :key="material.matId" class="material-item">
              <div class="material-info">
                <p class="material-name">{{ material.matName }}</p>
                <p class="material-qty">
                  {{ material.matQuantity }} <span class="unit">{{ material.unitSymbol }}</span>
                </p>
              </div>
            </div>
          </div>
        </section>

        <!-- Quick Actions -->
        <section class="card dashboard-section">
          <div class="section-title">
            <h3>{{ t('common.actions') }}</h3>
          </div>
          <div class="quick-actions">
            <router-link to="/products" class="action-btn action-primary">
              <span class="btn-icon">+</span>
              {{ t('products.addNew') }}
            </router-link>
            <router-link to="/unit-measures" class="action-btn action-secondary">
              <span class="btn-icon">+</span>
              {{ t('units.addNew') }}
            </router-link>
            <router-link to="/planning" class="action-btn action-secondary">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <rect x="3" y="12" width="4" height="8" fill="#42b983"/>
                <rect x="9" y="8" width="4" height="12" fill="#2c3e50"/>
                <rect x="15" y="4" width="4" height="16" fill="#62748a"/>
              </svg>
              {{ t('nav.optimization') }}
            </router-link>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useI18n } from '../../composables/useI18n'
import api from '../../services/api'

const { t, formatCurrency } = useI18n()

const totalProducts = ref(0)
const totalMaterials = ref(0)
const totalUnits = ref(0)
const recentProducts = ref([])
const recentMaterials = ref([])

const fetchData = async () => {
  try {
    const [productsRes, materialsRes, unitsRes] = await Promise.all([
      api.get('/products'),
      api.get('/raw-materials'),
      api.get('/unit-measures')
    ])

    const products = productsRes.data || []
    const materials = materialsRes.data || []
    const units = unitsRes.data || []

    totalProducts.value = products.length
    totalMaterials.value = materials.length
    totalUnits.value = units.length

    // Get last 5 items
    recentProducts.value = products.slice(-5).reverse()
    recentMaterials.value = materials.slice(-5).reverse()
  } catch (error) {
    console.error('Error fetching dashboard data:', error)
  }
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped src="./Inventory.css"></style>
