<template>
  <header class="app-header">
    <div class="container header-inner">
      <div class="brand-section">
        <img src="./assets/facoryLogo.svg" alt="ProfitFlow" class="brand-logo" />
      </div>
      <nav class="main-nav">
        <router-link to="/">{{ t('nav.inventory') }}</router-link>
        <router-link to="/products">{{ t('nav.products') }}</router-link>
        <router-link to="/unit-measures">{{ t('nav.units') }}</router-link>
        <router-link to="/raw-materials">{{ t('nav.rawMaterials') }}</router-link>
        <router-link to="/planning">{{ t('nav.optimization') }}</router-link>
      </nav>
      <div class="header-actions">
        <div class="lang-dropdown">
          <button 
            @click="toggleLangMenu"
            class="lang-button"
            :title="`Current language: ${currentLang.toUpperCase()}`"
          >
            <span class="lang-code" :style="{ 'background-color': languages.find(l => l.code === currentLang)?.bgColor, 'color': languages.find(l => l.code === currentLang)?.color }">
              {{ languages.find(l => l.code === currentLang)?.flag }}
            </span>
            <svg class="dropdown-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="6 9 12 15 18 9"></polyline>
            </svg>
          </button>
          
          <div v-if="showLangMenu" class="lang-menu">
            <button
              v-for="lang in languages"
              :key="lang.code"
              @click="changeLang(lang.code)"
              :class="['lang-option', { active: currentLang === lang.code }]"
            >
              <span class="lang-flag" :style="{ 'background-color': lang.bgColor, 'color': lang.color }">
                {{ lang.flag }}
              </span>
              <span class="lang-name">{{ lang.name }}</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </header>

  <main class="container app-main">
    <router-view />
  </main>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useI18n } from './composables/useI18n'

const { t, locale, setLocale } = useI18n()
const currentLang = ref(locale.value)
const showLangMenu = ref(false)

const languages = [
  { code: 'en', name: 'English', flag: '🇺🇸', color: '#ffffff', bgColor: '#3b82f6' },
  { code: 'pt', name: 'Português', flag: '🇵🇹', color: '#ffffff', bgColor: '#009c3b' },
  { code: 'es', name: 'Español', flag: '🇪🇸', color: '#ffffff', bgColor: '#c60b1e' }
]

const getLangLabel = (code) => {
  const lang = languages.find(l => l.code === code)
  return lang ? lang.name : code.toUpperCase()
}

const toggleLangMenu = () => {
  showLangMenu.value = !showLangMenu.value
}

const changeLang = (code) => {
  currentLang.value = code
  setLocale(code)
  showLangMenu.value = false
}

onMounted(() => {
  currentLang.value = locale.value
  
  document.addEventListener('click', (e) => {
    if (!e.target.closest('.lang-dropdown')) {
      showLangMenu.value = false
    }
  })
})
</script>

<style scoped>
.brand-section {
  display: flex;
  align-items: center;
}

.brand-logo {
  height: 50px;
  width: auto;
  object-fit: contain;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.lang-dropdown {
  position: relative;
}

.lang-button {
  background: rgba(66, 185, 131, 0.1);
  color: var(--accent-color);
  border: 1px solid var(--accent-color);
  padding: 0.5rem 0.75rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 0.9rem;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 0.4rem;
}

.lang-button:hover {
  background-color: rgba(66, 185, 131, 0.2);
}

.lang-button:focus {
  outline: none;
  border-color: var(--accent-color);
  box-shadow: 0 0 0 3px rgba(66, 185, 131, 0.1);
}

.lang-label {
  display: flex;
  align-items: center;
  gap: 0.4rem;
}

.lang-code {
  font-size: 1.4rem;
  font-weight: 700;
  padding: 0.35rem 0.6rem;
  border-radius: 4px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 2.4rem;
  min-height: 2.4rem;
  transition: all 0.2s ease;
}

.dropdown-icon {
  width: 1.1em;
  height: 1.1em;
  transition: transform 0.2s ease;
}

.lang-button:hover .dropdown-icon {
  transform: scaleY(1.1);
}

.lang-menu {
  position: absolute;
  top: calc(100% + 0.5rem);
  right: 0;
  background: var(--surface-color);
  border: 1px solid rgba(66, 185, 131, 0.3);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(11, 18, 32, 0.12);
  min-width: 180px;
  z-index: 1000;
  animation: slideDown 0.2s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.lang-option {
  width: 100%;
  background: var(--surface-color);
  border: none;
  padding: 0.75rem 1rem;
  text-align: left;
  cursor: pointer;
  font-weight: 500;
  font-size: 0.95rem;
  color: var(--text-main);
  display: flex;
  align-items: center;
  gap: 0.75rem;
  transition: all 0.2s ease;
  border-left: 3px solid transparent;
}

.lang-option:hover {
  background: rgba(66, 185, 131, 0.08);
}

.lang-option.active {
  background: rgba(66, 185, 131, 0.12);
  border-left-color: var(--accent-color);
  color: var(--accent-color);
  font-weight: 600;
}

.lang-flag {
  font-size: 1.8rem;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 2.8rem;
  min-height: 2.8rem;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.lang-name {
  flex: 1;
}
</style>