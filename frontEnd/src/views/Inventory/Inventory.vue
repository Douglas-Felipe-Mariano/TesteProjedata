<template>
  <div class="inventory-page">
    <header class="page-header">
      <h1>{{ $t('inventory.title') }}</h1>
    </header>

    <section class="card form-section">
      <h3>{{ $t('inventory.addNew') }}</h3>
      <form @submit.prevent="saveMaterial" class="form-grid">
        <div class="input-group">
          <label>{{ $t('inventory.name') }}</label>
          <input v-model="form.matName" required class="custom-input" />
        </div>
        <div class="input-group">
          <label>{{ $t('inventory.quantity') }}</label>
          <input v-model.number="form.matQuantity" type="number" step="0.01" required class="custom-input" />
        </div>
        <div class="input-group">
          <label>{{ $t('inventory.unit') }}</label>
          <select v-model="form.unitId" required class="custom-select">
            <option value="">Select...</option>
            <option :value="1">Kilogram (kg)</option>
            <option :value="2">Unit (un)</option>
          </select>
        </div>
        <button type="submit" class="btn-primary">{{ $t('inventory.save') }}</button>
      </form>
    </section>

    <section class="card table-section">
      <table class="modern-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>{{ $t('inventory.name') }}</th>
            <th>{{ $t('inventory.quantity') }}</th>
            <th>{{ $t('inventory.unit') }}</th>
            <th class="text-center">{{ $t('inventory.action') }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in materials" :key="item.matId">
            <td>#{{ item.matId }}</td>
            <td><strong>{{ item.matName }}</strong></td>
            <td>{{ item.matQuantity }}</td>
            <td><span class="badge">{{ item.unitSymbol }}</span></td>
            <td class="text-center">
              <button @click="deleteMaterial(item.matId)" class="btn-delete">🗑️</button>
            </td>
          </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useI18n } from 'vue-i18n';
import api from '../../services/api';

const { t } = useI18n();
const materials = ref([]);
const form = ref({ matName: '', matQuantity: 0, unitId: '' });

const fetchMaterials = async () => {
  try {
    const response = await api.get('/raw-materials');
    materials.value = response.data;
  } catch (error) {
    console.error("Error fetching materials", error);
  }
};

const saveMaterial = async () => {
  try {
    await api.post('/raw-materials', form.value);
    form.value = { matName: '', matQuantity: 0, unitId: '' };
    fetchMaterials();
  } catch (error) {
    alert(t('inventory.error_saving'));
  }
};

const deleteMaterial = async (id) => {
  if (confirm(t('inventory.deleteConfirm'))) {
    try {
      await api.delete(`/raw-materials/${id}`);
      fetchMaterials();
    } catch (error) {
      alert("Error deleting item");
    }
  }
};

onMounted(fetchMaterials);
</script>

<style scoped src="./Inventory.css"></style>