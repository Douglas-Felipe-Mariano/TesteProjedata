import { computed, ref } from 'vue'

/**
 * Enhanced i18n composable with full translations support.
 * Provides currency, number and date formatting based on current locale.
 */

// Translation messages for all supported languages
const messages = {
  en: {
    nav: {
      inventory: 'Dashboard',
      products: 'Products',
      units: 'Units',
      rawMaterials: 'Raw Materials',
      optimization: 'Optimization'
    },
    dashboard: {
      title: 'Dashboard',
      subtitle: 'Overview of your production system',
      totalProducts: 'Total Products',
      totalMaterials: 'Total Materials',
      totalUnits: 'Total Units',
      products: 'Products',
      materials: 'Total Materials',
      actions: 'Actions',
      viewAll: 'View all',
      empty: 'Empty',
      addNew: 'Add New'
    },
    products: {
      title: 'Products',
      subtitle: 'Add and manage products',
      addNew: 'New Product',
      editProduct: 'Edit Product',
      name: 'Product Name',
      namePlaceholder: 'Ex: Premium T-Shirt',
      price: 'Sale Price',
      pricePlaceholder: '0.00',
      description: 'Description',
      save: 'Save',
      updateProduct: 'Update',
      composition: 'Composition',
      selectMaterial: 'Select Material',
      selectMaterialPlaceholder: 'Select a material',
      noMaterials: 'No materials available',
      add: 'Add',
      empty: 'No products registered yet',
      noComposition: 'No composition',
      loadError: 'Could not load products',
      fillAllFields: 'Fill in product name and price',
      createdSuccess: 'Product created successfully',
      createError: 'Error creating product',
      editSuccess: 'Product updated successfully',
      editError: 'Error updating product',
      deleteSuccess: 'Product deleted successfully',
      deleteError: 'Error deleting product'
    },
    units: {
      title: 'Units of Measure',
      subtitle: 'Create and manage units',
      addNew: 'New Unit',
      editUnit: 'Edit Unit',
      name: 'Unit Name',
      namePlaceholder: 'Ex: Kilogram',
      symbol: 'Symbol',
      symbolPlaceholder: 'Ex: kg',
      save: 'Save',
      updateUnit: 'Update',
      empty: 'No units registered yet',
      loadError: 'Could not load units',
      fillAllFields: 'Fill in unit name and symbol',
      createdSuccess: 'Unit created successfully',
      createError: 'Error creating unit',
      editSuccess: 'Unit updated successfully',
      editError: 'Error updating unit',
      deleteSuccess: 'Unit deleted successfully',
      deleteError: 'Error deleting unit'
    },
    materials: {
      title: 'Raw Materials',
      subtitle: 'Manage raw materials inventory',
      addNew: 'New Material',
      editMaterial: 'Edit Material',
      name: 'Material Name',
      namePlaceholder: 'Ex: Cotton Fabric',
      quantity: 'Quantity',
      quantityPlaceholder: '0',
      unit: 'Unit',
      selectUnit: 'Select a unit',
      save: 'Save',
      updateMaterial: 'Update',
      empty: 'No materials registered yet',
      loadError: 'Could not load materials',
      fillAllFields: 'Fill in all required fields',
      createdSuccess: 'Material created successfully',
      createError: 'Error creating material',
      editSuccess: 'Material updated successfully',
      editError: 'Error updating material',
      deleteSuccess: 'Material deleted successfully',
      deleteError: 'Error deleting material'
    },
    planning: {
      title: 'Production Planning',
      subtitle: 'Optimize your production schedule',
      calculate: 'Calculate',
      results: 'Results',
      noResults: 'No results yet',
      noData: 'No planning data available',
      product: 'Product',
      quantity: 'Quantity',
      cost: 'Cost',
      profit: 'Profit',
      totalRevenue: 'Total Revenue',
      productionPlan: 'Production Plan',
      subtotal: 'Subtotal',
      suggestions: 'Generate Suggestion',
      generateFirst: 'Generate Your First Suggestion'
    },
    common: {
      save: 'Save',
      cancel: 'Cancel',
      edit: 'Edit',
      delete: 'Delete',
      loading: 'Loading...',
      error: 'Error',
      success: 'Success',
      confirm: 'Confirm',
      ok: 'OK',
      deleteConfirm: 'Confirm Delete',
      deleteConfirmMessage: 'Are you sure you want to delete this item?',
      actions: 'Actions',
      info: 'Info',
      searchByName: 'Search by name...',
      noResults: 'No results found',
      tryDifferentSearch: 'Try a different search term'
    },
    rawMaterials: {
      title: 'Raw Materials',
      subtitle: 'Manage raw materials inventory',
      addNew: 'New Material',
      editMaterial: 'Edit Material',
      name: 'Material Name',
      namePlaceholder: 'Ex: Cotton Fabric',
      quantity: 'Quantity',
      quantityPlaceholder: '0',
      unit: 'Unit',
      selectUnit: 'Select a unit',
      save: 'Save',
      updateMaterial: 'Update',
      empty: 'No materials registered yet',
      emptyHint: 'Start by adding a new material',
      list: 'Materials List',
      actions: 'Actions',
      loadError: 'Could not load materials',
      fillAllFields: 'Fill in all required fields',
      fillName: 'Fill in the material name',
      fillQuantity: 'Fill in the quantity',
      fillUnit: 'Select a unit of measure',
      createdSuccess: 'created successfully',
      createError: 'Error creating material',
      editSuccess: 'updated successfully',
      editError: 'Error updating material',
      deletedSuccess: 'Material deleted successfully',
      deleteError: 'Error deleting material'
    }
  },
  pt: {
    nav: {
      inventory: 'Painel',
      products: 'Produtos',
      units: 'Unidades',
      rawMaterials: 'Materiais',
      optimization: 'Otimização'
    },
    dashboard: {
      title: 'Painel de Controle',
      subtitle: 'Visão geral do sistema de produção',
      totalProducts: 'Total de Produtos',
      totalMaterials: 'Total de Materiais',
      totalUnits: 'Total de Unidades',
      products: 'Produtos',
      materials: 'Total de Materiais',
      actions: 'Ações',
      viewAll: 'Ver tudo',
      empty: 'Vazio',
      addNew: 'Adicionar'
    },
    products: {
      title: 'Produtos',
      subtitle: 'Adicione e gerencie produtos',
      addNew: 'Novo Produto',
      editProduct: 'Editar Produto',
      name: 'Nome do Produto',
      namePlaceholder: 'Ex: Camiseta Premium',
      price: 'Preço de Venda',
      pricePlaceholder: '0,00',
      description: 'Descrição',
      save: 'Salvar',
      updateProduct: 'Atualizar',
      composition: 'Composição',
      selectMaterial: 'Selecionar Material',
      selectMaterialPlaceholder: 'Selecione um material',
      noMaterials: 'Nenhum material disponível',
      add: 'Adicionar',
      empty: 'Nenhum produto cadastrado',
      noComposition: 'Sem composição',
      loadError: 'Não foi possível carregar produtos',
      fillAllFields: 'Preencha nome e preço do produto',
      createdSuccess: 'Produto criado com sucesso',
      createError: 'Erro ao criar produto',
      editSuccess: 'Produto atualizado com sucesso',
      editError: 'Erro ao atualizar produto',
      deleteSuccess: 'Produto excluído com sucesso',
      deleteError: 'Erro ao excluir produto'
    },
    units: {
      title: 'Unidades de Medida',
      subtitle: 'Crie e gerencie unidades',
      addNew: 'Nova Unidade',
      editUnit: 'Editar Unidade',
      name: 'Nome da Unidade',
      namePlaceholder: 'Ex: Quilograma',
      symbol: 'Símbolo',
      symbolPlaceholder: 'Ex: kg',
      save: 'Salvar',
      updateUnit: 'Atualizar',
      empty: 'Nenhuma unidade cadastrada',
      loadError: 'Não foi possível carregar unidades',
      fillAllFields: 'Preencha nome e símbolo',
      createdSuccess: 'Unidade criada com sucesso',
      createError: 'Erro ao criar unidade',
      editSuccess: 'Unidade atualizada com sucesso',
      editError: 'Erro ao atualizar unidade',
      deleteSuccess: 'Unidade excluída com sucesso',
      deleteError: 'Erro ao excluir unidade'
    },
    materials: {
      title: 'Matérias-Primas',
      subtitle: 'Gerencie o estoque de materiais',
      addNew: 'Novo Material',
      editMaterial: 'Editar Material',
      name: 'Nome do Material',
      namePlaceholder: 'Ex: Tecido de Algodão',
      quantity: 'Quantidade',
      quantityPlaceholder: '0',
      unit: 'Unidade',
      selectUnit: 'Selecione uma unidade',
      save: 'Salvar',
      updateMaterial: 'Atualizar',
      empty: 'Nenhum material cadastrado',
      loadError: 'Não foi possível carregar materiais',
      fillAllFields: 'Preencha todos os campos obrigatórios',
      createdSuccess: 'Material criado com sucesso',
      createError: 'Erro ao criar material',
      editSuccess: 'Material atualizado com sucesso',
      editError: 'Erro ao atualizar material',
      deleteSuccess: 'Material excluído com sucesso',
      deleteError: 'Erro ao excluir material'
    },
    planning: {
      title: 'Planejamento de Produção',
      subtitle: 'Otimize sua programação de produção',
      calculate: 'Calcular',
      results: 'Resultados',
      noResults: 'Sem resultados ainda',
      noData: 'Nenhum dado de planejamento disponível',
      product: 'Produto',
      quantity: 'Quantidade',
      cost: 'Custo',
      profit: 'Lucro',
      totalRevenue: 'Receita Total',
      productionPlan: 'Plano de Produção',
      subtotal: 'Subtotal',
      suggestions: 'Gerar Sugestão',
      generateFirst: 'Gere Sua Primeira Sugestão'
    },
    common: {
      save: 'Salvar',
      cancel: 'Cancelar',
      edit: 'Editar',
      delete: 'Excluir',
      loading: 'Carregando...',
      error: 'Erro',
      success: 'Sucesso',
      confirm: 'Confirmar',
      ok: 'OK',
      deleteConfirm: 'Confirmar Exclusão',
      deleteConfirmMessage: 'Tem certeza que deseja excluir este item?',
      actions: 'Ações',
      info: 'Info',
      searchByName: 'Buscar por nome...',
      noResults: 'Nenhum resultado encontrado',
      tryDifferentSearch: 'Tente um termo de busca diferente'
    },
    rawMaterials: {
      title: 'Matérias-Primas',
      subtitle: 'Gerencie o estoque de materiais',
      addNew: 'Novo Material',
      editMaterial: 'Editar Material',
      name: 'Nome do Material',
      namePlaceholder: 'Ex: Tecido de Algodão',
      quantity: 'Quantidade',
      quantityPlaceholder: '0',
      unit: 'Unidade',
      selectUnit: 'Selecione uma unidade',
      save: 'Salvar',
      updateMaterial: 'Atualizar',
      empty: 'Nenhum material cadastrado',
      emptyHint: 'Comece adicionando um novo material',
      list: 'Lista de Materiais',
      actions: 'Ações',
      loadError: 'Não foi possível carregar materiais',
      fillAllFields: 'Preencha todos os campos obrigatórios',
      fillName: 'Preencha o nome do material',
      fillQuantity: 'Preencha a quantidade',
      fillUnit: 'Selecione uma unidade de medida',
      createdSuccess: 'criado com sucesso',
      createError: 'Erro ao criar material',
      editSuccess: 'atualizado com sucesso',
      editError: 'Erro ao atualizar material',
      deletedSuccess: 'Material excluído com sucesso',
      deleteError: 'Erro ao excluir material'
    }
  },
  es: {
    nav: {
      inventory: 'Panel',
      products: 'Productos',
      units: 'Unidades',
      rawMaterials: 'Materiales',
      optimization: 'Optimización'
    },
    dashboard: {
      title: 'Panel de Control',
      subtitle: 'Resumen del sistema de producción',
      totalProducts: 'Total de Productos',
      totalMaterials: 'Total de Materiales',
      totalUnits: 'Total de Unidades',
      products: 'Productos',
      materials: 'Total de Materiales',
      actions: 'Acciones',
      viewAll: 'Ver todo',
      empty: 'Vacío',
      addNew: 'Agregar'
    },
    products: {
      title: 'Productos',
      subtitle: 'Agregue y gestione productos',
      addNew: 'Nuevo Producto',
      editProduct: 'Editar Producto',
      name: 'Nombre del Producto',
      namePlaceholder: 'Ej: Camiseta Premium',
      price: 'Precio de Venta',
      pricePlaceholder: '0.00',
      description: 'Descripción',
      save: 'Guardar',
      updateProduct: 'Actualizar',
      composition: 'Composición',
      selectMaterial: 'Seleccionar Material',
      selectMaterialPlaceholder: 'Seleccione un material',
      noMaterials: 'No hay materiales disponibles',
      add: 'Agregar',
      empty: 'No hay productos registrados',
      noComposition: 'Sin composición',
      loadError: 'No se pudieron cargar los productos',
      fillAllFields: 'Complete nombre y precio del producto',
      createdSuccess: 'Producto creado con éxito',
      createError: 'Error al crear producto',
      editSuccess: 'Producto actualizado con éxito',
      editError: 'Error al actualizar producto',
      deleteSuccess: 'Producto eliminado con éxito',
      deleteError: 'Error al eliminar producto'
    },
    units: {
      title: 'Unidades de Medida',
      subtitle: 'Cree y gestione unidades',
      addNew: 'Nueva Unidad',
      editUnit: 'Editar Unidad',
      name: 'Nombre de la Unidad',
      namePlaceholder: 'Ej: Kilogramo',
      symbol: 'Símbolo',
      symbolPlaceholder: 'Ej: kg',
      save: 'Guardar',
      updateUnit: 'Actualizar',
      empty: 'No hay unidades registradas',
      loadError: 'No se pudieron cargar las unidades',
      fillAllFields: 'Complete nombre y símbolo',
      createdSuccess: 'Unidad creada con éxito',
      createError: 'Error al crear unidad',
      editSuccess: 'Unidad actualizada con éxito',
      editError: 'Error al actualizar unidad',
      deleteSuccess: 'Unidad eliminada con éxito',
      deleteError: 'Error al eliminar unidad'
    },
    materials: {
      title: 'Materias Primas',
      subtitle: 'Gestione el inventario de materiales',
      addNew: 'Nuevo Material',
      editMaterial: 'Editar Material',
      name: 'Nombre del Material',
      namePlaceholder: 'Ej: Tela de Algodón',
      quantity: 'Cantidad',
      quantityPlaceholder: '0',
      unit: 'Unidad',
      selectUnit: 'Seleccione una unidad',
      save: 'Guardar',
      updateMaterial: 'Actualizar',
      empty: 'No hay materiales registrados',
      loadError: 'No se pudieron cargar los materiales',
      fillAllFields: 'Complete todos los campos requeridos',
      createdSuccess: 'Material creado con éxito',
      createError: 'Error al crear material',
      editSuccess: 'Material actualizado con éxito',
      editError: 'Error al actualizar material',
      deleteSuccess: 'Material eliminado con éxito',
      deleteError: 'Error al eliminar material'
    },
    planning: {
      title: 'Planificación de Producción',
      subtitle: 'Optimice su programación de producción',
      calculate: 'Calcular',
      results: 'Resultados',
      noResults: 'Sin resultados aún',
      noData: 'No hay datos de planificación disponibles',
      product: 'Producto',
      quantity: 'Cantidad',
      cost: 'Costo',
      profit: 'Ganancia',
      totalRevenue: 'Ingresos Totales',
      productionPlan: 'Plan de Producción',
      subtotal: 'Subtotal',
      suggestions: 'Generar Sugerencia',
      generateFirst: 'Genere Su Primera Sugerencia'
    },
    common: {
      save: 'Guardar',
      cancel: 'Cancelar',
      edit: 'Editar',
      delete: 'Eliminar',
      loading: 'Cargando...',
      error: 'Error',
      success: 'Éxito',
      confirm: 'Confirmar',
      ok: 'OK',
      deleteConfirm: 'Confirmar Eliminación',
      deleteConfirmMessage: '¿Está seguro de que desea eliminar este elemento?',
      actions: 'Acciones',
      info: 'Info',
      searchByName: 'Buscar por nombre...',
      noResults: 'No se encontraron resultados',
      tryDifferentSearch: 'Intente con un término de búsqueda diferente'
    },
    rawMaterials: {
      title: 'Materias Primas',
      subtitle: 'Gestione el inventario de materiales',
      addNew: 'Nuevo Material',
      editMaterial: 'Editar Material',
      name: 'Nombre del Material',
      namePlaceholder: 'Ej: Tela de Algodón',
      quantity: 'Cantidad',
      quantityPlaceholder: '0',
      unit: 'Unidad',
      selectUnit: 'Seleccione una unidad',
      save: 'Guardar',
      updateMaterial: 'Actualizar',
      empty: 'No hay materiales registrados',
      emptyHint: 'Comience agregando un nuevo material',
      list: 'Lista de Materiales',
      actions: 'Acciones',
      loadError: 'No se pudieron cargar los materiales',
      fillAllFields: 'Complete todos los campos requeridos',
      fillName: 'Complete el nombre del material',
      fillQuantity: 'Complete la cantidad',
      fillUnit: 'Seleccione una unidad de medida',
      createdSuccess: 'creado con éxito',
      createError: 'Error al crear material',
      editSuccess: 'actualizado con éxito',
      editError: 'Error al actualizar material',
      deletedSuccess: 'Material eliminado con éxito',
      deleteError: 'Error al eliminar material'
    }
  }
}

