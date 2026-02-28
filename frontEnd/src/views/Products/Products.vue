<template>
  <div class="products-page">
    <div class="container">
      <div class="page-header">
        <h1>{{ t('products.title') }}</h1>
        <p class="page-subtitle">{{ t('products.subtitle') }}</p>
      </div>

      <section class="card form-section">
        <h3>{{ isEditing ? t('products.editProduct') : t('products.addNew') }}</h3>
        <div class="form-grid">
          <div class="input-group">
            <label for="prodName">{{ t('products.name') }}</label>
            <input 
              id="prodName"
              v-model="form.prodName" 
              type="text"
              class="custom-input" 
              :placeholder="t('products.namePlaceholder')"
              required 
            />
          </div>
          <div class="input-group">
            <label for="prodPrice">{{ t('products.price') }}</label>
            <input 
              id="prodPrice"
              v-model.number="form.prodPrice" 
              type="number" 
              step="0.01" 
              class="custom-input" 
              :placeholder="t('products.pricePlaceholder')"
              required 
            />
          </div>
          <button 
            @click="saveProduct" 
            class="btn-primary btn-save"
            :disabled="isLoading"
          >
            <span v-if="!isLoading">{{ isEditing ? t('products.updateProduct') : t('products.save') }}</span>
            <span v-else>{{ t('common.loading') }}</span>
          </button>
          <button v-if="isEditing" @click="cancelEdit" class="btn-secondary btn-cancel">
            {{ t('common.cancel') }}
          </button>
        </div>
      </section>

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

      <section v-if="filteredProducts.length === 0" class="card empty-section">
        <div class="empty-state">
          <p>{{ searchQuery ? t('common.noResults') : t('products.empty') }}</p>
          <p v-if="searchQuery" class="text-muted">{{ t('common.tryDifferentSearch') }}</p>
        </div>
      </section>

      <div v-else class="products-grid">
        <div v-for="product in filteredProducts" :key="product.prodId" class="product-card">
          <div class="product-header">
            <div class="product-info">
              <h4>{{ product.prodName }}</h4>
              <span class="price-tag">{{ formatCurrency(product.prodPrice) }}</span>
            </div>
            <div class="product-actions">
              <button 
                @click="editProduct(product)"
                class="btn-edit"
                :disabled="isLoading"
                :title="`${t('common.edit')} ${product.prodName}`"
              >
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" stroke="#2c3e50" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  <path d="m18.5 2.5 3 3L12 15l-4 1 1-4 9.5-9.5z" fill="#42b983"/>
                </svg>
              </button>
              <button 
                @click="deleteProduct(product.prodId)"
                class="btn-delete"
                :disabled="isLoading"
                :title="`${t('common.delete')} ${product.prodName}`"
              >
                ✕
              </button>
            </div>
          </div>
          
          <div class="composition-box">
            <h5>{{ t('products.composition') || 'Composição' }}</h5>
            <ul v-if="product.compositions && product.compositions.length > 0" class="comp-list">
              <li v-for="comp in product.compositions" :key="`${product.prodId}-${comp.matId}`">
                <span class="comp-name">{{ comp.matName }}</span>
                <span class="qty">{{ comp.quantityNeeded }} {{ comp.unitSymbol }}</span>
              </li>
            </ul>
            <p v-else class="empty-comp">{{ t('products.noComposition') }}</p>

            <div class="add-comp-inline">
              <select 
                v-model="newComp[product.prodId].matId"
                class="comp-select"
                :disabled="materials.length === 0 || isLoading"
              >
                <option value="">{{ materials.length === 0 ? t('products.noMaterials') : t('products.selectMaterialPlaceholder') }}</option>
                <option v-for="m in materials" :key="m.matId" :value="m.matId">
                  {{ m.matName }} ({{ m.unitSymbol }})
                </option>
              </select>
              <input 
                v-model.number="newComp[product.prodId].quantity" 
                type="number" 
                placeholder="Qty"
                class="comp-qty"
                min="0"
                step="0.01"
              />
              <button 
                @click="addComposition(product.prodId)"
                class="btn-add-comp"
                :disabled="!newComp[product.prodId].matId || newComp[product.prodId].quantity <= 0 || isLoading"
              >
                +
              </button>
            </div>
          </div>
        </div>
      </div>
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
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue'
import { useI18n } from '../../composables/useI18n'
import api from '../../services/api'
import AlertModal from '../../components/AlertModal.vue'

const { t, formatCurrency } = useI18n()

