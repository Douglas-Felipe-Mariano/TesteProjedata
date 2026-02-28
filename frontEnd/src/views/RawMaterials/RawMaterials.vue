<template>
  <div class="raw-materials-page">
    <div class="container">
      <div class="page-header">
        <h1>{{ t('rawMaterials.title') }}</h1>
        <p class="page-subtitle">{{ t('rawMaterials.subtitle') }}</p>
      </div>

      <!-- Form Section -->
      <section class="card form-section">
        <h3>{{ isEditing ? t('rawMaterials.editMaterial') : t('rawMaterials.addNew') }}</h3>
        <div class="form-grid">
          <div class="input-group">
            <label for="matName">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="8" fill="#42b983"/>
                <circle cx="12" cy="12" r="4" fill="#2c3e50"/>
                <circle cx="12" cy="8" r="2" fill="#ffffff"/>
                <circle cx="8" cy="14" r="1" fill="#ffffff"/>
                <circle cx="16" cy="14" r="1" fill="#ffffff"/>
              </svg>
              {{ t('rawMaterials.name') }}
            </label>
            <input 
              id="matName"
              v-model="form.matName" 
              type="text"
              class="custom-input" 
              :placeholder="t('rawMaterials.namePlaceholder')"
              required 
            />
          </div>
          <div class="input-group">
            <label for="matQuantity">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <rect x="3" y="12" width="18" height="8" rx="2" fill="#42b983"/>
                <rect x="3" y="8" width="12" height="4" rx="1" fill="#2c3e50"/>
                <rect x="3" y="4" width="8" height="4" rx="1" fill="#62748a"/>
              </svg>
              {{ t('rawMaterials.quantity') }}
            </label>
            <input 
              id="matQuantity"
              v-model.number="form.matQuantity" 
              type="number"
              step="0.01"
              min="0"
              class="custom-input" 
              :placeholder="t('rawMaterials.quantityPlaceholder')"
              required 
            />
          </div>
          <div class="input-group">
            <label for="unitId">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <rect x="4" y="6" width="16" height="2" rx="1" fill="#42b983"/>
                <rect x="4" y="10" width="12" height="2" rx="1" fill="#2c3e50"/>
                <rect x="4" y="14" width="8" height="2" rx="1" fill="#62748a"/>
                <rect x="2" y="18" width="6" height="1" rx="0.5" fill="#2c3e50"/>
              </svg>
              {{ t('rawMaterials.unit') }}
            </label>
            <select 
              id="unitId"
              v-model.number="form.unitId" 
              class="custom-input"
              required
            >
              <option :value="null" disabled>{{ t('rawMaterials.selectUnit') }}</option>
              <option v-for="unit in units" :key="unit.unitId" :value="unit.unitId">
                {{ unit.unitName }} ({{ unit.unitSymbol }})
              </option>
            </select>
          </div>
          <button @click="saveMaterial" class="btn-primary btn-save" :disabled="isLoading">
            <span v-if="!isLoading">{{ isEditing ? t('rawMaterials.updateMaterial') : t('rawMaterials.save') }}</span>
            <span v-else>{{ t('common.loading') }}</span>
          </button>
          <button v-if="isEditing" @click="cancelEdit" class="btn-secondary btn-cancel">
            {{ t('common.cancel') }}
          </button>
        </div>
      </section>

      <!-- Materials List Section -->
      <section class="card list-section">
        <div class="section-header">
          <h3>{{ t('rawMaterials.list') }}</h3>
          <span class="count-badge">{{ filteredMaterials.length }}</span>
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

        <div v-if="filteredMaterials.length === 0" class="empty-state">
          <p>{{ searchQuery ? t('common.noResults') : t('rawMaterials.empty') }}</p>
          <p v-if="searchQuery" class="text-muted">{{ t('common.tryDifferentSearch') }}</p>
          <p v-else class="text-muted">{{ t('rawMaterials.emptyHint') }}</p>
        </div>

        <div v-else class="materials-table-wrapper">
          <table class="materials-table">
            <thead>
              <tr>
                <th>{{ t('rawMaterials.name') }}</th>
                <th>{{ t('rawMaterials.quantity') }}</th>
                <th>{{ t('rawMaterials.unit') }}</th>
                <th style="width: 120px;">{{ t('rawMaterials.actions') }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="material in filteredMaterials" :key="material.matId" class="material-row">
                <td class="cell-name">
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                    <circle cx="12" cy="12" r="8" fill="#42b983"/>
                    <circle cx="12" cy="12" r="4" fill="#2c3e50"/>
                    <circle cx="12" cy="8" r="2" fill="#ffffff"/>
                    <circle cx="8" cy="14" r="1" fill="#ffffff"/>
                    <circle cx="16" cy="14" r="1" fill="#ffffff"/>
                  </svg>
                  {{ material.matName }}
                </td>
                <td class="cell-quantity">{{ material.matQuantity }}</td>
                <td class="cell-unit">{{ material.unitName }} ({{ material.unitSymbol }})</td>
                <td class="cell-actions">
                  <button 
                    @click="editMaterial(material)"
                    class="btn-edit"
                    :disabled="isLoading"
                    :title="`${t('common.edit')} ${material.matName}`"
                  >
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                      <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" stroke="#2c3e50" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                      <path d="m18.5 2.5 3 3L12 15l-4 1 1-4 9.5-9.5z" fill="#42b983"/>
                    </svg>
                  </button>
                  <button 
                    @click="deleteMaterial(material.matId)"
                    class="btn-delete"
                    :disabled="isLoading"
                    :title="`${t('common.delete')} ${material.matName}`"
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

const materials = ref([])
const units = ref([])
const isLoading = ref(false)
const isEditing = ref(false)
const editingMaterialId = ref(null)
const form = ref({ matName: '', matQuantity: null, unitId: null })
const materialToDelete = ref(null)
const searchQuery = ref('')

// Computed property to filter materials by name
const filteredMaterials = computed(() => {
  if (!searchQuery.value.trim()) return materials.value
  const query = searchQuery.value.toLowerCase()
  return materials.value.filter(m => 
    m.matName.toLowerCase().includes(query)
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

const fetchMaterials = async () => {
  try {
    isLoading.value = true
    const response = await api.get('/raw-materials')
    materials.value = response.data || []
  } catch (error) {
    showModal('error', t('common.error'), t('rawMaterials.loadError'), t('common.ok'))
    console.error('Error fetching materials:', error)
  } finally {
    isLoading.value = false
  }
}

const fetchUnits = async () => {
  try {
    const response = await api.get('/unit-measures')
    units.value = response.data || []
  } catch (error) {
    console.error('Error fetching units:', error)
  }
}

const saveMaterial = async () => {
  if (!form.value.matName.trim()) {
    showModal('info', t('common.info'), t('rawMaterials.fillName'), t('common.ok'))
    return
  }

  if (form.value.matQuantity === null || form.value.matQuantity === '') {
    showModal('info', t('common.info'), t('rawMaterials.fillQuantity'), t('common.ok'))
    return
  }

  if (!form.value.unitId) {
    showModal('info', t('common.info'), t('rawMaterials.fillUnit'), t('common.ok'))
    return
  }

  try {
    isLoading.value = true
    
    const materialData = {
      matName: form.value.matName,
      matQuantity: parseFloat(form.value.matQuantity),
      unitId: form.value.unitId
    }
    
    if (isEditing.value && editingMaterialId.value) {
      // Atualizar material existente
      await api.put(`/raw-materials/${editingMaterialId.value}`, materialData)
      showModal('success', t('common.success'), `${t('rawMaterials.title')} "${form.value.matName}" ${t('rawMaterials.editSuccess')}`, t('common.ok'))
      cancelEdit()
    } else {
      // Criar novo material
      await api.post('/raw-materials', materialData)
      showModal('success', t('common.success'), `${t('rawMaterials.title')} "${form.value.matName}" ${t('rawMaterials.createdSuccess')}`, t('common.ok'))
      form.value = { matName: '', matQuantity: null, unitId: null }
    }
    
    await fetchMaterials()
  } catch (error) {
    const msg = error.response?.data?.message || (isEditing.value ? t('rawMaterials.editError') : t('rawMaterials.createError'))
    showModal('error', t('common.error'), msg, t('common.ok'))
    console.error('Error saving material:', error)
  } finally {
    isLoading.value = false
  }
}

const editMaterial = (material) => {
  isEditing.value = true
  editingMaterialId.value = material.matId
  form.value = {
    matName: material.matName,
    matQuantity: material.matQuantity,
    unitId: material.unitId
  }
}

const cancelEdit = () => {
  isEditing.value = false
  editingMaterialId.value = null
  form.value = { matName: '', matQuantity: null, unitId: null }
}

const deleteMaterial = async (matId) => {
  const material = materials.value.find(m => m.matId === matId)
  materialToDelete.value = matId
  confirmDelete.value = {
    isOpen: true,
    title: t('common.deleteConfirm'),
    message: `${t('rawMaterials.title')}: "${material?.matName}" - ${t('common.deleteConfirmMessage')}`
  }
}

const confirmDeleteAction = async () => {
  if (!materialToDelete.value) return

  try {
    isLoading.value = true
    await api.delete(`/raw-materials/${materialToDelete.value}`)
    showModal('success', t('common.success'), t('rawMaterials.deletedSuccess'), t('common.ok'))
    materialToDelete.value = null
    await fetchMaterials()
  } catch (error) {
    const msg = error.response?.data?.message || t('rawMaterials.deleteError')
    showModal('error', t('common.error'), msg, t('common.ok'))
    console.error('Error deleting material:', error)
  } finally {
    isLoading.value = false
  }
}

const cancelDelete = () => {
  materialToDelete.value = null
  confirmDelete.value.isOpen = false
}

const showModal = (type, title, message, actionLabel = 'OK') => {
  modal.value = { isOpen: true, type, title, message, actionLabel }
}

const closeModal = () => {
  modal.value.isOpen = false
}

onMounted(() => {
  fetchMaterials()
  fetchUnits()
})
</script>

<style scoped src="./RawMaterials.css"></style>
