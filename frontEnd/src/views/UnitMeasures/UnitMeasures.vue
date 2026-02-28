<template>
  <div class="unit-measures-page">
    <div class="container">
      <div class="page-header">
        <h1>{{ t('units.title') }}</h1>
        <p class="page-subtitle">{{ t('units.subtitle') }}</p>
      </div>

      <!-- Form Section -->
      <section class="card form-section">
        <h3>{{ isEditing ? t('units.editUnit') : t('units.addNew') }}</h3>
        <div class="form-grid">
          <div class="input-group">
            <label for="unitName">{{ t('units.name') }}</label>
            <input 
              id="unitName"
              v-model="form.unitName" 
              type="text"
              class="custom-input" 
              :placeholder="t('units.namePlaceholder')"
              required 
            />
          </div>
          <div class="input-group">
            <label for="unitSymbol">{{ t('units.symbol') }}</label>
            <input 
              id="unitSymbol"
              v-model="form.unitSymbol" 
              type="text"
              class="custom-input"
              :placeholder="t('units.symbolPlaceholder')"
              maxlength="5"
              required 
            />
          </div>
          <button @click="saveUnit" class="btn-primary btn-save" :disabled="isLoading">
            <span v-if="!isLoading">{{ isEditing ? t('units.updateUnit') : t('units.save') }}</span>
            <span v-else>{{ t('common.loading') }}</span>
          </button>
          <button v-if="isEditing" @click="cancelEdit" class="btn-secondary btn-cancel">
            {{ t('common.cancel') }}
          </button>
        </div>
      </section>

      <!-- Units List Section -->
      <section class="card list-section">
        <div class="section-header">
          <h3>{{ t('units.list') }}</h3>
          <span class="count-badge">{{ filteredUnits.length }}</span>
        </div>

        <!-- Search Input -->
        <div class="search-box">
          <input 
            v-model="searchQuery"
            type="text"
            class="custom-input search-input"
            :placeholder="t('common.searchByName')"
          />
          <svg class="search-icon" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#62748a" stroke-width="2">
            <circle cx="11" cy="11" r="8"/>
            <path d="m21 21-4.35-4.35"/>
          </svg>
        </div>

        <div v-if="filteredUnits.length === 0" class="empty-state">
          <p>{{ searchQuery ? t('common.noResults') : t('units.empty') }}</p>
          <p class="text-muted">{{ searchQuery ? t('common.tryDifferentSearch') : t('units.emptyHint') }}</p>
        </div>

        <div v-else class="units-table-wrapper">
          <table class="units-table">
            <thead>
              <tr>
                <th>{{ t('units.name') }}</th>
                <th>{{ t('units.symbol') }}</th>
                <th style="width: 120px;">{{ t('units.actions') }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="unit in filteredUnits" :key="unit.unitId" class="unit-row">
                <td class="cell-name">{{ unit.unitName }}</td>
                <td class="cell-symbol">
                  <span class="symbol-badge">{{ unit.unitSymbol }}</span>
                </td>
                <td class="cell-actions">
                  <button 
                    @click="editUnit(unit)"
                    class="btn-edit"
                    :disabled="isLoading"
                    :title="`${t('common.edit')} ${unit.unitName}`"
                  >
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                      <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" stroke="#2c3e50" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                      <path d="m18.5 2.5 3 3L12 15l-4 1 1-4 9.5-9.5z" fill="#42b983"/>
                    </svg>
                  </button>
                  <button 
                    @click="deleteUnit(unit.unitId)"
                    class="btn-delete"
                    :disabled="isLoading"
                    :title="`${t('common.delete')} ${unit.unitName}`"
                  >
                    ✕
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </div>
  </div>

  <!-- Alert Modal -->
  <AlertModal
    :isOpen="modal.isOpen"
    :type="modal.type"
    :title="modal.title"
    :message="modal.message"
    :actionLabel="modal.actionLabel"
    @close="closeModal"
  />

  <!-- Confirm Delete Modal -->
  <ConfirmModal
    :isOpen="confirmDelete.isOpen"
    :title="confirmDelete.title"
    :message="confirmDelete.message"
    :cancelLabel="t('common.cancel')"
    :confirmLabel="t('common.delete')"
    @confirm="confirmDeleteAction"
    @cancel="cancelDelete"
  />
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useI18n } from '../../composables/useI18n'
import api from '../../services/api'
import AlertModal from '../../components/AlertModal.vue'
import ConfirmModal from '../../components/ConfirmModal.vue'

