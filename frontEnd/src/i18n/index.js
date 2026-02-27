import { createI18n } from 'vue-i18n';

const messages = {
  en: {
    nav: {
      inventory: 'Inventory',
      products: 'Products',
      optimization: 'Optimization'
    },
    inventory: {
      title: 'Raw Material Management',
      addNew: 'Add New Item',
      name: 'Material Name',
      quantity: 'Quantity',
      unit: 'Unit',
      action: 'Actions',
      save: 'Add to Stock',
      deleteConfirm: 'Are you sure?'
    },
    products: {
    title: 'Product Management',
    addNew: 'Create New Product',
    name: 'Product Name',
    price: 'Sale Price',
    description: 'Description',
    save: 'Save Product',
    composition: 'Composition / Recipe',
    selectMaterial: 'Select Material',
    add: 'Add'
    }
  },
  pt: {
    nav: {
      inventory: 'Estoque',
      products: 'Produtos',
      optimization: 'Otimização'
    },
    inventory: {
      title: 'Gestão de Matérias-Primas',
      addNew: 'Cadastrar Novo Item',
      name: 'Nome do Material',
      quantity: 'Quantidade',
      unit: 'Unidade',
      action: 'Ações',
      save: 'Adicionar ao Estoque',
      deleteConfirm: 'Tem certeza?'
    },
    products: {
    title: 'Gestão de Produtos',
    addNew: 'Criar Novo Produto',
    name: 'Nome do Produto',
    price: 'Preço de Venda',
    description: 'Descrição',
    save: 'Salvar Produto',
    composition: 'Composição / Receita',
    selectMaterial: 'Selecionar Material',
    add: 'Adicionar'
    }
  }
};

const i18n = createI18n({
  legacy: false, 
  locale: 'en', 
  fallbackLocale: 'en',
  messages,
});

export default i18n;