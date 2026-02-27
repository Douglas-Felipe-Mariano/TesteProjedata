<template>
  <div class="products-page">
    <header class="page-header">
      <h1>{{ $t('products.title') }}</h1>
    </header>

    <section class="card form-section">
      <h3>{{ $t('products.addNew') }}</h3>
      <div class="form-grid">
        <div class="input-group">
          <label>{{ $t('products.name') }}</label>
          <input v-model="form.prodName" class="custom-input" required />
        </div>
        <div class="input-group">
          <label>{{ $t('products.price') }}</label>
          <input v-model.number="form.prodPrice" type="number" step="0.01" class="custom-input" required />
        </div>
        <button @click="saveProduct" class="btn-primary">{{ $t('products.save') }}</button>
      </div>
    </section>

    <div class="products-grid">
      <div v-for="product in products" :key="product.prodId" class="product-card">
        <div class="product-header">
          <h4>{{ product.prodName }}</h4>
          <span class="price-tag">{{ formatCurrency(product.prodPrice) }}</span>
        </div>
        
        <div class="composition-box">
          <h5>{{ $t('products.composition') }}</h5>
          <ul class="comp-list">
            <li v-for="comp in product.compositions" :key="comp.matId">
              {{ comp.matName }} <span class="qty">{{ comp.quantityNeeded }} {{ comp.unitSymbol }}</span>
            </li>
          </ul>

          <div class="add-comp-inline">
            <select v-model="newComp[product.prodId].matId">
              <option value="">{{ $t('products.add') }}...</option>
              <option v-for="m in materials" :key="m.matId" :value="m.matId">{{ m.matName }}</option>
            </select>
            <input v-model.number="newComp[product.prodId].quantity" type="number" placeholder="Qty" />
            <button @click="addComposition(product.prodId)">+</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import api from '../../services/api'; // Ajustado para subir dois níveis

const products = ref([]);
const materials = ref([]);
const form = ref({ prodName: '', prodPrice: 0 });
const newComp = reactive({});

const fetchData = async () => {
  try {
    const [pRes, mRes] = await Promise.all([
      api.get('/products'),
      api.get('/raw-materials')
    ]);
    products.value = pRes.data;
    materials.value = mRes.data;
    
    // Inicializa o estado reativo para cada formulário de composição
    products.value.forEach(p => {
      if (!newComp[p.prodId]) {
        newComp[p.prodId] = { matId: '', quantity: 0 };
      }
    });
  } catch (error) {
    console.error("Error fetching data", error);
  }
};

const saveProduct = async () => {
  try {
    await api.post('/products', form.value);
    form.value = { prodName: '', prodPrice: 0 };
    await fetchData();
  } catch (error) {
    alert("Error saving product");
  }
};

const addComposition = async (prodId) => {
  const data = {
    prodId: prodId,
    matId: newComp[prodId].matId,
    quantityNeeded: newComp[prodId].quantity
  };
  
  try {
    await api.post('/product-compositions', data);
    newComp[prodId] = { matId: '', quantity: 0 };
    await fetchData();
  } catch (error) {
    alert(error.response?.data?.message || "Error adding material");
  }
};

const formatCurrency = (v) => {
  return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(v);
};

onMounted(fetchData);
</script>

<style scoped src="./Products.css"></style>