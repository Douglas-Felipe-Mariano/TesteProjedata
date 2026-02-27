<template>
  <div class="planning-page">
    <div class="planning-header">
      <div>
        <h1>{{ $t('planning.title') }}</h1>
        <p>{{ $t('planning.description') }}</p>
      </div>
      <button @click="calculateProduction" class="btn-optimize">
        🚀 {{ $t('planning.calculateBtn') }}
      </button>
    </div>

    <div v-if="suggestion" class="dashboard-grid">
      <div class="card summary-card">
        <span class="label">{{ $t('planning.totalRevenue') }}</span>
        <h2 class="total-value">{{ formatCurrency(suggestion.totalValue) }}</h2>
      </div>

      <div class="card table-card">
        <table class="modern-table">
          <thead>
            <tr>
              <th>{{ $t('planning.productName') }}</th>
              <th>{{ $t('planning.quantity') }}</th>
              <th>{{ $t('planning.subtotal') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in suggestion.items" :key="item.productId">
              <td><strong>{{ item.productName }}</strong></td>
              <td><span class="qty-badge">{{ item.quantityToProduce }} units</span></td>
              <td class="revenue-cell">{{ formatCurrency(item.totalValue) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import api from '../../services/api';

const suggestion = ref(null);
const calculateProduction = async () => {
  const res = await api.get('/production-planning/suggestion');
  suggestion.value = res.data;
};
const formatCurrency = (v) => new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(v);
</script>

<style scoped src="./Planning.css"></style>