// Reactive locale state (persisted in localStorage)
const locale = ref(localStorage.getItem('locale') || 'en')

export function useI18n() {
  
  /**
   * Translation function - returns text for the given key in current locale
   */
  const t = (key, params = {}) => {
    const keys = key.split('.')
    let value = messages[locale.value]
    
    // Navigate through nested keys
    for (const k of keys) {
      if (value && typeof value === 'object' && k in value) {
        value = value[k]
      } else {
        // Fallback: try English, then generate from key
        value = getFromEnglish(key) || generateFallbackFromKey(key)
        break
      }
    }
    
    // Return value or fallback
    if (typeof value === 'string') {
      return interpolateParams(value, params)
    }
    
    return generateFallbackFromKey(key)
  }

  /**
   * Fallback to English translation
   */
  const getFromEnglish = (key) => {
    const keys = key.split('.')
    let value = messages['en']
    
    for (const k of keys) {
      if (value && typeof value === 'object' && k in value) {
        value = value[k]
      } else {
        return null
      }
    }
    
    return typeof value === 'string' ? value : null
  }

  /**
   * Interpolates dynamic parameters into text strings
   */
  const interpolateParams = (text, params) => {
    if (!params || Object.keys(params).length === 0) return text
    
    return text.replace(/\{(\w+)\}/g, (match, key) => {
      return params[key] !== undefined ? params[key] : match
    })
  }

  /**
   * Generates readable text from translation key (last resort fallback)
   */
  const generateFallbackFromKey = (key) => {
    const parts = key.split('.')
    const lastPart = parts[parts.length - 1]
    
    return lastPart
      .replace(/([A-Z])/g, ' $1')
      .replace(/^./, str => str.toUpperCase())
      .trim()
  }

  /**
   * Sets the current locale and persists to localStorage
   */
  const setLocale = (newLocale) => {
    if (messages[newLocale]) {
      locale.value = newLocale
      localStorage.setItem('locale', newLocale)
    }
  }

  /**
   * Dynamic currency formatter based on locale
   */
  const formatCurrency = computed(() => {
    const currencyMap = {
      'en': { currency: 'USD', locale: 'en-US' },
      'pt': { currency: 'BRL', locale: 'pt-BR' },
      'es': { currency: 'EUR', locale: 'es-ES' }
    }
    
    const config = currencyMap[locale.value] || currencyMap['en']
    
    return (value) => new Intl.NumberFormat(config.locale, {
      style: 'currency',
      currency: config.currency
    }).format(value || 0)
  })

  /**
   * Number formatter based on locale
   */
  const formatNumber = computed(() => {
    const localeMap = { 'en': 'en-US', 'pt': 'pt-BR', 'es': 'es-ES' }
    const currentLocale = localeMap[locale.value] || 'en-US'
    
    return (value, options = {}) => new Intl.NumberFormat(currentLocale, {
      minimumFractionDigits: 0,
      maximumFractionDigits: 2,
      ...options
    }).format(value || 0)
  })

  /**
   * Returns singular/plural unit text based on quantity
   */
  const getUnitsText = (quantity) => {
    const num = Number(quantity) || 0
    if (locale.value === 'pt') {
      return num === 1 ? 'unidade' : 'unidades'
    } else if (locale.value === 'es') {
      return num === 1 ? 'unidad' : 'unidades'
    }
    return num === 1 ? 'unit' : 'units'
  }

  return {
    t,
    $t: t,
    locale,
    setLocale,
    formatCurrency,
    formatNumber,
    getUnitsText
  }
}