const { t } = useI18n()

const units = ref([])
const searchQuery = ref('')
const isLoading = ref(false)
const isEditing = ref(false)
const editingUnitId = ref(null)
const form = ref({ unitName: '', unitSymbol: '' })
const unitToDelete = ref(null)

// Computed property for filtering units by name
const filteredUnits = computed(() => {
  if (!searchQuery.value.trim()) return units.value
  
  const query = searchQuery.value.toLowerCase().trim()
  return units.value.filter(unit => 
    unit.unitName.toLowerCase().includes(query) ||
    unit.unitSymbol.toLowerCase().includes(query)
  )
})

const modal = ref({
  isOpen: false,
  type: 'info',
  title: '',
  message: '',
  actionLabel: 'OK'
})

const confirmDelete = ref({
  isOpen: false,
  title: '',
  message: ''
})

const fetchUnits = async () => {
  try {
    isLoading.value = true
    const response = await api.get('/unit-measures')
    units.value = response.data || []
  } catch (error) {
    showModal('error', t('common.error'), t('units.loadError'), t('common.ok'))
    console.error('Error fetching units:', error)
  } finally {
    isLoading.value = false
  }
}

const saveUnit = async () => {
  if (!form.value.unitName.trim() || !form.value.unitSymbol.trim()) {
    showModal('info', t('common.info'), t('units.fillAllFields'), t('common.ok'))
    return
  }

  try {
    isLoading.value = true
    
    if (isEditing.value && editingUnitId.value) {
      // Atualizar unidade existente
      await api.put(`/unit-measures/${editingUnitId.value}`, form.value)
      showModal('success', t('common.success'), `${t('units.title')} "${form.value.unitName}" ${t('units.editSuccess')}`, t('common.ok'))
      cancelEdit()
    } else {
      // Criar nova unidade
      await api.post('/unit-measures', form.value)
      showModal('success', t('common.success'), `${t('units.title')} "${form.value.unitName}" ${t('units.createdSuccess')}`, t('common.ok'))
      form.value = { unitName: '', unitSymbol: '' }
    }
    
    await fetchUnits()
  } catch (error) {
    const msg = error.response?.data?.message || (isEditing.value ? t('units.editError') : t('units.createError'))
    showModal('error', t('common.error'), msg, t('common.ok'))
    console.error('Error saving unit:', error)
  } finally {
    isLoading.value = false
  }
}

const editUnit = (unit) => {
  isEditing.value = true
  editingUnitId.value = unit.unitId
  form.value = {
    unitName: unit.unitName,
    unitSymbol: unit.unitSymbol
  }
}

const cancelEdit = () => {
  isEditing.value = false
  editingUnitId.value = null
  form.value = { unitName: '', unitSymbol: '' }
}

const deleteUnit = async (unitId) => {
  const unit = units.value.find(u => u.unitId === unitId)
  unitToDelete.value = unitId
  confirmDelete.value = {
    isOpen: true,
    title: t('common.deleteConfirm'),
    message: `${t('units.title')}: "${unit?.unitName}" - ${t('common.deleteConfirmMessage')}`
  }
}

const confirmDeleteAction = async () => {
  if (!unitToDelete.value) return

  try {
    isLoading.value = true
    await api.delete(`/unit-measures/${unitToDelete.value}`)
    showModal('success', t('common.success'), t('units.deletedSuccess'), t('common.ok'))
    unitToDelete.value = null
    await fetchUnits()
  } catch (error) {
    const msg = error.response?.data?.message || t('units.deleteError')
    showModal('error', t('common.error'), msg, t('common.ok'))
    console.error('Error deleting unit:', error)
  } finally {
    isLoading.value = false
  }
}

const cancelDelete = () => {
  unitToDelete.value = null
  confirmDelete.value.isOpen = false
}

const showModal = (type, title, message, actionLabel = 'OK') => {
  modal.value = { isOpen: true, type, title, message, actionLabel }
}

const closeModal = () => {
  modal.value.isOpen = false
}

onMounted(() => {
  fetchUnits()
})
</script>

<style scoped src="./UnitMeasures.css"></style>