const products = ref([])
const materials = ref([])
const isLoading = ref(false)
const isEditing = ref(false)
const editingProductId = ref(null)
const form = ref({ prodName: '', prodPrice: 0 })
const newComp = reactive({})
const searchQuery = ref('')

// Computed property to filter products by name
const filteredProducts = computed(() => {
  if (!searchQuery.value.trim()) return products.value
  const query = searchQuery.value.toLowerCase()
  return products.value.filter(p => 
    p.prodName.toLowerCase().includes(query)
  )
})

const modal = ref({
  isOpen: false,
  type: 'info',
  title: '',
  message: '',
  actionLabel: 'OK'
})

const fetchData = async () => {
  try {
    isLoading.value = true
    const [pRes, mRes] = await Promise.all([
      api.get('/products'),
      api.get('/raw-materials')
    ])
    products.value = pRes.data || []
    materials.value = mRes.data || []
    
    products.value.forEach(p => {
      if (!newComp[p.prodId]) {
        newComp[p.prodId] = { matId: '', quantity: 0 }
      }
    })
  } catch (error) {
    showModal('error', t('common.error'), t('products.loadError'), t('common.ok'))
    console.error('Error fetching data:', error)
  } finally {
    isLoading.value = false
  }
}

const saveProduct = async () => {
  if (!form.value.prodName.trim() || form.value.prodPrice <= 0) {
    showModal('info', t('common.info'), t('products.fillAllFields'), t('common.ok'))
    return
  }

  try {
    isLoading.value = true
    
    if (isEditing.value && editingProductId.value) {
      // Atualizar produto existente
      await api.put(`/products/${editingProductId.value}`, form.value)
      showModal('success', t('common.success'), `${t('products.title')} "${form.value.prodName}" ${t('products.editSuccess')}`, t('common.ok'))
      cancelEdit()
    } else {
      // Criar novo produto
      await api.post('/products', form.value)
      showModal('success', t('common.success'), `${t('products.title')} "${form.value.prodName}" ${t('products.createdSuccess')}`, t('common.ok'))
      form.value = { prodName: '', prodPrice: 0 }
    }
    
    await fetchData()
  } catch (error) {
    const msg = error.response?.data?.message || (isEditing.value ? t('products.editError') : t('products.createError'))
    showModal('error', t('common.error'), msg, t('common.ok'))
    console.error('Error saving product:', error)
  } finally {
    isLoading.value = false
  }
}

const editProduct = (product) => {
  isEditing.value = true
  editingProductId.value = product.prodId
  form.value = {
    prodName: product.prodName,
    prodPrice: product.prodPrice
  }
}

const cancelEdit = () => {
  isEditing.value = false
  editingProductId.value = null
  form.value = { prodName: '', prodPrice: 0 }
}

const deleteProduct = async (prodId) => {
  if (confirm(t('common.deleteConfirmMessage'))) {
    try {
      isLoading.value = true
      await api.delete(`/products/${prodId}`)
      showModal('success', t('common.success'), t('common.updateSuccess'), t('common.ok'))
      await fetchData()
    } catch (error) {
      const msg = error.response?.data?.message || 'Erro ao deletar produto'
      showModal('error', t('common.error'), msg, t('common.ok'))
      console.error('Error deleting product:', error)
    } finally {
      isLoading.value = false
    }
  }
}

const addComposition = async (prodId) => {
  if (!newComp[prodId].matId || newComp[prodId].quantity <= 0) {
    showModal('info', t('common.info'), t('products.selectMaterialAndQty'), t('common.ok'))
    return
  }

  const data = {
    prodId: prodId,
    matId: newComp[prodId].matId,
    quantityNeeded: newComp[prodId].quantity
  }
  
  try {
    isLoading.value = true
    await api.post('/product-compositions', data)
    const matName = materials.value.find(m => m.matId == newComp[prodId].matId)?.matName || t('products.material')
    showModal('success', t('common.success'), `"${matName}" ${t('products.addedComposition')}`, t('common.ok'))
    newComp[prodId] = { matId: '', quantity: 0 }
    await fetchData()
  } catch (error) {
    const msg = error.response?.data?.message || t('products.addCompositionError')
    showModal('error', t('common.error'), msg, t('common.ok'))
    console.error('Error adding composition:', error)
  } finally {
    isLoading.value = false
  }
}

const showModal = (type, title, message, actionLabel = 'OK') => {
  modal.value = { isOpen: true, type, title, message, actionLabel }
}

const closeModal = () => {
  modal.value.isOpen = false
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped src="./Products.css"></style>
